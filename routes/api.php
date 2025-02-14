<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\UserController;
use App\Http\Controllers\Api\PengajuanController;
use App\Http\Controllers\Api\PengembanganController;
use App\Http\Controllers\Api\PengujianController;
use App\Http\Controllers\Api\PengujianDetailController;
use App\Http\Controllers\Api\CatatanPengujianController;

// Route::get('/user', function (Request $request) {
//     return $request->user();
// })->middleware('auth:sanctum');



Route::resource('users', UserController::class);
Route::resource('pengajuan', PengajuanController::class);
Route::resource('pengembangan', PengembanganController::class);
Route::resource('pengujian',PengujianController::class);
Route::resource('pengujian-detail',PengujianDetailController::class);
Route::resource('catatan-pengujian',CatatanPengujianController::class);

