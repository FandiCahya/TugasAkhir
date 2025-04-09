<?php

namespace App\Http\Controllers\Api;

use App\Models\Pengajuan;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Models\PersetujuanPengujian;
use App\Models\PersetujuanPengujianModel;
use Illuminate\Support\Facades\Log;
use Illuminate\Database\QueryException;
use Illuminate\Validation\ValidationException;
use Barryvdh\DomPDF\Facade\Pdf;

class ShowController extends Controller
{
    public function allshow(Request $request)
    {
        try {
            $query = PersetujuanPengujian::query();
            $persetujuanId = $request->get('persetujuanId');
            $statusPengajuan = $request->get('status_pengajuan');

            if ($persetujuanId) {
                $query->where('id', $persetujuanId);
            }
            // Filter berdasarkan status pengajuan
            if ($statusPengajuan) {
                $query->whereHas('pengujian.pengembangan.pengajuan', function ($q) use ($statusPengajuan) {
                    $q->where('status', $statusPengajuan);
                });
            }

            $data = $query->with(['details.user', 'pengujian.pengembangan.pengajuan.user'])->get();

            return response()->json([
                'success' => true,
                'payload' => $data->map(function ($item) {
                    return [
                        'pengajuan' => [
                            'id' => $item->pengujian->pengembangan->pengajuan->id ?? null,
                            'tgl' => $item->pengujian->pengembangan->pengajuan->tgl ?? null,
                            'nama_sistem' => $item->pengujian->pengembangan->pengajuan->nama_sistem ?? null,
                            'jenis' => $item->pengujian->pengembangan->pengajuan->jenis ?? null,
                            'rencana_anggaran' => $item->pengujian->pengembangan->pengajuan->rencana_anggaran ?? null,
                            'masalah' => $item->pengujian->pengembangan->pengajuan->masalah ?? null,
                            'output' => $item->pengujian->pengembangan->pengajuan->output ?? null,
                            'status' => $item->pengujian->pengembangan->pengajuan->status ?? null,
                            'created_at' => $item->pengujian->pengembangan->pengajuan->created_at ?? null,
                            'user' => [
                                'id' => $item->pengujian->pengembangan->pengajuan->user->id ?? null,
                                'name' => $item->pengujian->pengembangan->pengajuan->user->name ?? null,
                                'email' => $item->pengujian->pengembangan->pengajuan->user->email ?? null,
                                'devisi' => $item->pengujian->pengembangan->pengajuan->user->devisi ?? null,
                                'role' => $item->pengujian->pengembangan->pengajuan->user->role ?? null,
                                'created_at' => $item->pengujian->pengembangan->pengajuan->user->created_at ?? null,
                            ],
                        ],
                        'pengembangan' => [
                            'id' => $item->pengujian->pengembangan->id ?? null,
                            'tanggal_mulai' => $item->pengujian->pengembangan->tanggal_mulai ?? null,
                            'tanggal_selesai' => $item->pengujian->pengembangan->tanggal_selesai ?? null,
                            'tahap' => $item->pengujian->pengembangan->tahap ?? null,
                            'persentase' => $item->pengujian->pengembangan->persentase ?? null,
                            'keterangan' => $item->pengujian->pengembangan->keterangan ?? null,
                            'status' => $item->pengujian->pengembangan->status ?? null,
                        ],
                        'pengujian' => [
                            'id' => $item->pengujian->id ?? null,
                            'perangkat_lunak' => $item->pengujian->perangkat_lunak ?? null,
                            'versi' => $item->pengujian->versi ?? null,
                            'tujuan' => $item->pengujian->tujuan ?? null,
                            'metode' => $item->pengujian->metode ?? null,
                            'tanggal' => $item->pengujian->tanggal ?? null,
                        ],
                        'persetujuan_pengujian' => [
                            'id' => $item->id,
                            'status' => $item->status,
                            'created_at' => $item->created_at,
                            'updated_at' => $item->updated_at,
                        ],
                        'persetujuan_pengujian_details' => $item->details->map(function ($detail) {
                            return [
                                'id' => $detail->id,
                                'status' => $detail->status,
                                'catatan' => $detail->catatan,
                                'signature' => $detail->signature,
                                'role' => $detail->role,
                                'user' => [
                                    'id' => $detail->user->id ?? null,
                                    'name' => $detail->user->name ?? null,
                                    'email' => $detail->user->email ?? null,
                                ],
                            ];
                        }),
                    ];
                }),
            ]);
        } catch (\Exception $e) {
            return response()->json(
                [
                    'success' => false,
                    'message' => 'Something went wrong while fetching the data.',
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }

    private function formatPersetujuanPengujian($item)
    {
        return [
            'pengajuan' => [
                'id' => $item->pengujian->pengembangan->pengajuan->id ?? null,
                'tgl' => $item->pengujian->pengembangan->pengajuan->tgl ?? null,
                'nama_sistem' => $item->pengujian->pengembangan->pengajuan->nama_sistem ?? null,
                'jenis' => $item->pengujian->pengembangan->pengajuan->jenis ?? null,
                'rencana_anggaran' => $item->pengujian->pengembangan->pengajuan->rencana_anggaran ?? null,
                'masalah' => $item->pengujian->pengembangan->pengajuan->masalah ?? null,
                'output' => $item->pengujian->pengembangan->pengajuan->output ?? null,
                'status' => $item->pengujian->pengembangan->pengajuan->status ?? null,
                'created_at' => $item->pengujian->pengembangan->pengajuan->created_at ?? null,
                'user' => [
                    'id' => $item->pengujian->pengembangan->pengajuan->user->id ?? null,
                    'name' => $item->pengujian->pengembangan->pengajuan->user->name ?? null,
                    'email' => $item->pengujian->pengembangan->pengajuan->user->email ?? null,
                    'devisi' => $item->pengujian->pengembangan->pengajuan->user->devisi ?? null,
                    'role' => $item->pengujian->pengembangan->pengajuan->user->role ?? null,
                    'created_at' => $item->pengujian->pengembangan->pengajuan->user->created_at ?? null,
                ],
            ],
            'pengembangan' => [
                'id' => $item->pengujian->pengembangan->id ?? null,
                'tanggal_mulai' => $item->pengujian->pengembangan->tanggal_mulai ?? null,
                'tanggal_selesai' => $item->pengujian->pengembangan->tanggal_selesai ?? null,
                'tahap' => $item->pengujian->pengembangan->tahap ?? null,
                'persentase' => $item->pengujian->pengembangan->persentase ?? null,
                'keterangan' => $item->pengujian->pengembangan->keterangan ?? null,
                'status' => $item->pengujian->pengembangan->status ?? null,
            ],
            'pengujian' => [
                'id' => $item->pengujian->id ?? null,
                'perangkat_lunak' => $item->pengujian->perangkat_lunak ?? null,
                'versi' => $item->pengujian->versi ?? null,
                'tujuan' => $item->pengujian->tujuan ?? null,
                'metode' => $item->pengujian->metode ?? null,
                'tanggal' => $item->pengujian->tanggal ?? null,
            ],
            'persetujuan_pengujian' => [
                'id' => $item->id,
                'status' => $item->status,
                'created_at' => $item->created_at,
                'updated_at' => $item->updated_at,
            ],
            'persetujuan_pengujian_details' => $item->details->map(function ($detail) {
                return [
                    'id' => $detail->id,
                    'status' => $detail->status,
                    'catatan' => $detail->catatan,
                    'signature' => $detail->signature,
                    'role' => $detail->role,
                    'user' => [
                        'id' => $detail->user->id ?? null,
                        'name' => $detail->user->name ?? null,
                        'email' => $detail->user->email ?? null,
                    ],
                ];
            }),
        ];
    }

    public function previewLaporan($id)
    {
        try {
            $persetujuan = PersetujuanPengujian::with(['details.user', 'pengujian.pengembangan.pengajuan.user'])->findOrFail($id);

            $formatted = $this->formatPersetujuanPengujian($persetujuan);

            $pdf = PDF::loadView('pdf.laporan', ['data' => $formatted]);

            return $pdf->stream('preview-persetujuan-' . $id . '.pdf');
        } catch (\Exception $e) {
            return response()->json(
                [
                    'success' => false,
                    'message' => 'Gagal menampilkan preview PDF',
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }

    public function downloadLaporan($id)
    {
        try {
            $persetujuan = PersetujuanPengujian::with(['details.user', 'pengujian.pengembangan.pengajuan.user'])->findOrFail($id);

            $formatted = $this->formatPersetujuanPengujian($persetujuan);

            $pdf = PDF::loadView('pdf.laporan', ['data' => $formatted]);

            return $pdf->download('persetujuan-pengujian-' . $id . '.pdf');
        } catch (\Exception $e) {
            return response()->json(
                [
                    'success' => false,
                    'message' => 'Failed to download PDF.',
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }
}
