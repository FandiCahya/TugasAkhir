<?php

namespace App\Observers;

use App\Models\PersetujuanPengujian;
use App\Models\PersetujuanPengujianModel;
use Illuminate\Support\Facades\Log;

class PersetujuanPengujianDetailObserver
{
    /**
     * Handle the PersetujuanPengujianDetail "updated" event.
     */
    public function updated(PersetujuanPengujianModel $detail): void
    {
        Log::info('Observer triggered: PersetujuanPengujianDetail updated', [
            'detail_id' => $detail->id,
            'status' => $detail->status
        ]);

        $persetujuan = $detail->persetujuanPengujian;

        if (!$persetujuan) {
            Log::error('Observer error: PersetujuanPengujian not found for detail ID: ' . $detail->id);
            return;
        }

        // Cek apakah semua approval "setuju"
        $allApproved = $persetujuan->details()->where('status', 'setuju')->count() === 4;

        // Cek apakah ada yang "tidak_setuju"
        $hasRejection = $persetujuan->details()->where('status', 'tidak_setuju')->exists();

        if ($allApproved) {
            Log::info('All approvals setuju, updating status to approved', [
                'persetujuan_id' => $persetujuan->id
            ]);
            $persetujuan->update(['status' => 'approved']);
        } elseif ($hasRejection) {
            Log::info('Some approvals tidak setuju, updating status to rejected', [
                'persetujuan_id' => $persetujuan->id
            ]);
            $persetujuan->update(['status' => 'rejected']);
        } else {
            Log::info('Approvals still pending', [
                'persetujuan_id' => $persetujuan->id
            ]);
            $persetujuan->update(['status' => 'pending']);
        }
    }
}
