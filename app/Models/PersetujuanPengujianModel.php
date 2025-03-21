<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Support\Facades\Log;

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

         protected static function boot()
    {
        parent::boot();

        static::updated(function ($detail) {
            Log::info('Event updated triggered for PersetujuanPengujianDetail', [
                'detail_id' => $detail->id,
                'status' => $detail->status
            ]);

            // Ambil persetujuan_pengujian yang terkait
            $persetujuan = $detail->persetujuanPengujian;

            if (!$persetujuan) {
                Log::error('Error: PersetujuanPengujian not found for detail ID: ' . $detail->id);
                return;
            }

            // Hitung jumlah yang sudah "setuju"
            $approvedCount = $persetujuan->details()->where('status', 'setuju')->count();
            $totalCount = $persetujuan->details()->count();

            Log::info("Approved count: $approvedCount, Total Details Count: $totalCount");

            if ($approvedCount === $totalCount) {
                Log::info('Semua telah menyetujui, update status ke approved', [
                    'persetujuan_id' => $persetujuan->id
                ]);
                $persetujuan->update(['status' => 'approved']);
            } else {
                Log::info('Belum semua menyetujui, status tetap pending');
            }
        });
    }
}
