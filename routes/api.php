<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\UserController;
use App\Http\Controllers\Api\PengajuanController;
use App\Http\Controllers\Api\PengembanganController;

// Route::get('/user', function (Request $request) {
//     return $request->user();
// })->middleware('auth:sanctum');



Route::resource('users', UserController::class);
Route::resource('pengajuan', PengajuanController::class);
Route::resource('pengembangan', PengembanganController::class);
