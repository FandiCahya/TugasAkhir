<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;
use App\Models\Pengajuan;
use App\Models\User;

class PengembanganSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Mendapatkan pengajuan_id
        $pengajuanId = Pengajuan::where('nama_sistem', 'Sistem Monitoring IoT')->first()->id;

        $user = User::first();

        if (!$user) {
            $this->command->info('Seeder FormulirUsulan membutuhkan setidaknya satu user di database.');
            return;
        }

        DB::table('pengembangan')->insert([
            'id' => (string) Str::uuid(),  // UUID untuk id
            'user_id' => $user->id,
            'pengajuan_id' => $pengajuanId,  // Ganti dengan UUID yang valid dari tabel pengajuan
            'tanggal_mulai' => '2025-02-01',  // Tanggal mulai pengembangan
            'tanggal_selesai' => '2025-02-28',  // Tanggal selesai pengembangan
            'tahap' => 'UI Design',  // Tahap pengembangan
            'persentase' => 50,  // Persentase perkembangan
            'keterangan' => 'Desain antarmuka pengguna sedang dikerjakan.',  // Keterangan
            'created_at' => now(),
            'updated_at' => now(),
        ]);

    }
}
