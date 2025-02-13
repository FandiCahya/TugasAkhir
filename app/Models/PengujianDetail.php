<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Concerns\HasUuids;

class PengujianDetail extends Model
{
    use HasFactory, HasUuids;

    public $incrementing = false;
    protected $keyType = 'string';
    protected $table = 'pengujian_detail';
    protected $fillable = ['id','pengujian_id', 'nama_uji', 'kasus_uji', 'hasil_diharapkan', 'hasil_pengujian', 'status'];

    public function pengujian()
    {
        return $this->belongsTo(Pengujian::class, 'pengujian_id');
    }
}
