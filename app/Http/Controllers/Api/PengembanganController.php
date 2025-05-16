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
            $query = Pengembangan::with('pengajuan.user');

            // Filter keyword pada tahap dan keterangan
            if ($keyword = $request->query('keyword')) {
                $query->where(fn($q) => $q->where('tahap', 'like', "%$keyword%")->orWhere('keterangan', 'like', "%$keyword%"));
            }

            if ($status = $request->query('status')) {
                $query->where('status', 'like', "%$status%");
            }

            if ($pengajuanStatus = $request->query('pengajuanstatus')) {
                if (is_array($pengajuanStatus)) {
                    $query->whereHas('pengajuan', fn($q) => $q->whereIn('status', $pengajuanStatus));
                } else {
                    $query->whereHas('pengajuan', fn($q) => $q->where('status', 'like', "%$pengajuanStatus%"));
                }
            }

            // Filter berdasarkan relasi pengajuan.user: user_id, role, devisi
            $filters = ['user_id' => 'id', 'role' => 'role', 'devisi' => 'devisi'];
            foreach ($filters as $reqKey => $col) {
                if ($val = $request->query($reqKey)) {
                    $query->whereHas('pengajuan.user', fn($q) => $q->where($col, 'like', "%$val%"));
                }
            }

            $data = $query->get();

            return response()->json([
                'success' => true,
                'message' => 'Data pengembangan berhasil diambil.',
                'payload' => $data->map(
                    fn($item) => [
                        'id' => $item->id,
                        'tanggal_mulai' => $item->tanggal_mulai,
                        'tanggal_selesai' => $item->tanggal_selesai,
                        'tahap' => $item->tahap,
                        'persentase' => $item->persentase,
                        'keterangan' => $item->keterangan,
                        'status' => $item->status,
                        'updated_at' => $item->updated_at,
                        'created_at' => $item->created_at,
                        'pengajuan' => $item->pengajuan
                            ? [
                                'id' => $item->pengajuan->id,
                                'tgl' => $item->pengajuan->tgl,
                                'nama_sistem' => $item->pengajuan->nama_sistem,
                                'jenis' => $item->pengajuan->jenis,
                                'rencana_anggaran' => $item->pengajuan->rencana_anggaran,
                                'masalah' => $item->pengajuan->masalah,
                                'output' => $item->pengajuan->output,
                                'tanda_tangan' => $item->pengajuan->tanda_tangan,
                                'alasan_penolakan' => $item->pengajuan->alasan_penolakan,
                                'status' => $item->pengajuan->status,
                                'user' => $item->pengajuan->user
                                    ? [
                                        'id' => $item->pengajuan->user->id,
                                        'name' => $item->pengajuan->user->name,
                                        'email' => $item->pengajuan->user->email,
                                        'role' => $item->pengajuan->user->role,
                                        'devisi' => $item->pengajuan->user->devisi,
                                    ]
                                    : null,
                            ]
                            : null,
                    ],
                ),
            ]);
        } catch (\Exception $e) {
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => $e->getCode() ?: 500,
                        'message' => 'Terjadi kesalahan saat mengambil data pengembangan.',
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
                'status' => 'nullable|in:developed,testing,finished',
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
                    'message' => 'Data pengembangan berhasil disimpan.',
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
                        'message' => 'Gagal melakukan validasi',
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
                        'message' => 'Gagal melakukan query',
                        'details' => $e->getMessage(),
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
                        'message' => 'Gagal melakukan penyimpanan data',
                        'details' => $e->getMessage(),
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
        $usulan = Pengembangan::where('id', $id);
        return response()->json($usulan);
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Pengembangan $pengembangan) {}

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        try {
            $pengembangan = Pengembangan::findOrFail($id);
            // Validasi inputan
            $request->validate([
                'tanggal_mulai' => 'date',
                'tanggal_selesai' => 'date',
                'tahap' => 'string',
                'persentase' => 'integer|min:0|max:100',
                'keterangan' => 'string',
                'status' => 'in:developed,finished,testing',
            ]);

            // Memperbarui data pengembangan
            $pengembangan->update($request->all());

            return response()->json(
                [
                    'success' => true,
                    'message' => 'Data pengembangan berhasil diperbarui.',
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
                        'message' => 'Gagal melakukan validasi',
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
                        'message' => 'Gagal melakukan query',
                        'details' => $e->getMessage(),
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
                        'message' => 'Gagal melakukan penyimpanan data',
                        'details' => $e->getMessage(),
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
                        'message' => 'Gagal melakukan query',
                        'details' => $e->getMessage(),
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
                        'message' => 'Gagal melakukan penghapusan data',
                        'details' => $e->getMessage(),
                    ],
                ],
                $e->getCode() ?: 500,
            );
        }
    }
}
