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
        Schema::create('aimessages', function (Blueprint $table) {
            $table->string('convoid');
            $table->string('messageid');
            $table->string('username');
            $table->string('message');
            $table->dateTime('sent');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('aimessages');
    }
};
