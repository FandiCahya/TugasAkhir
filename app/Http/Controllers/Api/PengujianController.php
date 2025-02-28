<?php

namespace App\Http\Controllers\Api;

use App\Models\Pengembangan;
use Illuminate\Http\Request;
use App\Models\Pengujian;
use Illuminate\Support\Str;
use Carbon\Carbon;
use App\Http\Controllers\Controller;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\QueryException;
use App\Models\Pengajuan;

class PengujianController extends Controller
{
    public function index(Request $request)
    {
        try {
            $keyword = $request->query->get('keyword');
            $userId = $request->query->get('user_id');

            $pengujians = Pengujian::with(['details','pengembangan.pengajuan.user']);

            // Jika ada keyword, filter berdasarkan keyword
        if ($keyword) {
            $pengujians = $pengujians->where(function($query) use ($keyword) {
                $query->where('perangkat_lunak', 'like', '%' . $keyword . '%')
                    ->orWhere('tujuan', 'like', '%' . $keyword . '%')
                    ->orWhere('metode', 'like', '%' . $keyword . '%')
                      ->orWhereHas('pengembangan', function($query) use ($keyword) {
                          $query->where('status', 'like', '%' . $keyword . '%');
                      });
            });
        }

        // Filter berdasarkan user_id
        if ($userId) {
            $pengujians->whereHas('pengembangan.pengajuan.user', function ($query) use ($userId) {
                $query->where('id', '=', $userId); // Memfilter berdasarkan id user
            });
        }

        $pengujians = $pengujians->get();

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
                                'devisi' => $item->pengembangan->pengajuan->user->devisi ?? null,
                                'role' => $item->pengembangan->pengajuan->user->role ?? null,
                            ],
                            'pengembangan' => [
                                'id' => $item->pengembangan->id ?? null,
                                'tanggal_mulai' => $item->pengembangan->tanggal_mulai ?? null,
                                'tanggal_selesai' => $item->pengembangan->tanggal_selesai?? null,
                                'tahap' => $item->pengembangan->tahap ?? null,
                                'persentase' => $item->pengembangan->persentase ?? null,
                                'keterangan' => $item->pengembangan->keterangan ?? null,
                                'status' => $item->pengembangan->status ?? null,
                                'pengajuan' => [
                                    'id'=> $item->pengembangan->pengajuan->id ?? null,
                                    'tgl'=> $item->pengembangan->pengajuan->tgl ?? null,
                                    'nama_sistem' => $item->pengembangan->pengajuan->nama_sistem ?? null,
                                    'jenis' => $item->pengembangan->pengajuan->jenis ?? null,
                                    'rencana_anggaran' => $item->pengembangan->pengajuan->rencana_anggaran ?? null,
                                    'masalah' => $item->pengembangan->pengajuan->masalah ?? null,
                                    'output' => $item->pengembangan->pengajuan->output ?? null,
                                    'status' => $item->pengembangan->pengajuan->status ?? null,
                                    'user' => [
                                        'id' => $item->pengembangan->pengajuan->user->id ?? null,
                                        'name' => $item->pengembangan->pengajuan->user->name ?? null,
                                        'email' => $item->pengembangan->pengajuan->user->email ?? null,
                                        'devisi' => $item->pengembangan->pengajuan->user->devisi ?? null,
                                        'role' => $item->pengembangan->pengajuan->user->role ?? null,
                                    ],
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
                            'devisi' => $pengujian->user->devisi ?? null,
                            'role' => $pengujian->user->role ?? null,
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

            // Cari pengajuan yang terhubung dengan pengembangan ini dan update statusnya
            $pengembangan = Pengembangan::with('pengajuan')->find($validated['pengembangan_id']); 

            if ($pengembangan && $pengembangan->pengajuan) {
                // Update status pengajuan menjadi "testing"
                $pengembangan->pengajuan->status = 'testing';
                $pengembangan->pengajuan->save(); // Simpan perubahan status
            }

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
            $request->validate([
                'perangkat_lunak' => 'string',
                'versi' => 'string',
                'tujuan' => 'string',
                'metode' => 'string',
                'tanggal' => 'date',
                'pelaksana_id' => 'uuid|exists:users,id',
            ]);

            // Cari pengujian berdasarkan ID
            $pengujian = Pengujian::findOrFail($id);

            // Update pengujian
            $pengujian->update($request->all());

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
