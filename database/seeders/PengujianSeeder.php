<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;
use Carbon\Carbon;
use App\Models\Pengembangan;
use App\Models\User;

class PengujianSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $pengembangan = Pengembangan::first();

        if (!$pengembangan) {
            $this->command->info('Seeder FormulirUsulan membutuhkan setidaknya satu user di database.');
            return;
        }

        DB::table('pengujian')->insert([
            'id' => Str::uuid(),
            'pengembangan_id' => $pengembangan->id,
            'hasil' => ['positif', 'negatif'][rand(0, 1)], // Random hasil
            'catatan' => 'Catatan pengujian contoh.',
            'tester_id' => $pengembangan->pengajuan->user->id,
            'created_at' => Carbon::now(),
            'updated_at' => Carbon::now(),
        ]);
    }
}
