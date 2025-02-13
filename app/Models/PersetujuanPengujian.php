<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Concerns\HasUuids;

class PersetujuanPengujian extends Model
{
    use HasFactory, HasUuids;

    public $incrementing = false;
    protected $keyType = 'string';
    protected $table = 'persetujuan_pengujian';
    protected $fillable = ['id','pengujian_id'];

    public function details()
    {
        return $this->hasMany(PersetujuanPengujianDetail::class, 'persetujuan_pengujian_id');
    }
}

class PersetujuanPengujianDetail extends Model
{
    use HasFactory, HasUuids;

    public $incrementing = false;
    protected $keyType = 'string';
    protected $table = 'persetujuan_pengujian_details';
    protected $fillable = ['id','persetujuan_pengujian_id', 'user_id', 'status', 'catatan', 'signature'];

    public function persetujuan()
    {
        return $this->belongsTo(PersetujuanPengujian::class, 'persetujuan_pengujian_id');
    }
}
