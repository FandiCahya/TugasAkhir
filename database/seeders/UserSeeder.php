<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Str;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        DB::table('users')->insert([
            'id' => (string) Str::uuid(),  // UUID untuk id
            'name' => 'Admin',
            'email' => 'admin@gmail.com',
            'email_verified_at' => now(),
            'password' => Hash::make(value: 'admin123'),  // Password yang di-hash
            'devisi' => 'IT',  // Devise Admin // Ganti dengan path foto yang sesuai
            'role' => 'admin',  // Role admin
            'created_at' => now(),
            'updated_at' => now(),
        ]);
        DB::table('users')->insert([
            'id' => (string) Str::uuid(),  // UUID untuk id
            'name' => 'user',
            'email' => 'user@gmail.com',
            'email_verified_at' => now(),
            'password' => Hash::make(value: 'user123'),  // Password yang di-hash
            'devisi' => 'IT',  // Devise Admin // Ganti dengan path foto yang sesuai
            'role' => 'user',  // Role admin
            'created_at' => now(),
            'updated_at' => now(),
        ]);
        DB::table('users')->insert([
            'id' => (string) Str::uuid(),  // UUID untuk id
            'name' => 'mqr',
            'email' => 'mqr@gmail.com',
            'email_verified_at' => now(),
            'password' => Hash::make(value: 'mqr123'),  // Password yang di-hash
            'devisi' => 'IT',  // Devise Admin // Ganti dengan path foto yang sesuai
            'role' => 'mqr',  // Role admin
            'created_at' => now(),
            'updated_at' => now(),
        ]);
        DB::table('users')->insert([
            'id' => (string) Str::uuid(),  // UUID untuk id
            'name' => 'kacap',
            'email' => 'kacap@gmail.com',
            'email_verified_at' => now(),
            'password' => Hash::make(value: 'kacap123'),  // Password yang di-hash
            'devisi' => 'IT',  // Devise Admin // Ganti dengan path foto yang sesuai
            'role' => 'kepalacabang',  // Role admin
            'created_at' => now(),
            'updated_at' => now(),
        ]);
    }
}
