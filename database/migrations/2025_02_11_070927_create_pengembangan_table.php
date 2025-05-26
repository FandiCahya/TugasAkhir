<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('pengembangan', function (Blueprint $table) {
            $table->uuid('id')->primary(); // Primary key UUID
            $table->uuid('user_id');
            $table->uuid('pengajuan_id'); // Foreign key ke pengajuan
            $table->date('tanggal_mulai'); // Tanggal mulai pengembangan
            $table->date('tanggal_selesai'); // Tanggal selesai pengembangan
            $table->string('tahap'); // Tahap perkembangan
            $table->integer(column: 'persentase'); // Persentase perkembangan
            $table->text('keterangan')->nullable(); // Keterangan perkembangan
            $table->enum('status', ['developed', 'finished','testing'])->default('developed');
            $table->timestamps(); // Created_at dan updated_at

            // Menambahkan foreign key constraint untuk pengajuan_id
            $table->foreign('user_id')->references('id')->on('users')->onDelete('cascade');
            $table->foreign('pengajuan_id')->references('id')->on('pengajuan')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('pengembangan');
    }
};
