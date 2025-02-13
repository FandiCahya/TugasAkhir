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
        Schema::create('pengujian_perangkat_lunak', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->uuid('pengembangan_id');
            $table->string('perangkat_lunak');
            $table->string('versi');
            $table->text('tujuan');
            $table->string('metode');
            $table->date('tanggal');
            $table->uuid('pelaksana_id');
            $table->timestamps();

            $table->foreign('pengembangan_id')->references('id')->on('pengembangan')->onDelete('cascade');
            $table->foreign( 'pelaksana_id')->references('id')->on('users')->onDelete('cascade');
        });

        Schema::create('pengujian_detail', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->uuid('pengujian_id');
            $table->string('nama_uji');
            $table->text('kasus_uji');
            $table->text('hasil_diharapkan');
            $table->text('hasil_pengujian');
            $table->enum('status', ['OK', 'Tidak']);
            $table->timestamps();

            $table->foreign('pengujian_id')->references('id')->on('pengujian_perangkat_lunak')->onDelete('cascade');
            
        });

        Schema::create('catatan_pengujian', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->uuid('pengujian_id');
            $table->text('uraian');
            $table->text('rencana_tindak_lanjut')->nullable();
            $table->uuid('penanggung_jawab_id');
            $table->timestamps();

            $table->foreign('pengujian_id')->references('id')->on('pengujian_perangkat_lunak')->onDelete('cascade');
        });

        
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {

        Schema::dropIfExists('catatan_pengujian');
        Schema::dropIfExists('pengujian_detail');
        Schema::dropIfExists('pengujian_perangkat_lunak');
    }
};

