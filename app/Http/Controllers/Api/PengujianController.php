<?php

namespace App\Http\Controllers\Api;

use App\Models\Pengujian;
use App\Models\Pengembangan;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\QueryException;
use Illuminate\Support\Str;

class PengujianController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Request $request)
    {
        try {
            $query = $request->input('search');
            $pengujian = pengujian::with('pengembangan', 'user') // Load relasi pengajuan dan user
                ->when($query, function ($queryBuilder) use ($query) {
                    return $queryBuilder->where('catatan', 'like', '%' . $query . '%'); // Pastikan menggunakan kolom yang benar
                })
                ->get();

            // $users = pengujian::all();
            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengujian->map(function ($item) {
                        return [
                            'id' => $item->id,
                            'hasil' => $item->hasil,
                            'catatan' => $item->catatan,
                            'updated_at' => $item->updated_at,
                            'created_at' => $item->created_at,
                            'pengembangan' => [
                                'id' => $item->pengembangan->id,
                                'tanggal_mulai' => $item->pengembangan->tanggal_mulai,
                                'tanggal_selesai' => $item->pengembangan->tanggal_selesai,
                                'tahap' => $item->pengembangan->tahap,
                                'persentase' => $item->pengembangan->persentase,
                                'keterangan' => $item->pengembangan->keterangan,
                                'status' => $item->pengembangan->status,
                                'updated_at' => $item->pengembangan->updated_at,
                                'created_at' => $item->pengembangan->created_at,
                                'user' => [
                                    'id' => $item->user->id ?? null,
                                    'name' => $item->user->name ?? null,
                                    'email' => $item->user->email ?? null,
                                    'role' => $item->user->role ?? null,
                                    'devisi' => $item->user->devisi ?? null,
                                ],
                                'pengajuan' => [
                                    'id' => $item->pengembangan->pengajuan->id,
                                    'tgl' => $item->pengembangan->pengajuan->tgl,
                                    'nama_sistem' => $item->pengembangan->pengajuan->nama_sistem,
                                    'jenis' => $item->pengembangan->pengajuan->jenis,
                                    'rencana_anggaran' => $item->pengembangan->pengajuan->rencana_anggaran,
                                    'masalah' => $item->pengembangan->pengajuan->masalah,
                                    'output' => $item->pengembangan->pengajuan->output,
                                    'tanda_tangan' => $item->pengembangan->pengajuan->tangan_tangan,
                                    'alasan_penolakan' => $item->pengembangan->pengajuan->alasan_penolakan,
                                    'status' => $item->pengembangan->pengajuan->status,
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

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        try {
            // Validasi input
            $request->validate([
                'pengembangan_id' => 'required|uuid|exists:pengembangan,id', // Harus ada di tabel pengembangan
                'hasil' => 'required|in:positif,negatif', // Hanya boleh positif atau negatif
                'catatan' => 'nullable|string',
                'tester_id' => 'required|uuid|exists:users,id', // Tester harus ada di tabel users
            ]);

            // Mengambil data pengembangan berdasarkan ID
            // $pengembangan = Pengembangan::findOrFail($request->pengembangan_id);

            // Membuat data pengujian baru
            $pengujian = Pengujian::create([
                'id' => Str::uuid(),
                'pengembangan_id' => $request->pengembangan_id,
                'hasil' => $request->hasil,
                'catatan' => $request->catatan,
                'tester_id' => $request->tester_id,
            ]);

            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengujian,
                ],
                201,
            );
        } catch (ValidationException $e) {
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => 422,
                        'message' => 'Validation failed',
                        'details' => $e->errors(),
                    ],
                ],
                422,
            );
        } catch (QueryException $e) {
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
        $usulan = Pengujian::where('id', $id);
        return response()->json($usulan);
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Pengujian $pengujian)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Pengujian $pengujian)
    {
        try {
            // Validasi input
            $request->validate([
                'hasil' => 'required|in:positif,negatif', // Hanya boleh positif atau negatif
                'catatan' => 'nullable|string',
            ]);

            // Update data pengujian
            $pengujian->update([
                'hasil' => $request->hasil,
                'catatan' => $request->catatan,
            ]);

            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengujian,
                ],
                200,
            );
        } catch (ValidationException $e) {
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => 422,
                        'message' => 'Validation failed',
                        'details' => $e->errors(),
                    ],
                ],
                422,
            );
        } catch (QueryException $e) {
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
    public function destroy(Pengujian $pengujian)
    {
        try {
            // Menghapus data pengembangan
            $pengujian->delete();

            return response()->json(
                [
                    'success' => true,
                    'message' => 'Pengujian berhasil dihapus.',
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
