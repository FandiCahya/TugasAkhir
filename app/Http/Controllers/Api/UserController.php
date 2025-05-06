<?php

namespace App\Http\Controllers\Api; // Tambahkan Api di namespace

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\QueryException;
use App\Http\Resources\GlobalResource;

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
            return new GlobalResource(true, 'Berhasil mengambil data Users', $users);
        } catch (\Exception $e) {
            // Menangani error lainnya
            return new GlobalResource(false, 'Terjadi kesalahan saat mengambil data pengguna. ', []);
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

            return new GlobalResource(true, 'Data pengguna berhasil ditambahkan.', $user);
        } catch (ValidationException $e) {
            return new GlobalResource(false, 'Validasi gagal. Silakan periksa kembali data yang dikirim.', []);
        } catch (\Exception $e) {
            return new GlobalResource(false, 'Terjadi kesalahan saat menambahkan data pengguna.', []);
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

            $data = $request->only(['name', 'email', 'devisi', 'foto_profile', 'role']);
            if ($request->filled('password')) {
                $data['password'] = bcrypt($request->password);
            }

            $user->update($data);

            return new GlobalResource(true, 'Data pengguna berhasil diperbarui.', $user);
        } catch (ValidationException $e) {
            return new GlobalResource(false, 'Validasi gagal. Silakan periksa kembali data yang dikirim.', []);
        } catch (\Exception $e) {
            return new GlobalResource(false, 'Terjadi kesalahan saat memperbarui data pengguna.', []);
        }
    }

    public function destroy($id)
    {
        try {
            $user = User::find($id);
            if ($user) {
                $user->delete();
                return new GlobalResource(true, 'Data pengguna berhasil dihapus.', []);
            } else {
                return new GlobalResource(false, 'Data pengguna tidak ditemukan.', []);
            }
        } catch (\Exception $e) {
            return new GlobalResource(false, 'Terjadi kesalahan saat menghapus data pengguna.', []);
        }
    }
}
