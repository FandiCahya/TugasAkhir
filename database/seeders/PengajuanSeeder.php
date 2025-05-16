<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Str;
use App\Models\Pengajuan;
use App\Models\User;
use Carbon\Carbon;

class PengajuanSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $user = User::first();

        if (!$user) {
            $this->command->info('Seeder Pengajuan membutuhkan setidaknya satu user di database.');
            return;
        }

        Pengajuan::insert([
            [
                'id' => Str::uuid(),
                'user_id' => $user->id,
                'tgl' => Carbon::now()->subDays(5),
                'nama_sistem' => 'Sistem Monitoring IoT',
                'jenis' => 'sistem_baru',
                'rencana_anggaran' => 'termasuk_dalam_perencanaan',
                'masalah' => 'Energi terbuang karena tidak ada monitoring otomatis.',
                'output' => 'Dashboard real-time penggunaan listrik berbasis IoT.',
                'status' => 'pending',
                'created_at' => Carbon::now()->subDays(5),
                'updated_at' => Carbon::now(),
            ],
            [
                'id' => Str::uuid(),
                'user_id' => $user->id,
                'tgl' => Carbon::now()->subDays(4),
                'nama_sistem' => 'Aplikasi Inventaris Barang',
                'jenis' => 'pengembangan',
                'rencana_anggaran' => 'termasuk_dalam_perencanaan',
                'masalah' => 'Data barang sering tidak sinkron antar departemen.',
                'output' => 'Aplikasi terintegrasi untuk manajemen stok dan barang keluar/masuk.',
                'status' => 'rejected',
                'created_at' => Carbon::now()->subDays(4),
                'updated_at' => Carbon::now(),
            ],
            [
                'id' => Str::uuid(),
                'user_id' => $user->id,
                'tgl' => Carbon::now()->subDays(3),
                'nama_sistem' => 'Sistem Absensi Pegawai',
                'jenis' => 'sistem_baru',
                'rencana_anggaran' => 'termasuk_dalam_perencanaan',
                'masalah' => 'Absensi masih dilakukan secara manual dan rentan manipulasi.',
                'output' => 'Aplikasi absensi berbasis fingerprint dan face recognition.',
                'status' => 'accepted',
                'created_at' => Carbon::now()->subDays(3),
                'updated_at' => Carbon::now(),
            ]
        ]);
    }
}
