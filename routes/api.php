<?php

use App\Http\Controllers\Api\PersetujuanPengujianController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\UserController;
use App\Http\Controllers\Api\PengajuanController;
use App\Http\Controllers\Api\PengembanganController;
use App\Http\Controllers\Api\PengujianController;
use App\Http\Controllers\Api\PengujianDetailController;
use App\Http\Controllers\Api\CatatanPengujianController;
use App\Http\Controllers\Api\AuthController;

// Route::get('/user', function (Request $request) {
//     return $request->user();
// })->middleware('auth:sanctum');


Route::post('login', [AuthController::class, 'login']);
Route::post('register', [AuthController::class, 'register']);
// Logout route - pastikan user terautentikasi
Route::middleware('auth:sanctum')->post('logout', [AuthController::class, 'logout']);

Route::resource('users', UserController::class);
Route::resource('pengajuan', PengajuanController::class);
Route::resource('pengembangan', PengembanganController::class);
Route::resource('pengujian',PengujianController::class);
Route::resource('pengujian-detail',PengujianDetailController::class);
Route::resource('catatan-pengujian',CatatanPengujianController::class);

Route::get( 'approval', [PersetujuanPengujianController::class, 'showall']);

Route::post('persetujuan-pengujian/create', [PersetujuanPengujianController::class, 'createApproval']);

Route::post('persetujuan-pengujian-detail/{id}/approval', [PersetujuanPengujianController::class, 'approval']);

Route::post('pengujian-detail/{id}', [PersetujuanPengujianController::class, 'update']);


