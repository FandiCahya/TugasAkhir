<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
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
            $this->command->info('Seeder FormulirUsulan membutuhkan setidaknya satu user di database.');
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
                'masalah' => 'Diperlukan sistem monitoring berbasis IoT untuk efisiensi energi.',
                'output' => 'Sistem berbasis cloud yang menampilkan data secara real-time.',
                'status' => 'draft',
                'created_at' => Carbon::now()->subDays(5),
                'updated_at' => Carbon::now(),
            ]
        ]);
    }
}
