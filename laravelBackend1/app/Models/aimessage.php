<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class aimessage extends Model
{
    use HasFactory;
    protected $table = 'aimessages';
    protected $primaryKey = 'messageid';
    protected $keyType = 'string';

    public $timestamps = false;

    protected $fillable = [
        'convoid', 'messageid', 'username', 'message', 'sent'
    ];
}
