<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\PengujianDetail;
use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\QueryException;

class PengujianDetailController extends Controller
{
    public function index(Request $request)
    {
        try {
            $data = PengujianDetail::all(); // Retrieve all pengujian details
            $keyword = $request->query->get('keyword');
            return response()->json(
                [
                    'success' => true,
                    'message' => 'Berhasil mendapatkan data pengujian detail',
                    'payload' => $data->map(function ($item) {
                        return [
                            'id' => $item->id,
                            'nama_uji' => $item->nama_uji,
                            'kasus_uji' => $item->kasus_uji,
                            'hasil_diharapkan' => $item->hasil_diharapkan,
                            'hasil_pengujian' => $item->hasil_pengujian,
                            'status' => $item->status,
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
                'nama_uji' => 'required|string|max:255',
                'kasus_uji' => 'required|string|max:255',
                'hasil_diharapkan' => 'required|string',
                'hasil_pengujian' => 'nullable|string',
                'status' => 'required|string|max:50',
            ]);

            // Create the pengujian detail
            $pengujianDetail = PengujianDetail::create($validated);

            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengujianDetail,
                    'message' => 'Berhasil menambahkan data pengujian detail',
                ],
                201,
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
        }
    }

    public function show(string $id)
    {
        try {
            $pengujianDetail = PengujianDetail::findOrFail($id);

            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengujianDetail,
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
            // Find the pengujian detail
            $pengujianDetail = PengujianDetail::findOrFail($id);

            // Validate incoming data
            $request->validate([
                'nama_uji' => 'string',
                'kasus_uji' => 'string',
                'hasil_diharapkan' => 'string',
                'hasil_pengujian' => 'string',
                'status' => 'string',
            ]);

            // Update the pengujian detail
            $pengujianDetail->update($request->all());

            return response()->json(
                [
                    'success' => true,
                    'payload' => $pengujianDetail,
                    'message' => 'Berhasil memperbarui data pengujian detail',
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
        }catch (ValidationException $e) {
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
        }
    }

    public function destroy(string $id)
    {
        try {
            $pengujianDetail = PengujianDetail::findOrFail($id);
            $pengujianDetail->delete();

            return response()->json(
                [
                    'success' => true,
                    'payload' => "Delete Pengujian Detail Successfully",
                    'message' => 'Berhasil menghapus data pengujian detail',
                ],
                204,
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
}
