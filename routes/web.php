<?php

use App\Http\Controllers\DashboardController;
use App\Http\Controllers\ProfileController;
use Illuminate\Support\Facades\Route;
// use App\Http\Controllers\Auth\AuthenticatedSessionController;
use App\Http\Controllers\Api\AuthController;

Route::middleware('guest')->get('/login', function () {
  return view('auth/login');
})->name('login');
Route::middleware('guest')->get('/', function () {
  return view('auth/login');
});
Route::view('/register', 'register')->name('register');
  // Jika Anda ingin halaman register
Route::middleware('auth:sanctum')->post('logout', [AuthController::class, 'logout']);

// Route::get('/dashboard', [DashboardController::class, 'index']);
Route::get('/dashboard',function (){return view('dashboard');
})->name(name: 'dashboard');

Route::get('/dashboard', [DashboardController::class, 'dashboard'])->name('dashboard');

Route::get('/users',function (){return view('users');
})->name(name: 'users');

Route::get('/pengajuan',action: function (){return view('submission');
})->name(name: 'pengajuan');

Route::get(uri: '/pengembangan',action: function (){return view('pengembangan');
})->name(name: 'pengembangan');

Route::get('/pengujian',action: function (){return view('pengujian');
})->name(name: 'pengujian');

Route::get('/pengujian-detail',action: function (){return view('detail-pengujian');
})->name(name: 'pengujian-detail');

Route::get('/pengujian-catatan',action: function (){return view('catatan-pengujian');
})->name(name: 'pengujian-catatan');

Route::get('/approval',action: function (){return view('approval');
})->name(name: 'approval');

Route::get('/history',action: function (){return view('history');
})->name(name: 'history');

Route::get('/laporan',action: function (){return view('laporan');
})->name(name: 'laporan');

Route::middleware('auth')->group(function () {
  Route::get('/profile', [ProfileController::class, 'edit'])->name('profile.edit');
  Route::patch('/profile', [ProfileController::class, 'update'])->name('profile.update');
  Route::delete('/profile', [ProfileController::class, 'destroy'])->name('profile.destroy');
});