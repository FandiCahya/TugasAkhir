<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Concerns\HasUuids;

class Pengujian extends Model
{
    use HasFactory, HasUuids;
    protected $table = 'pengujian_perangkat_lunak';
    protected $fillable = ['id', 'pengembangan_id', 'perangkat_lunak', 'versi', 'tujuan', 'metode', 'tanggal', 'pelaksana_id'];

    public function details()
    {
        return $this->hasMany(PengujianDetail::class, 'pengujian_id');
    }

    public function persetujuan()
    {
        return $this->hasMany(PersetujuanPengujian::class, 'pengujian_id'); // Correct foreign key logic
    }

    public function pelaksana()
    {
        return $this->belongsTo(User::class,  'pelaksana_id'); // Correct foreign key logic
    }

    public function pengembangan()
    {
        return $this->belongsTo(Pengembangan::class);
    }    

    public function user()
    {
        return $this->belongsTo(User::class);
    }
    public function pengajuan()
    {
        return $this->belongsTo(Pengajuan::class);
    }
    public function catatan()
    {
        return $this->hasMany(CatatanPengujian::class, 'pengujian_id');
    }

    public function createPengujianDetail(array $userIds,$pengujian_id)
    {
        // Ensure we have 4 users in the provided array
        if (count($userIds) !== 4) {
            throw new \Exception('You must provide exactly 4 users for approval');
        }

        $roles = ['user', 'admin', 'mqr', 'kepala cabang'];

        foreach ($userIds as $index => $userId) {
            $user = User::find($userId); // Find user by ID

            // Ensure the user exists
            if (!$user) {
                throw new \Exception('User with ID ' . $userId . ' not found.');
            }

            // Create an approval detail for this user
            PersetujuanPengujianDetail::create([
                'persetujuan_pengujian_id' => $pengujian_id,
                'user_id' => $user->id,
                'status' => 'tidak_setuju',
                'catatan' => null,
                'signature' => null,
                'role' => $roles[$index] ?? 'user',
            ]);
        }
    }
}
