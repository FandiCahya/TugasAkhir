<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Concerns\HasUuids;

class PersetujuanPengujianModel extends Model
{
    use HasFactory, HasUuids;

    public $incrementing = false;
    protected $keyType = 'string';
    protected $table = 'persetujuan_pengujian_details';
    protected $fillable = ['id', 'persetujuan_pengujian_id', 'user_id', 'status', 'catatan', 'signature'];

    public function persetujuanPengujian()
    {
        return $this->belongsTo(PersetujuanPengujian::class, 'persetujuan_pengujian_id');
    }

    public function user()
    {
        return $this->belongsTo(User::class, 'user_id'); // Replace with correct foreign key if different
    }
}
