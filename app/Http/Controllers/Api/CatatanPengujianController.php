<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Models\CatatanPengujian;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\QueryException;

class CatatanPengujianController extends Controller
{
    public function index()
    {
        try {
            $data = CatatanPengujian::all(); // Retrieve all catatan pengujian

            return response()->json(
                [
                    'success' => true,
                    'message' => 'Berhasil mendapatkan data catatan pengujian',
                    'payload' => $data->map(function ($item) {
                        return [
                            'id' => $item->id,
                            'uraian' => $item->uraian,
                            'rencana_tindak_lanjut' => $item->rencana_tindak_lanjut,
                            'updated_at' => $item->updated_at,
                            'created_at' => $item->created_at,
                            'pengujian' => [
                                'id' => $item->pengujian->id,
                                'perangkat_lunak' => $item->pengujian->perangkat_lunak,
                                'versi' => $item->pengujian->versi,
                                'tujuan' => $item->pengujian->tujuan,
                                'metode' => $item->pengujian->metode,
                                'tanggal' => $item->pengujian->tanggal,
                                'created_at' => $item->pengujian->created_at,
                                'updated_at' => $item->pengujian->updated_at,
                            ],
                            'penanggung_jawab' => [
                                'id' => $item->user->id ?? null,
                                'name' => $item->user->name ?? null,
                                'email' => $item->user->email ?? null,
                            ],
                        ];
                    }),
                    'error' => null,
                ],
                200,
            );
        } catch (\Exception $e) {
            return response()->json(
                [
                    'success' => false,
                    'payload' => null,
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }

    public function store(Request $request)
    {
        try {
            // Validate incoming data
            $validated = $request->validate([
                'pengujian_id' => 'required|exists:pengujian_perangkat_lunak,id',
                'uraian' => 'required|string|max:255',
                'rencana_tindak_lanjut' => 'required|string|max:255',
                'penanggung_jawab_id' => 'nullable|exists:users,id',
            ]);

            // Create the catatan pengujian
            $catatanPengujian = CatatanPengujian::create($validated);

            return response()->json(
                [
                    'success' => true,
                    'payload' => $catatanPengujian,
                    'message' => 'Berhasil menambahkan catatan pengujian',
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
                    'payload' => null,
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
                    'payload' => null,
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }

    public function show($id)
    {
        try {
            $catatanPengujian = CatatanPengujian::findOrFail($id);

            return response()->json(
                [
                    'success' => true,
                    'payload' => $catatanPengujian->map(function ($item) {
                        return [
                            'id' => $item->id,
                            'uraian' => $item->uraian,
                            'rencana_tindak_lanjut' => $item->rencana_tindak_lanjut,
                            'updated_at' => $item->updated_at,
                            'created_at' => $item->created_at,
                            'pengujian' => [
                                'id' => $item->pengujian->id,
                                'perangkat_lunak' => $item->pengujian->perangkat_lunak,
                                'versi' => $item->pengujian->versi,
                                'tujuan' => $item->pengujian->tujuan,
                                'metode' => $item->pengujian->metode,
                                'tanggal' => $item->pengujian->tanggal,
                                'created_at' => $item->pengujian->created_at,
                                'updated_at' => $item->pengujian->updated_at,
                            ],
                            'penanggung_jawab' => [
                                'id' => $item->user->id ?? null,
                                'name' => $item->user->name ?? null,
                                'email' => $item->user->email ?? null,
                            ],
                        ];
                    }),
                    'error' => null,
                ],
                200,
            );
        } catch (\Exception $e) {
            return response()->json(
                [
                    'success' => false,
                    'payload' => null,
                    'error' => $e->getMessage(),
                ],
                404,
            );
        }
    }

    public function update(Request $request, $id)
    {
        try {
            // Find the catatan pengujian by ID
            $catatanPengujian = CatatanPengujian::findOrFail($id);

            // Validate incoming data
            $validated = $request->validate([
                'uraian' => 'required|string|max:255',
                'rencana_tindak_lanjut' => 'required|string|max:255',
            ]);

            // Update the catatan pengujian
            $catatanPengujian->update($validated);

            return response()->json(
                [
                    'success' => true,
                    'payload' => $catatanPengujian,
                    'message' => 'Berhasil memperbarui catatan pengujian',
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
        } catch (\Exception $e) {
            return response()->json(
                [
                    'success' => false,
                    'payload' => null,
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }

    public function destroy($id)
    {
        try {
            $catatanPengujian = CatatanPengujian::findOrFail($id);
            $catatanPengujian->delete();

            return response()->json(
                [
                    'success' => true,
                    'payload' => 'Catatan Pengujian Delete Successfully',
                    'message' => 'Berhasil menghapus catatan pengujian',
                ],
                200,
            ); // Change 204 to 200 to include a response body
        } catch (\Exception $e) {
            return response()->json(
                [
                    'success' => false,
                    'payload' => null,
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }
}
