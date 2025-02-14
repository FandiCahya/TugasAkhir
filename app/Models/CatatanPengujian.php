<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Concerns\HasUuids;

class CatatanPengujian extends Model
{
    use HasFactory, HasUuids;

    public $incrementing = false;
    protected $keyType = 'string';
    protected $table = 'catatan_pengujian';
    protected $fillable = ['id','pengujian_id', 'uraian', 'rencana_tindak_lanjut', 'penanggung_jawab_id'];

    public function pengujian()
    {
        return $this->belongsTo(Pengujian::class, 'pengujian_id');
    }

    public function user()
    {
        return $this->belongsTo(User::class, foreignKey: 'penanggung_jawab_id');
    }
}
