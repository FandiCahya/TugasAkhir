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

        $user = User::first();
        $pengembangan = Pengembangan::first();

        if (!$user) {
            $this->command->info('Seeder Persetujuan Pengujian membutuhkan setidaknya satu user di database.');
            return;
        }
        // Insert Pengujian Perangkat Lunak
        $pengujianId = Str::uuid();
        $pengujianDetailId = Str::uuid();
        $persetujuanPengujianId = Str::uuid();

        DB::table('pengujian_perangkat_lunak')->insert([
            'id' => $pengujianId,
            'pengembangan_id' => $pengembangan?->id,
            'perangkat_lunak' => 'CCBS',
            'versi' => '1.0.0',
            'tujuan' => 'Menambahkan tool pencarian berdasarkan identitas',
            'metode' => 'Test Form Pencarian',
            'tanggal' => Carbon::now()->toDateString(),
            'pelaksana_id' => $user?->id,
            'created_at' => Carbon::now(),
            'updated_at' => Carbon::now(),
        ]);

        // Tambahkan data ke tabel pengujian_detail
        DB::table('pengujian_detail')->insert([
            'id' => $pengujianDetailId,
            'pengujian_id' => $pengujianId,
            'nama_uji' => 'Search Identitas',
            'kasus_uji' => 'Pencarian berdasarkan identitas',
            'hasil_diharapkan' => 'Hanya identitas yang dicari muncul dalam pencarian',
            'hasil_pengujian' => 'Sesuai',
            'status' => 'OK',
            'created_at' => now(),
            'updated_at' => now(),
        ]);

        // Tambahkan data ke tabel catatan_pengujian
        DB::table('catatan_pengujian')->insert([
            'id' => Str::uuid(),
            'pengujian_id' => $pengujianId,
            'uraian' => 'Tidak ada kendala yang ditemukan',
            'rencana_tindak_lanjut' => null,
            'penanggung_jawab_id' => $user?->id,
            'created_at' => now(),
            'updated_at' => now(),
        ]);

        // Tambahkan data ke tabel persetujuan_pengujian
        DB::table('persetujuan_pengujian')->insert([
            'id' => $persetujuanPengujianId,
            'pengujian_id' => $pengujianId,
            'created_at' => now(),
            'updated_at' => now(),
        ]);

        // Role yang harus memberikan persetujuan
        $roles = ['user', 'admin', 'mqr', 'kepalacabang'];

        // Loop untuk insert data dengan masing-masing role
        foreach ($roles as $role) {
            DB::table('persetujuan_pengujian_details')->insert([
                'id' => Str::uuid(),
                'persetujuan_pengujian_id' => $persetujuanPengujianId,
                'user_id' => $user?->id,
                'status' => 'setuju',
                'catatan' => 'Disetujui oleh ' . ucfirst($role),
                'signature' => 'signature_approval/' . $role . '.png',
                'role' => $role,
                'created_at' => now(),
                'updated_at' => now(),
            ]);
        }
    }
}
