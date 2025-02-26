<?php

namespace App\Http\Controllers\Api;

use App\Models\Pengembangan;
use App\Models\Pengajuan;
use App\Models\Pengujian;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\QueryException;
use Illuminate\Support\Str;

class PengembanganController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Request $request)
    {
        try {
            $keyword = $request->query->get('keyword');
            $status = $request->query->get('status');
            $pengembanganQuery = Pengembangan::with('pengajuan');
            if ($keyword) {
                $pengembanganQuery->where(function ($query) use ($keyword) {
                    $query->where('tahap', 'like', '%' . $keyword . '%')
                        ->orWhere('keterangan',  'like', '%' . $keyword . '%');
                });
            }
            if ($status) {
                $pengembanganQuery->where('status', 'like', '%' . $status . '%');
            }

            // Ambil data pengajuan setelah diterapkan filter jika ada
            $pengembangan = $pengembanganQuery->get();

            // $users = pengembangan::all();
            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengembangan->map(function ($item) {
                        return [
                            'id' => $item->id,
                            'tanggal_mulai' => $item->tanggal_mulai,
                            'tanggal_selesai' => $item->tanggal_selesai,
                            'tahap' => $item->tahap,
                            'persentase' => $item->persentase,
                            'keterangan' => $item->keterangan,
                            'status' => $item->status,
                            'updated_at' => $item->updated_at,
                            'created_at' => $item->created_at,
                            'pengajuan' => [
                                'id' => $item->pengajuan->id ?? null,
                                'tgl' => $item->pengajuan->tgl ?? null,
                                'nama_sistem' => $item->pengajuan->nama_sistem ?? null,
                                'jenis' => $item->pengajuan->jenis ?? null,
                                'rencana_anggaran' => $item->pengajuan->rencana_anggaran ?? null,
                                'masalah' => $item->pengajuan->masalah ?? null,
                                'output' => $item->pengajuan->output ?? null,
                                'tanda_tangan' => $item->pengajuan->tanda_tangan ?? null,
                                'alasan_penolakan' => $item->pengajuan->alasan_penolakan ?? null,
                                'status' => $item->pengajuan->status ?? null,
                                'user' => [
                                    'id' => $item->user->id ?? null,
                                    'name' => $item->user->name ?? null,
                                    'email' => $item->user->email ?? null,
                                    'role' => $item->user->role ?? null,
                                    'devisi' => $item->user->devisi ?? null,
                                ],
                            ],
                        ];
                    }),
                ],
                200,
            );
        } catch (\Exception $e) {
            // Menangani error lainnya
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => $e->getCode() ?: 500,
                        'message' => $e->getMessage(),
                    ],
                ],
                $e->getCode() ?: 500,
            );
        }
    }

    public function create()
    {
        //
    }
    public function store(Request $request)
    {
        try {
            // Validasi inputan
            $request->validate([
                'pengajuan_id' => 'required|uuid|exists:pengajuan,id', // Pengajuan harus ada dan menggunakan UUID
                'tanggal_mulai' => 'required|date',
                'tanggal_selesai' => 'required|date',
                'tahap' => 'required|string',
                'persentase' => 'required|integer|min:0|max:100',
                'keterangan' => 'nullable|string',
                'status' => 'nullable|in:developed,finished',
            ]);

            // Mengambil data pengajuan berdasarkan pengajuan_id
            $pengajuan = Pengajuan::findOrFail($request->pengajuan_id);

            // Mengambil user_id dari relasi pengajuan
            $user_id = $pengajuan->user_id; // Mengambil user_id dari relasi pengajuan

            // Membuat data pengembangan baru
            $pengembangan = Pengembangan::create([
                'pengajuan_id' => $request->pengajuan_id,
                'tanggal_mulai' => $request->tanggal_mulai,
                'tanggal_selesai' => $request->tanggal_selesai,
                'tahap' => $request->tahap,
                'persentase' => $request->persentase,
                'keterangan' => $request->keterangan,
                'status' => $request->status ?? 'developed',
                'user_id' => $user_id, // Menyimpan user_id yang diambil dari pengajuan
            ]);

            $pengajuan->status = 'developing';
            $pengajuan->save();

            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengembangan,
                ],
                201,
            );
        } catch (ValidationException $e) {
            // Jika validasi gagal
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => 422,
                        'message' => 'Validation failed',
                        'details' => $e->errors(), // Menampilkan kesalahan validasi
                    ],
                ],
                422,
            );
        } catch (QueryException $e) {
            // Menangani error jika query gagal
            return response()->json(
                [
                    'success' => false,
                    'error' => [
                        'code' => $e->getCode(),
                        'message' => $e->getMessage(),
                    ],
                ],
                $e->getCode() ?: 500,
            );
        } catch (\Exception $e) {
            // Menangani error lainnya
            return response()->json(
                [
                    'success' => false,
                    'error' => [
                        'code' => $e->getCode() ?: 500,
                        'message' => $e->getMessage(),
                    ],
                ],
                $e->getCode() ?: 500,
            );
        }
    }

    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        $usulan = Pengembangan::where('id',  $id);
        return response()->json($usulan);
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Pengembangan $pengembangan)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Pengembangan $pengembangan)
    {
        try {
            // Validasi inputan
            $request->validate([
                'tanggal_mulai' => 'required|date',
                'tanggal_selesai' => 'required|date',
                'tahap' => 'required|string',
                'persentase' => 'required|integer|min:0|max:100',
                'keterangan' => 'nullable|string',
                'status' => 'nullable|in:developed,finished',
            ]);

            // Memperbarui data pengembangan
            $pengembangan->update([
                'tanggal_mulai' => $request->tanggal_mulai,
                'tanggal_selesai' => $request->tanggal_selesai,
                'tahap' => $request->tahap,
                'persentase' => $request->persentase,
                'keterangan' => $request->keterangan,
                'status' => $request->status ?? 'developed',
            ]);

            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengembangan,
                ],
                200,
            );
        } catch (ValidationException $e) {
            // Jika validasi gagal
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => 422,
                        'message' => 'Validation failed',
                        'details' => $e->errors(), // Menampilkan kesalahan validasi
                    ],
                ],
                422,
            );
        } catch (QueryException $e) {
            // Menangani error jika query gagal
            return response()->json(
                [
                    'success' => false,
                    'error' => [
                        'code' => $e->getCode(),
                        'message' => $e->getMessage(),
                    ],
                ],
                $e->getCode() ?: 500,
            );
        } catch (\Exception $e) {
            // Menangani error lainnya
            return response()->json(
                [
                    'success' => false,
                    'error' => [
                        'code' => $e->getCode() ?: 500,
                        'message' => $e->getMessage(),
                    ],
                ],
                $e->getCode() ?: 500,
            );
        }
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Pengembangan $pengembangan)
    {
        try {
            // Menghapus data pengembangan
            $pengembangan->delete();

            return response()->json(
                [
                    'success' => true,
                    'message' => 'Pengembangan berhasil dihapus.',
                ],
                200,
            );
        } catch (QueryException $e) {
            // Menangani error jika query gagal
            return response()->json(
                [
                    'success' => false,
                    'error' => [
                        'code' => $e->getCode(),
                        'message' => $e->getMessage(),
                    ],
                ],
                $e->getCode() ?: 500,
            );
        } catch (\Exception $e) {
            // Menangani error lainnya
            return response()->json(
                [
                    'success' => false,
                    'error' => [
                        'code' => $e->getCode() ?: 500,
                        'message' => $e->getMessage(),
                    ],
                ],
                $e->getCode() ?: 500,
            );
        }
    }
}
