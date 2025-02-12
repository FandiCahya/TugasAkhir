<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Concerns\HasUuids;

class Pengembangan extends Model
{
    use HasFactory, HasUuids;
    protected $table = 'pengembangan';
    protected $primaryKey = 'id';
    public $incrementing = false;
    protected $keyType = 'string';

    protected $fillable = [
        'id',
        'user_id', 
        'pengajuan_id', 
        'tanggal_mulai', 
        'tanggal_selesai', 
        'tahap', 
        'persentase', 
        'keterangan', 
        'status'
    ];

    // Menentukan relasi dengan model Pengajuan (relasi many-to-one)
    public function pengajuan()
    {
        return $this->belongsTo(Pengajuan::class, 'pengajuan_id');
    }

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class, 'user_id');
    }
}
