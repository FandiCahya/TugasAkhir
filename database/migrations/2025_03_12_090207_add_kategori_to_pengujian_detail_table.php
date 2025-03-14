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
        Schema::table('pengujian_detail', function (Blueprint $table) {
            $table->enum('kategori', ['uji_positif', 'uji_negatif'])->after('hasil_pengujian')->default('uji_positif');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('pengujian_detail', function (Blueprint $table) {
            $table->dropColumn('kategori');
        });
    }
};
