<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Pengajuan;
use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;
use Illuminate\Support\Facades\Auth;
use Illuminate\Database\QueryException;
use Illuminate\Support\Str;

class PengajuanController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Request $request)
    {
        try {
            $keyword = $request->query->get('keyword');
            $status = $request->query->get('status');
            $pengajuanQuery = Pengajuan::with('user'); // Relasi dengan user

            if ($keyword) {
                $pengajuanQuery->where(function ($query) use ($keyword) {
                    $query->where('nama_sistem', 'like', '%' . $keyword . '%')
                        ->orWhere('jenis', 'like', '%' . $keyword . '%')
                        ->orWhere('output', 'like', '%' . $keyword . '%');
                });
            }
    
            // Jika ada status, filter berdasarkan status
            if ($status) {
                $pengajuanQuery->where('status', 'like', '%' . $status . '%');
            }

        // Ambil data pengajuan setelah diterapkan filter jika ada
        $pengajuan = $pengajuanQuery->get();

            // $users = Pengajuan::all();
            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengajuan->map(function ($item) {
                        return [
                            'id' => $item->id,
                            'tgl' => $item->tgl,
                            'nama_sistem' => $item->nama_sistem,
                            'jenis' => $item->jenis,
                            'rencana_anggaran' => $item->rencana_anggaran,
                            'masalah' => $item->masalah,
                            'output' => $item->output,
                            'tanda_tangan' => $item->tangan_tangan,
                            'alasan_penolakan' => $item->alasan_penolakan,
                            'status' => $item->status,
                            'updated_at' => $item->updated_at,
                            'created_at' => $item->created_at,
                            'user' => [
                                'id' => $item->user->id ?? null,
                                'name' => $item->user->name ?? null,
                                'email' => $item->user->email ?? null,
                                'role' => $item->user->role ?? null,
                                'devisi' => $item->user->devisi ?? null,
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
            $user_id = Auth::id() ?? '9e2d47a4-c50f-4675-b26e-5b4008658da9'; // Cek apakah user_id terdeteksi
            if (!$user_id) {
                return response()->json(
                    [
                        'success' => false,
                        'error' => [
                            'code' => 401,
                            'message' => 'Unauthorized: User is not authenticated',
                        ],
                    ],
                    401,
                );
            }

            $request->validate([
                'tgl' => 'required|date',
                'nama_sistem' => 'required|string|max:255',
                'jenis' => 'required|in:sistem_baru,pengembangan',
                'rencana_anggaran' => 'required|in:termasuk_dalam_perencanaan,tidak_termasuk_perencanaan',
                'masalah' => 'required|string',
                'output' => 'required|string',
                'status' => 'in:pending,accepted,rejected',
            ]);

            $usulan = Pengajuan::create([
                'id' => Str::uuid(),
                'user_id' => $user_id,
                'tgl' => $request->tgl,
                'nama_sistem' => $request->nama_sistem,
                'jenis' => $request->jenis,
                'rencana_anggaran' => $request->rencana_anggaran,
                'masalah' => $request->masalah,
                'output' => $request->output,
                'status' => $request->status ?? 'pending',
            ]);
            return response()->json(
                [
                    'success' => true,
                    'payload' => $usulan,
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
            // Jika ada masalah query database (misal: masalah foreign key)
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => 400,
                        'message' => 'Database query error',
                        'details' => $e->getMessage(), // Menampilkan pesan error database
                    ],
                ],
                400,
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
     * Display the specified resource.
     */
    public function show($id)
    {
        $usulan = Pengajuan::where('id', $id)->where('user_id', Auth::id())->firstOrFail();
        return response()->json($usulan);
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Pengajuan $pengajuan) {}

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        try {
            $usulan = Pengajuan::findOrFail($id);
            $request->validate([
                'tgl' => 'date',
                'nama_sistem' => 'string|max:255',
                'jenis' => 'in:sistem_baru,pengembangan',
                'rencana_anggaran' => 'in:termasuk_dalam_perencanaan,tidak_termasuk_perencanaan',
                'masalah' => 'string',
                'output' => 'string',
                'tanda_tangan' => 'string|max:255',
                'alasan_penolakan' => 'string|max:255',
                'status' => 'in:pending,submitted,accepted',
            ]);

            $usulan->update($request->all());

            return response()->json(
                [
                    'success' => true,
                    'payload' => $usulan,
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
            // Jika ada masalah query database (misal: masalah foreign key)
            return response()->json(
                [
                    'success' => false,
                    'payload' => [],
                    'error' => [
                        'code' => 400,
                        'message' => 'Database query error',
                        'details' => $e->getMessage(), // Menampilkan pesan error database
                    ],
                ],
                400,
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
     * Remove the specified resource from storage.
     */
    public function destroy($id)
    {
        try{
            $pengajuan = Pengajuan::findOrFail($id);
            $pengajuan->delete();
            return response()->json(
                [
                    'success' => true,
                    'payload' => [
                        'code' => '200',
                        'message' => 'Delete Pengajuan Successfully',
                    ],
                ],
                200,
            );
        }catch (\Exception $e) {
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
}
