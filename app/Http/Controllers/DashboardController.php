<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use App\Models\Pengajuan;
use App\Models\Pengembangan;
use App\Models\Pengujian;
use App\Models\PengujianDetail;
use App\Models\CatatanPengujian;
use App\Models\PersetujuanPengujian;


class DashboardController extends Controller
{
    public function dashboard()
    {
        $counts = [
            'users' => User::count(),
            'pengajuan' => Pengajuan::count(),
            'pengembangan' => Pengembangan::count(),
            'pengujian' => Pengujian::count(),
            'pengujian_detail' => PengujianDetail::count(),
            'catatan_pengujian' => CatatanPengujian::count(),
            'persetujuan_pengujian' => PersetujuanPengujian::count(),
        ];

        return view('dashboard', compact('counts'));
    }
}
