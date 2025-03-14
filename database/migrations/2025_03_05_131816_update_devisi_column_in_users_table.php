<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;
use Illuminate\Support\Facades\DB;

return new class extends Migration
{
    public function up(): void
    {
        // Update invalid 'devisi' values before modifying the column
        DB::table('users')
            ->whereNotIn('devisi', ['egov', 'opj', 'legal', 'c-care', 'nro', 'noc', 'rumah tangga', 'helpdesk', 'hrd', 'retail', 'vas', 'corp', 'finance'])
            ->update(['devisi' => null]);

        // Now modify the 'devisi' column to the new values
        Schema::table('users', function (Blueprint $table) {
            $table->enum('devisi', ['egov', 'opj', 'legal', 'c-care', 'nro', 'noc', 'rumah tangga', 'helpdesk', 'hrd', 'retail', 'vas', 'corp', 'finance'])->nullable()->change();
        });
    }

    public function down(): void
    {
        // Revert back to the old 'devisi' values if necessary
        Schema::table('users', function (Blueprint $table) {
            $table->enum('devisi', ['IT', 'HR', 'Finance', 'Marketing', 'Operations'])->nullable()->change();
        });
    }
};
