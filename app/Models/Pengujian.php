<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Concerns\HasUuids;

class Pengujian extends Model
{
    protected $table = 'pengujian';
    protected $primaryKey = 'id';
    public $incrementing = false;
    protected $keyType = 'string';

    protected $fillable = [
        'id',
        'pengembangan_id',
        'hasil',
        'catatan',
        'tester_id',
    ];

    // Relasi ke Pengembangan
    public function pengembangan(): BelongsTo
    {
        return $this->belongsTo(Pengembangan::class, 'pengembangan_id');
    }

    // Relasi ke User (tester)
    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class, 'tester_id');
    }
}
