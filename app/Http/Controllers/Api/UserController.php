<?php

namespace App\Http\Controllers\Api; // Tambahkan Api di namespace

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\QueryException;

class UserController extends Controller
{
    public function index(Request $request)
    {
        try {
            $query = $request->input('search');
            $users = User::when($query, function ($queryBuilder) use ($query) {
                return $queryBuilder->where('name', 'like', '%' . $query . '%');
            })->get();

            $users = User::all();
            return response()->json(
                [
                    'success' => true,
                    'payload' => $users,
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

    public function store(Request $request)
    {
        try {
            $request->validate([
                'name' => 'required|string|max:255',
                'email' => 'required|string|email|max:255|unique:users',
                'password' => 'required|string|min:8',
                'devisi' => 'nullable',
                'foto_profile' => 'nullable|string',
                'role' => 'required|in:user,admin,qmr,kepalacabang',
            ]);

            $user = User::create([
                'name' => $request->name,
                'email' => $request->email,
                'password' => bcrypt($request->password),
                'devisi' => $request->devisi,
                'foto_profile' => $request->foto_profile,
                'role' => $request->role,
            ]);

            return response()->json(
                [
                    'success' => true,
                    'payload' => $user,
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

    public function update(Request $request, $id)
    {
        try {
            $user = User::findOrFail($id);

            $request->validate([
                'name' => 'string|max:255',
                'email' => 'string|email|max:255|unique:users,email,' . $id,
                'password' => 'nullable|string|min:8',
                'devisi' => 'nullable',
                'foto_profile' => 'nullable|string',
                'role' => 'in:user,admin,qmr,kepalacabang',
            ]);

            $user->update(array_filter($request->only(['name', 'email', 'password', 'devisi', 'foto_profile', 'role'])));

            return response()->json(
                [
                    'success' => true,
                    'payload' => $user,
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

    public function destroy($id)
    {
        try {
            $user = User::findOrFail($id);
            $user->delete();
            return response()->json(
                [
                    'success' => true,
                    'payload' => [],
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
}
