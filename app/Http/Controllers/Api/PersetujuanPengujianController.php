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

class PersetujuanPengujianController extends Controller
{
    public function showall(Request $request)
    {
        try {
            $query = PersetujuanPengujianModel::query();
            $persetujuanId = $request->get('persetujuanId');
            $userId = $request->get('user_id');

            // Filter berdasarkan ID persetujuan_pengujian jika diberikan
            if ($persetujuanId) {
                $query->where('persetujuan_pengujian_id', $persetujuanId);
            }

            // Ambil semua data yang sesuai dengan filter
            $data = $query->get();

            // Return the data as a JSON response
            return response()->json([
                'success' => true,
                'message' => 'Berhasil mendapatkan data persetujuan pengujian',
                'payload' => $data->map(function ($item) {
                    return [
                        'id' => $item->id,
                        'status' => $item->status,
                        'catatan' => $item->catatan,
                        'signature' => $item->signature,
                        'persetujuan_pengujian' => $item->persetujuanPengujian ? [
                            'id' => $item->persetujuanPengujian->id,
                            'status' => $item->persetujuanPengujian->status,
                            'created_at' => $item->persetujuanPengujian->created_at,
                            'updated_at' => $item->persetujuanPengujian->updated_at,
                        ] : null,
                        'user' => $item->user ? [
                            'id' => $item->user->id,
                            'name' => $item->user->name,
                            'email' => $item->user->email,
                            'role' => $item->user->role,
                            'devisi' => $item->user->devisi,
                        ] : null,
                    ];
                }),
            ]);
        } catch (\Exception $e) {
            // Return error details if something goes wrong
            return response()->json([
                'success' => false,
                'message' => 'Something went wrong while fetching the data.',
                'error' => $e->getMessage(),
            ], 500);
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

            // Update the status of the pengajuan to 'approval'
            $pengajuan = Pengajuan::find($request->pengujian_id);
            if ($pengajuan) {
                $pengajuan->status = 'approval'; // Change status to 'approval'
                $pengajuan->save(); // Save the changes
            }

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

             // Cek apakah ada yang menolak
            if ($this->checkIfRejected($persetujuan)) {
                return response()->json(
                    [
                        'success' => true,
                        'message' => 'Persetujuan ditolak',
                        'data' => $persetujuan,
                    ],
                    400
                );
            }

            // Cek apakah semua user sudah menyetujui dan telah mengirimkan signature
            if ($this->checkIfAllApproved($persetujuan)) {
                $persetujuan->update(['status' => 'approved']);
                Log::info('Updated status to approved: ' . $persetujuan->status);

                // Update status Pengajuan menjadi "finished"
                $pengajuan = $persetujuan->pengujian->pengembangan->pengajuan;
                if ($pengajuan) {
                    $pengajuan->update(['status' => 'finished']);
                }
                

                return response()->json(
                    [
                        'success' => true,
                        'message' => 'Persetujuan selesai',
                        'data' => $persetujuan,
                    ],
                    200
                );
            }

            // Check if all approvals are completed (all 4 approved)
            if ($this->checkIfAllApproved($persetujuan)) {
                return response()->json(
                    [
                        'success' => true,
                        'message' => 'Persetujuan selesai',
                        'data' => $persetujuan,
                    ],
                    200,
                );
            }

            return response()->json(
                [
                    'success' => true,
                    'message' => 'Persetujuan berhasil, Tunggu persetujuan dari user lainnya',
                    'data' => $persetujuan,$persetujuanDetail,
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
        // Hitung jumlah persetujuan yang memiliki status "setuju" dan memiliki signature
        $approvedCount = $persetujuan->details()->where('status', 'setuju')->whereNotNull('signature')->count();
        Log::info('Approved count: ' . $approvedCount);
        Log::info('Total Details Count: ' . $persetujuan->details()->count());
        Log::info('All Details:', $persetujuan->details()->get()->toArray());

        return $approvedCount === 4; // Pastikan ada 4 persetujuan lengkap

    }

    // Helper method to check if there is any rejection
    protected function checkIfRejected($persetujuan)
    {
        return $persetujuan->details()->where('status', 'pending')->exists();
    }

    // Handle image upload for signature
    protected function handleSignatureUpload(Request $request)
    {
        if ($request->hasFile('signature')) {
            $signature = $request->file('signature');
            $signaturePath = $signature->storeAs('signatures', $signature->getClientOriginalName(), 'public');
            return $signaturePath;
        }
        return null;
    }
}