<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Models\Pengujian;
use Illuminate\Support\Str;
use Carbon\Carbon;
use App\Http\Controllers\Controller;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\QueryException;

class PengujianController extends Controller
{
    public function index()
    {
        try {
            $pengujians = Pengujian::with(['details'])->get();

            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengujians->map(function ($item) {
                        return [
                            'id' => $item->id,
                            'perangkat_lunak' => $item->perangkat_lunak,
                            'versi' => $item->versi,
                            'tujuan' => $item->tujuan,
                            'metode' => $item->metode,
                            'tanggal' => $item->tanggal,
                            'updated_at' => $item->updated_at,
                            'created_at' => $item->created_at,
                            'pelaksana' => [
                                'id' => $item->pengembangan->pengajuan->user->id ?? null,
                                'name' => $item->pengembangan->pengajuan->user->name ?? null,
                                'email' => $item->pengembangan->pengajuan->user->email ?? null,
                            ],
                            'pengembangan' => [
                                'id' => $item->pengembangan->id ?? null,
                                'tahap' => $item->pengembangan->tahap ?? null,
                                'persentase' => $item->pengembangan->persentase ?? null,
                                'status' => $item->pengembangan->status ?? null,
                                'pengajuan' => [
                                    'nama_sistem' => $item->pengembangan->pengajuan->nama_sistem ?? null,
                                ],
                            ],
                            'pengujian_detail' => $item->details->map(function ($detail) {
                                return [
                                    'id' => $detail->id,
                                    'nama_uji' => $detail->nama_uji,
                                    'kasus_uji' => $detail->kasus_uji,
                                    'hasil_diharapkan' => $detail->hasil_diharapkan,
                                    'hasil_pengujian' => $detail->hasil_pengujian,
                                    'status' => $detail->status,
                                ];
                            }),
                        ];
                    }),
                ],
                200,
            );
        } catch (\Exception $e) {
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

    public function show($id)
    {
        try {
            $pengujian = Pengujian::with(['details', 'persetujuan.details.user', 'pengembangan', 'user'])->find($id);

            if (!$pengujian) {
                return response()->json(
                    [
                        'success' => false,
                        'payload' => [],
                        'error' => [
                            'code' => 404,
                            'message' => 'Pengujian tidak ditemukan',
                        ],
                    ],
                    404,
                );
            }

            return response()->json(
                [
                    'success' => true,
                    'payload' => [
                        'id' => $pengujian->id,
                        'perangkat_lunak' => $pengujian->perangkat_lunak,
                        'versi' => $pengujian->versi,
                        'tujuan' => $pengujian->tujuan,
                        'metode' => $pengujian->metode,
                        'tanggal' => $pengujian->tanggal,
                        'pelaksana' => [
                            'id' => $pengujian->user->id ?? null,
                            'name' => $pengujian->user->name ?? null,
                            'email' => $pengujian->user->email ?? null,
                        ],
                        'pengembangan' => [
                            'id' => $item->pengembangan->id ?? null,
                            'tahap' => $item->pengembangan->tahap ?? null,
                            'persentase' => $item->pengembangan->persentase ?? null,
                            'status' => $item->pengembangan->status ?? null,
                        ],
                        'updated_at' => $pengujian->updated_at,
                        'created_at' => $pengujian->created_at,
                        'pengujian_detail' => $pengujian->details->map(function ($detail) {
                            return [
                                'id' => $detail->id,
                                'nama_uji' => $detail->nama_uji,
                                'kasus_uji' => $detail->kasus_uji,
                                'hasil_diharapkan' => $detail->hasil_diharapkan,
                                'hasil_pengujian' => $detail->hasil_pengujian,
                                'status' => $detail->status,
                            ];
                        }),
                    ],
                ],
                200,
            );
        } catch (\Exception $e) {
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

    public function store(Request $request)
    {
        try {
            // Validasi input
            $validated = $request->validate([
                'pengembangan_id' => 'required|uuid|exists:pengembangan,id',
                'perangkat_lunak' => 'required|string',
                'versi' => 'required|string',
                'tujuan' => 'required|string',
                'metode' => 'required|string',
                'tanggal' => 'required|date',
                'pelaksana_id' => 'required|uuid|exists:users,id',
            ]);

            // Buat pengujian baru
            $pengujian = Pengujian::create([
                'id' => Str::uuid(),
                'pengembangan_id' => $validated['pengembangan_id'],
                'perangkat_lunak' => $validated['perangkat_lunak'],
                'versi' => $validated['versi'],
                'tujuan' => $validated['tujuan'],
                'metode' => $validated['metode'],
                'tanggal' => Carbon::createFromFormat('Y-m-d', $validated['tanggal']),
                'pelaksana_id' => $validated['pelaksana_id'],
            ]);

            // Kembalikan response sukses
            return response()->json(
                [
                    'success' => true,
                    'message' => 'Pengujian berhasil dibuat',
                    'payload' => $pengujian,
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
                500,
            );
        } catch (\Exception $e) {
            // Tangani error jika ada
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => $e->getCode(),
                        'message' => $e->getMessage(),
                    ],
                ],
                500,
            );
        }
    }

    public function update(Request $request, $id)
    {
        try {
            // Validasi input
            $validated = $request->validate([
                'perangkat_lunak' => 'required|string',
                'versi' => 'required|string',
                'tujuan' => 'required|string',
                'metode' => 'required|string',
                'tanggal' => 'required|date',
                'pelaksana_id' => 'nullable|uuid|exists:users,id',
            ]);

            // Cari pengujian berdasarkan ID
            $pengujian = Pengujian::findOrFail($id);

            // Update pengujian
            $pengujian->update([
                'perangkat_lunak' => $validated['perangkat_lunak'],
                'versi' => $validated['versi'],
                'tujuan' => $validated['tujuan'],
                'metode' => $validated['metode'],
                'tanggal' => Carbon::createFromFormat('Y-m-d', $validated['tanggal']),
                'pelaksana_id' => $validated['pelaksana_id'] ?? $pengujian->pelaksana_id,
            ]);

            // Kembalikan response sukses
            return response()->json(
                [
                    'success' => true,
                    'message' => 'Pengujian berhasil diperbarui',
                    'payload' => $pengujian,
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
                        'details' => $e->errors(),
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
                500,
            );
        } catch (\Exception $e) {
            // Tangani error jika ada
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => $e->getCode(),
                        'message' => $e->getMessage(),
                    ],
                ],
                500,
            );
        }
    }

    public function destroy($id)
    {
        try {
            // Cari pengujian berdasarkan ID
            $pengujian = Pengujian::findOrFail($id);

            // Hapus pengujian
            $pengujian->delete();

            // Kembalikan response sukses
            return response()->json(
                [
                    'success' => true,
                    'message' => 'Pengujian berhasil dihapus',
                ],
                200,
            );
        } catch (ModelNotFoundException $e) {
            // Jika pengujian tidak ditemukan
            return response()->json(
                [
                    'success' => false,
                    'error' => [
                        'code' => 404,
                        'message' => 'Pengujian tidak ditemukan',
                    ],
                ],
                404,
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
                500,
            );
        } catch (\Exception $e) {
            // Tangani error jika ada
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => $e->getCode(),
                        'message' => $e->getMessage(),
                    ],
                ],
                500,
            );
        }
    }
}
