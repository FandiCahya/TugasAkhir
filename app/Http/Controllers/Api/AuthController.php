<?php

namespace App\Http\Controllers\Api;

use App\Models\User;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Exception;
use Carbon\Carbon;
use Illuminate\Support\Facades\Auth;

class AuthController extends Controller
{
    // Register user
    public function register(Request $request)
    {
        try {
            // Validasi input
            $validator = Validator::make($request->all(), [
                'name' => 'required|string|max:255',
                'email' => 'required|string|email|max:255|unique:users',
                'password' => 'required|string|min:6|confirmed',
            ]);

            // Jika validasi gagal, kembalikan error
            if ($validator->fails()) {
                return response()->json(['message' => 'Validation failed', 'errors' => $validator->errors()], 400);
            }

            // Buat pengguna baru
            $user = User::create([
                'name' => $request->name,
                'email' => $request->email,
                'password' => Hash::make($request->password),
            ]);

            $token = $user->createToken('auth_token')->plainTextToken;

            // Response sukses dengan data pengguna
            return response()->json(
                [
                    'message' => 'User created successfully.',
                    'user' => $user,
                    'access_token' => $token,
                    'token_type' => 'Bearer'
                ],
                201,
            );
        } catch (Exception $e) {
            // Tangani exception dan kirimkan pesan error
            return response()->json(
                [
                    'message' => 'Something went wrong during registration.',
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }

    // Login user

    public function login(Request $request)
    {
        try {
            // Validasi input
            $validator = Validator::make($request->all(), [
                'email' => 'required',
                'password' => 'required',
            ]);

            if (! Auth::attempt($request->only('email', 'password'))) {
                return response()->json([
                    'message' => 'Unauthorized'
                ], 401);
            }

            // Jika validasi gagal, kembalikan error
            if ($validator->fails()) {
                return response()->json(['message' => 'Validation failed', 'errors' => $validator->errors()], 400);
            }

            // Cari user berdasarkan email
            $user = User::where('email', $request->email)->firstOrFail();

            // Periksa apakah user ada dan password cocok
            if (!$user || !Hash::check($request->password, $user->password)) {
                return response()->json(['message' => 'Invalid credentials'], 401);
            }

            // Buat token untuk user
            $token = $user->createToken('auth_token')->plainTextToken;

            // Menambahkan waktu kadaluarsa pada token (Jika diinginkan)
            // $expiresAt = Carbon::now()->addMinutes(60); // Token kedaluwarsa dalam 1 jam

            // Response dengan token dan data pengguna
            return response()->json([
                'message' => 'Login successful',
                'token' => $token,// Sertakan waktu kadaluarsa dalam respons
                'token_type' => 'Bearer',
                'user' => $user,
            ]);
        } catch (Exception $e) {
            // Tangani exception dan kirimkan pesan error
            return response()->json(
                [
                    'message' => 'Something went wrong during login.',
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }

    public function logout(Request $request)
    {
        try {
            // Hapus token yang sedang digunakan
            $request->user()->tokens->each(function ($token) {
                $token->delete();
            });

            // Response sukses
            return response()->json(['message' => 'Logged out successfully.']);
        } catch (Exception $e) {
            // Tangani exception dan kirimkan pesan error
            return response()->json(
                [
                    'message' => 'Something went wrong during logout.',
                    'error' => $e->getMessage(),
                ],
                500,
            );
        }
    }
}
