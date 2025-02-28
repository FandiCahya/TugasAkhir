<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Models\PersetujuanPengujian;
use App\Models\PersetujuanPengujianDetail;
use App\Models\PersetujuanPengujianModel;
use Illuminate\Support\Facades\Log;

class PersetujuanPengujianController extends Controller
{
    public function showall(Request $request)
    {
        try {
            $data = PersetujuanPengujianModel::all();
            $userId = $request->get('user_id');

            // Return the data as a JSON response
            return response()->json([
                'success' => true,
                'payload' => $data->map(function ($item) {
                    return [
                        'id' => $item->id,
                        'status' => $item->status,
                        'catatan' => $item->catatan,
                        'signature' => $item->signature,
                        'role' => $item->role,
                        'persetujuan_pengujian' => [
                            'id' => $item->persetujuanPengujian->id ?? null, // Check if pengujian is not null
                            'status' => $item->status ?? null,
                            'created_at' => $item->persetujuanPengujian->created_at ?? null,
                            'updated_at' => $item->persetujuanPengujian->updated_at ?? null,
                            'pengujian' => [
                                'perangkat_lunak' => $item->persetujuanPengujian->pengujian->perangkat_lunak ?? null,
                                'versi' => $item->persetujuanPengujian->pengujian->versi ?? null,
                                'tujuan' => $item->persetujuanPengujian->pengujian->tujuan ?? null,
                                'metode' => $item->persetujuanPengujian->pengujian->metode ?? null,
                                'tanggal' => $item->persetujuanPengujian->pengujian->tanggal ?? null,
                                'pengembangan' => [
                                    'id' => $item->persetujuanPengujian->pengujian->pengembangan->id ?? null,
                                    'tanggal_mulai' => $item->persetujuanPengujian->pengujian->pengembangan->tanggal_mulai ?? null,
                                    'tanggal_selesai' => $item->persetujuanPengujian->pengujian->pengembangan->tanggal_selesai ?? null,
                                    'tahap' => $item->persetujuanPengujian->pengujian->pengembangan->tahap ?? null,
                                    'persentase' => $item->persetujuanPengujian->pengujian->pengembangan->persentase ?? null,
                                    'keterangan' => $item->persetujuanPengujian->pengujian->pengembangan->keterangan ?? null,
                                    'status' => $item->persetujuanPengujian->pengujian->pengembangan->status ?? null,
                                    'pengajuan' => [
                                        'id' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->id ?? null,
                                        'tgl' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->tgl ?? null,
                                        'nama_sistem' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->nama_sistem ?? null,
                                        'jenis' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->jenis ?? null,
                                        'rencana_anggaran' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->rencana_anggaran ?? null,
                                        'masalah' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->masalah ?? null,
                                        'output' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->output ?? null,
                                        'status' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->status ?? null,
                                        'created_at' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->created_at ?? null,
                                        'user' => [
                                            'id' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->user->id ?? null,
                                            'name' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->user->name ?? null,
                                            'email' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->user->email ?? null,
                                            'devisi' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->user->devisi ?? null,
                                            'role' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->user->role ?? null,
                                            'created_at' => $item->persetujuanPengujian->pengujian->pengembangan->pengajuan->user->created_at ?? null,
                                        ],
                                    ],
                                ],
                            ],
                        ],
                        'user' => [
                            'id' => $item->user->id ?? null, // Check if user is not null
                            'name' => $item->user->name ?? null,
                            'email' => $item->user->email ?? null,
                        ],
                    ];
                }),
            ]);
        } catch (\Exception $e) {
            // Return error details if something goes wrong
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

    public function createApproval(Request $request)
    {
        try {
            // Validate incoming data
            $request->validate([
                'pengujian_id' => 'required|exists:pengujian_perangkat_lunak,id',
                'user_ids' => 'required|array|size:4', // Ensure exactly 4 users
                'user_ids.*' => 'exists:users,id', // Ensure each user_id is valid
            ]);

            // Create the persetujuan record
            $persetujuan = PersetujuanPengujian::create([
                'pengujian_id' => $request->pengujian_id,
            ]);

            // Dynamically assign users based on the request data
            $persetujuan->assignApprovalUsers($request->user_ids);

            return response()->json(
                [
                    'success' => true,
                    'message' => 'Approval users assigned successfully.',
                    'data' => $persetujuan,
                ],
                201,
            );
        } catch (\Exception $e) {
            return response()->json(
                [
                    'success' => false,
                    'error' => $e->getMessage(),
                ],
                400,
            );
        }
    }

    // public function approve(Request $request, $id)
    // {
    //     try {
    //         // Validate incoming data
    //         $request->validate([
    //             'user_id' => 'nullable|exists:users,id',
    //             'status' => 'required|in:setuju,tidak_setuju',
    //             'catatan' => 'nullable|string',
    //             'signature' => 'nullable|image|mimes:jpeg,png,jpg,gif|max:2048', // Signature as image
    //         ]);

    //         // Find the approval record
    //         $persetujuan = PersetujuanPengujianModel::findOrFail($id);

    //         // Find or create the PersetujuanPengujianDetail record for this user
    //         $detail = PersetujuanPengujianDetail::updateOrCreate(
    //             ['persetujuan_pengujian_id' => $persetujuan->id, 'user_id' => $request->user_id],
    //             [
    //                 'status' => $request->status,
    //                 'catatan' => $request->catatan,
    //                 'signature' => $this->handleSignatureUpload($request), // Upload signature
    //             ],
    //         );

    //         // After each approval, check if all 4 users have approved
    //         if ($this->checkIfAllApproved($persetujuan)) {
    //             // If all users approved, set the status of PersetujuanPengujian to 'approved'
    //             $persetujuan->update(['status' => 'approved']);
    //         }

    //         // Check if there is any rejection
    //         if ($this->checkIfRejected($persetujuan)) {
    //             return response()->json(
    //                 [
    //                     'success' => false,
    //                     'message' => 'Approval failed due to rejection from one or more users',
    //                     'data' => $persetujuan,
    //                 ],
    //                 400,
    //             );
    //         }

    //         // Check if all approvals are completed (all 4 approved)
    //         if ($this->checkIfAllApproved($persetujuan)) {
    //             return response()->json(
    //                 [
    //                     'success' => true,
    //                     'message' => 'Approval completed successfully',
    //                     'data' => $persetujuan,
    //                 ],
    //                 200,
    //             );
    //         }

    //         return response()->json(
    //             [
    //                 'success' => true,
    //                 'message' => 'Approval submitted successfully, waiting for other users',
    //                 'data' => $persetujuan,
    //             ],
    //             200,
    //         );
    //     } catch (\Exception $e) {
    //         return response()->json(
    //             [
    //                 'success' => false,
    //                 'error' => $e->getMessage(),
    //             ],
    //             500,
    //         );
    //     }
    // }

    public function approval(Request $request, $id)
    {
        try {
            // Validate incoming data
            $request->validate([
                'status' => 'required|in:setuju,tidak_setuju',
                'catatan' => 'nullable|string',
                'signature' => 'nullable|image|mimes:jpeg,png,jpg,gif|max:2048', // Signature as image
            ]);
            Log::info('Request data:', $request->all());

            // Find the PersetujuanPengujianDetail record by its ID
            $persetujuanDetail = PersetujuanPengujianModel::findOrFail($id); // We now search by PersetujuanPengujianDetail ID

            // Update or create the PersetujuanPengujianDetail record
            $persetujuanDetail->update([
                'status' => $request->status,
                'catatan' => $request->catatan,
                'signature' => $this->handleSignatureUpload($request), // Handle the signature upload
            ]);

            // Retrieve the associated PersetujuanPengujian model (for checking approval)
            $persetujuan = $persetujuanDetail->persetujuanPengujian;

            // After each approval, check if all 4 users have approved
            if ($this->checkIfAllApproved($persetujuan)) {
                // If all users approved, set the status of PersetujuanPengujian to 'approved'
                $persetujuan->update(['status' => 'approved']);

                // Now update the Pengajuan status to 'finished'
                $pengajuan = $persetujuan->pengujian->pengembangan->pengajuan; // Accessing Pengajuan through relationships

                if ($pengajuan) {
                    // Update the Pengajuan status to 'finished'
                    $pengajuan->status = 'finished';
                    $pengajuan->save(); // Save the changes to Pengajuan
                }
            }

            // Check if there is any rejection
            if ($this->checkIfRejected($persetujuan)) {
                return response()->json(
                    [
                        'success' => false,
                        'message' => 'Approval failed due to rejection from one or more users',
                        'data' => $persetujuan,
                    ],
                    400,
                );
            }

            // Check if all approvals are completed (all 4 approved)
            if ($this->checkIfAllApproved($persetujuan)) {
                return response()->json(
                    [
                        'success' => true,
                        'message' => 'Approval completed successfully',
                        'data' => $persetujuan,
                    ],
                    200,
                );
            }

            return response()->json(
                [
                    'success' => true,
                    'message' => 'Approval submitted successfully, waiting for other users',
                    'data' => $persetujuan,
                ],
                200,
            );
        } catch (\Exception $e) {
            Log::error('Approval error: ' . $e->getMessage());
            return response()->json(
                [
                    'success' => false,
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }

    // Helper method to check if all users approved
    protected function checkIfAllApproved($persetujuan)
    {
        // Ensure all 4 users have approved and signed
        return $persetujuan->details->where('status', 'setuju')->count() === 4 && $persetujuan->details->whereNotNull('signature')->count() === 4;
    }

    // Helper method to check if there is any rejection
    protected function checkIfRejected($persetujuan)
    {
        return $persetujuan->details->where('status', 'tidak_setuju')->count() > 0;
    }

    // Handle image upload for signature
    protected function handleSignatureUpload(Request $request)
    {
        if ($request->hasFile('signature')) {
            $signature = $request->file('signature');

            // Add logging to check the file upload
            Log::info('Signature file uploaded: ' . $signature->getClientOriginalName());

            $signaturePath = $signature->storeAs('signatures', $signature->getClientOriginalName(), 'public');
            return $signaturePath;
        }
        return null;
    }
}