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
    protected $fillable = ['id', 'pengujian_id','status'];

    public function details()
    {
        return $this->hasMany(PersetujuanPengujianModel::class, 'persetujuan_pengujian_id');
    }

    public function persetujuan_detail()
    {
        return $this->hasMany(PersetujuanPengujianModel::class, 'persetujuan_pengujian_id');
    }

    public function pengujian()
    {
        return $this->belongsTo(Pengujian::class, 'pengujian_id', 'id'); // Ensure correct foreign key is being used
    }

    // Check if all approvers have approved
    public function isApproved()
    {
        $approvedCount = $this->details()->where('status', 'setuju')->count();
        return $approvedCount == 4; // All 4 users need to approve
    }

    // Method to check if any rejection exists
    public function isRejected(): bool
    {
        $details = $this->details;
        $rejectedCount = $details->where('status', 'tidak_setuju')->count();
        return $rejectedCount > 0; // If any user rejects, the approval is rejected
    }

    public function assignApprovalUsers(array $userIds)
    {
        // Ensure we have 4 users in the provided array
        if (count($userIds) !== 4) {
            throw new \Exception('You must provide exactly 4 users for approval');
        }

        $roles = ['user', 'admin', 'mqr', 'kepala cabang'];

        foreach ($userIds as $index => $userId) {
            $user = User::find($userId); // Find user by ID

            // Ensure the user exists
            if (!$user) {
                throw new \Exception('User with ID ' . $userId . ' not found.');
            }

            // Create an approval detail for this user
            PersetujuanPengujianModel::create([
                'persetujuan_pengujian_id' => $this->id,
                'user_id' => $user->id,
                'status' => 'tidak_setuju',
                'catatan' => null,
                'signature' => null,
            ]);
        }
    }
    public function createPengujianDetail(array $userIds,$pengujian_id)
    {
        // Ensure we have 4 users in the provided array
        if (count($userIds) !== 4) {
            throw new \Exception('You must provide exactly 4 users for approval');
        }

        $roles = ['user', 'admin', 'qmr', 'kepala cabang'];

        foreach ($userIds as $index => $userId) {
            $user = User::find($userId); // Find user by ID

            // Ensure the user exists
            if (!$user) {
                throw new \Exception('User with ID ' . $userId . ' not found.');
            }

            // Create an approval detail for this user
            PersetujuanPengujianModel::create([
                'persetujuan_pengujian_id' => $pengujian_id,
                'user_id' => $user->id,
                'status' => 'tidak_setuju',
                'catatan' => null,
                'signature' => null,
                'role' => $roles[$index] ?? 'user',
            ]);
        }
    }
}
