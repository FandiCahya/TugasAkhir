<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Concerns\HasUuids;

class Pengajuan extends Model
{
    use HasFactory, HasUuids;

    protected $table = 'pengajuan';
    protected $primaryKey = 'id';
    public $incrementing = false;
    protected $keyType = 'string';

    protected $fillable = [
        'id',
        'user_id',
        'tgl',
        'nama_sistem',
        'jenis',
        'rencana_anggaran',
        'masalah',
        'output',
        'tanda_tangan',
        'alasan_penolakan',
        'status',
        'signature'
    ];

    protected $casts = [
        'tgl' => 'date',
        'jenis' => 'string',
        'rencana_anggaran' => 'string',
        'status' => 'string',
    ];

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class, 'user_id');
    }
}
