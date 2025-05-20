<?php

namespace Database\Seeders;

use App\Models\User;
// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        // User::factory(10)->create();

        // Jalankan FormulirUsulanSeeder
        $this->call(UserSeeder::class);
        $this->call(PengajuanSeeder::class);
        // $this->call(PengembanganSeeder::class);
        // $this->call(PengujianSeeder::class);
    }
}
