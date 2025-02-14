<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Concerns\HasUuids;

class Pengujian extends Model
{
    use HasFactory, HasUuids;
    protected $table = 'pengujian_perangkat_lunak';
    protected $fillable = ['id','pengembangan_id','perangkat_lunak', 'versi', 'tujuan', 'metode', 'tanggal', 'pelaksana_id'];

    public function details()
    {
        return $this->hasMany(PengujianDetail::class, 'pengujian_id');
    }

    public function persetujuan()
    {
        return $this->hasOne(PersetujuanPengujian::class, 'pengujian_id');
    }

    public function pengembangan()
    {
        return $this->belongsTo(Pengembangan::class, 'pengembangan_id');
    }

    public function user()
    {
        return $this->belongsTo(User::class, 'penanggung_jawab_id');
    }
}


