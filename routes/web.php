<?php

use App\Http\Controllers\ProfileController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Auth\AuthenticatedSessionController;

Route::get('/', [AuthenticatedSessionController::class, 'create'])->name('login');


Route::middleware('auth')->group(function () {
    Route::get('/profile', [ProfileController::class, 'edit'])->name('profile.edit');
    Route::patch('/profile', [ProfileController::class, 'update'])->name('profile.update');
    Route::delete('/profile', [ProfileController::class, 'destroy'])->name('profile.destroy');
});

require __DIR__.'/auth.php';


Route::get('/dashboard', function () {
    return view('dashboard');
})->middleware(['auth', 'verified'])->name('dashboard');

Route::get('/users',function (){return view('users');
})->middleware(['auth', 'verified'])->name(name: 'users');

Route::get('/pengajuan',action: function (){return view('submission');
})->middleware(['auth', 'verified'])->name(name: 'pengajuan');

Route::get(uri: '/pengembangan',action: function (){return view('pengembangan');
})->middleware(['auth', 'verified'])->name(name: 'pengembangan');

Route::get('/pengujian',action: function (){return view('pengujian');
})->middleware(['auth', 'verified'])->name(name: 'pengujian');

Route::get('/approval',action: function (){return view('approval');
})->middleware(['auth', 'verified'])->name(name: 'approval');

Route::get('/history',action: function (){return view('history');
})->middleware(['auth', 'verified'])->name(name: 'history');

Route::get('/logs',action: function (){return view('logs');
})->middleware(['auth', 'verified'])->name(name: 'logs');
