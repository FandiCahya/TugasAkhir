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
        Schema::create('pengujian', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->uuid('pengembangan_id');
            $table->enum('hasil', ['positif', 'negatif']);
            $table->text('catatan')->nullable();
            $table->uuid('tester_id');
            $table->timestamps();

            // Foreign keys
            $table->foreign('pengembangan_id')->references('id')->on('pengembangan')->onDelete('cascade');
            $table->foreign('tester_id')->references('id')->on('users')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('pengujian');
    }
};
