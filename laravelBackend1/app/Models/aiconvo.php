<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class aiconvo extends Model
{
    use HasFactory;
    protected $table = 'aiconvos';
    protected $primaryKey = 'convoid';
    protected $keyType = 'string';
    protected $fillable = [
        'convoid', 'convotitle', 'username',
    ];

    public $timestamps = false;
}
