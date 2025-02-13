<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('persetujuan_pengujian', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->uuid('pengujian_id');
            $table->enum('status', ['pending', 'approved', 'rejected'])->default('pending');
            $table->timestamps();

            $table->foreign('pengujian_id')->references('id')->on('pengujian_perangkat_lunak')->onDelete('cascade');
        });

        Schema::create('persetujuan_pengujian_details', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->uuid( 'persetujuan_pengujian_id');
            $table->uuid('user_id');
            $table->enum('status', ['setuju', 'tidak_setuju']);
            $table->text('catatan')->nullable();
            $table->string('signature')->nullable();
            $table->enum('role', ['user', 'admin', 'mqr', 'kepalacabang'])->default('user');
            $table->timestamps();

            // Foreign Keys
            $table->foreign(columns: 'persetujuan_pengujian_id')->references('id')->on(table: 'persetujuan_pengujian')->onDelete('cascade');
            $table->foreign('user_id')->references('id')->on('users')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('persetujuan_pengujian');
        Schema::dropIfExists('persetujuan_pengujian_details');
    }
};
