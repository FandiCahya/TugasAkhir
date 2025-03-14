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
        Schema::create('pengajuan', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->uuid('user_id');
            $table->date('tgl');
            $table->string('nama_sistem');
            $table->enum('jenis', ['sistem_baru', 'pengembangan']);
            $table->enum('rencana_anggaran', ['termasuk_dalam_perencanaan', 'tidak_termasuk_perencanaan']);
            $table->text('masalah');
            $table->text('output');
            $table->string('tanda_tangan')->nullable();
            $table->text('alasan_penolakan')->nullable();
            $table->enum('status', ['pending', 'accepted', 'rejected','developing','testing','approval','finished'])->default('pending');
            $table->timestamps();

            // Foreign key
            $table->foreign('user_id')->references('id')->on('users')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('pengajuan');
    }
};
