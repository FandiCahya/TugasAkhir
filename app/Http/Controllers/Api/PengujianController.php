<?php

namespace App\Http\Controllers\Api;

use App\Models\CatatanPengujian;
use App\Models\Pengembangan;
use App\Models\PengujianDetail;
use App\Models\PersetujuanPengujian;
use App\Models\PersetujuanPengujianDetail;
use Illuminate\Http\Request;
use App\Models\Pengujian;
use Illuminate\Support\Str;
use Carbon\Carbon;
use App\Http\Controllers\Controller;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\QueryException;
use App\Models\Pengajuan;
use App\Models\User;
use App\Models\PersetujuanPengujianModel;

class PengujianController extends Controller
{
    public function index(Request $request)
    {
        try {
            $keyword = $request->query->get('keyword');
            $userId = $request->query->get('user_id');
            $pengujianId = $request->query->get('pengujian_id');
            $disetujuiOleh = $request->query->get('disetujui_oleh');

            $pengujians = Pengujian::with([
                'details',
                'pengembangan.pengajuan.user',
                'catatan', // Tambahkan relasi ke catatan
                'persetujuan.persetujuan_detail.user'// Tambahkan relasi ke persetujuan]);
            ]);

            // Jika ada keyword, filter berdasarkan keyword
            if ($keyword) {
                $pengujians = $pengujians->where(function ($query) use ($keyword) {
                    $query
                        ->where('perangkat_lunak', 'like', '%' . $keyword . '%')
                        ->orWhere('tujuan', 'like', '%' . $keyword . '%')
                        ->orWhere('metode', 'like', '%' . $keyword . '%')
                        ->orWhereHas('pengembangan', function ($query) use ($keyword) {
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

            if ($pengujianId) {
                $pengujians->where('id', '=', $pengujianId);
            }
            
            // Filter berdasarkan disetujui oleh user tertentu
            if ($disetujuiOleh) {
                $pengujians->whereHas('persetujuan.persetujuan_detail.user', function ($query) use ($disetujuiOleh) {
                    $query->where('id', '=', $disetujuiOleh);
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
                            'status' => $item->status,
                            'updated_at' => $item->updated_at,
                            'created_at' => $item->created_at,
                            'pelaksana' => [
                                'id' => $item->pelaksana->id ?? null,
                                'name' => $item->pelaksana->name ?? null,
                                'email' => $item->pelaksana->email ?? null,
                                'devisi' => $item->pelaksana->devisi ?? null,
                                'role' => $item->pelaksana->role ?? null,
                            ],
                            'pengembangan' => [
                                'id' => $item->pengembangan->id ?? null,
                                'tanggal_mulai' => $item->pengembangan->tanggal_mulai ?? null,
                                'tanggal_selesai' => $item->pengembangan->tanggal_selesai ?? null,
                                'tahap' => $item->pengembangan->tahap ?? null,
                                'persentase' => $item->pengembangan->persentase ?? null,
                                'keterangan' => $item->pengembangan->keterangan ?? null,
                                'status' => $item->pengembangan->status ?? null,
                                'pengajuan' => [
                                    'id' => $item->pengembangan->pengajuan->id ?? null,
                                    'tgl' => $item->pengembangan->pengajuan->tgl ?? null,
                                    'nama_sistem' => $item->pengembangan->pengajuan->nama_sistem ?? null,
                                    'jenis' => $item->pengembangan->pengajuan->jenis ?? null,
                                    'rencana_anggaran' => $item->pengembangan->pengajuan->rencana_anggaran ?? null,
                                    'masalah' => $item->pengembangan->pengajuan->masalah ?? null,
                                    'output' => $item->pengembangan->pengajuan->output ?? null,
                                    'status' => $item->pengembangan->pengajuan->status ?? null,
                                    'created_at' => $item->pengembangan->pengajuan->created_at ?? null,
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
                                    'kategori'  => $detail->kategori,
                                    'status' => $detail->status,
                                ];
                            }),
                            'catatan' => $item->catatan->map(function ($catatan) {
                                return [
                                    'id' => $catatan->id,
                                    'uraian' => $catatan->uraian,
                                    'rencana_tindak_lanjut' => $catatan->rencana_tindak_lanjut,
                                    'penanggung_jawab' => $catatan->penanggung_jawab_id,
                                    'created_at' => $catatan->created_at,
                                ];
                            }),
                            'persetujuan' => $item->persetujuan->map(function ($persetujuan) {
                                return [
                                    'id' => $persetujuan->id,
                                    'status' => $persetujuan->status,
                                    'tanggal_persetujuan' => $persetujuan->created_at,
                                    'persetujuan_detail' => $persetujuan->persetujuan_detail->map(function ($detail) {
                                    return [
                                        'id' => $detail->id,
                                        'status' => $detail->status,
                                        'catatan' => $detail->catatan,
                                        'signature' => $detail->signature,
                                        // 'role' => $detail->role,
                                        'disetujui_oleh' => [
                                            'id' => $detail->user->id ?? null,
                                            'name' => $detail->user->name ?? null,
                                            'email' => $detail->user->email ?? null,
                                            'devisi' => $detail->user->devisi ?? null,
                                            'role' => $detail->user->role ?? null,
                                        ],
                                    ];
                                })
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
            // Validasi input utama
            $validated = $request->validate([
                'pengembangan_id' => 'required|uuid|exists:pengembangan,id',
                'perangkat_lunak' => 'required|string',
                'versi' => 'required|string',
                'tujuan' => 'required|string',
                'metode' => 'required|string',
                'tanggal' => 'required|date',
                'pelaksana_id' => 'uuid',
                'user_ids' => 'required|array|size:4',
                'user_ids.*' => 'exists:users,id',
                'pengujian_detail' => 'required|array|min:1',
                'pengujian_detail.*.nama_uji' => 'required|string|max:255',
                'pengujian_detail.*.kasus_uji' => 'required|string|max:255',
                'pengujian_detail.*.hasil_diharapkan' => 'required|string',
                'pengujian_detail.*.hasil_pengujian' => 'nullable|string',
                'pengujian_detail.*.kategori' => 'required|in:uji_positif,uji_negatif',
                'pengujian_detail.*.status' => 'required|string|max:50',
                // Validasi catatan pengujian
                'catatan.uraian' => 'nullable|string|max:255',
                'catatan.rencana_tindak_lanjut' => 'nullable|string|max:255',
                'catatan.penanggung_jawab_id' => 'nullable|exists:users,id',
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

            // Buat detail pengujian
            $pengujianDetails = [];
            foreach ($validated['pengujian_detail'] as $detail) {
                $pengujianDetails[] = PengujianDetail::create([
                    'pengujian_id' => $pengujian->id,
                    'nama_uji' => $detail['nama_uji'],
                    'kasus_uji' => $detail['kasus_uji'],
                    'hasil_diharapkan' => $detail['hasil_diharapkan'],
                    'hasil_pengujian' => $detail['hasil_pengujian'] ?? null,
                    'kategori' => $detail['kategori'],
                    'status' => $detail['status'],
                ]);
            }

            // Buat persetujuan pengujian
            $persetujuan = PersetujuanPengujian::create([
                'pengujian_id' => $pengujian->id,
            ]);
            $persetujuan->createPengujianDetail($validated['user_ids'], $persetujuan->id);
            // Ambil detail persetujuan setelah dibuat
            $persetujuanDetails = PersetujuanPengujianDetail::where('persetujuan_pengujian_id', $persetujuan->id)->get();

            // Update status pengembangan dan pengajuan
            $pengembangan = Pengembangan::find($validated['pengembangan_id']);
            if ($pengembangan) {
                $pengembangan->status = 'finished';
                $pengembangan->save();

                if ($pengembangan->pengajuan) {
                    $pengembangan->pengajuan->status = 'approval';
                    $pengembangan->pengajuan->save();
                }
            }

            // Buat catatan pengujian jika ada
            $catatanPengujian = null;
            if (!empty($validated['catatan']['uraian'])) {
                $catatanPengujian = CatatanPengujian::create([
                    'pengujian_id' => $pengujian->id,
                    'uraian' => $validated['catatan']['uraian'],
                    'rencana_tindak_lanjut' => $validated['catatan']['rencana_tindak_lanjut'] ?? null,
                    'penanggung_jawab_id' => $validated['catatan']['penanggung_jawab_id'] ?? null,
                ]);
            }

            // Kembalikan response sukses
            return response()->json(
                [
                    'success' => true,
                    'message' => 'Pengujian dan Approval berhasil dibuat',
                    'payload' => [
                        'pengujian' => $pengujian,
                        'pengujian_detail' => $pengujianDetails,
                        'persetujuan' => $persetujuan,
                        'persetujuan_detail' => $persetujuanDetails,
                        'catatan_pengujian' => $catatanPengujian,
                    ],
                ],
                201,
            );
        } catch (ValidationException $e) {
            return response()->json(
                [
                    'success' => false,
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
                500,
            );
        } catch (\Exception $e) {
            return response()->json(
                [
                    'success' => false,
                    'error' => [
                        'code' => 500,
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


