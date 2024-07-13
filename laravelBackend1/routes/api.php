<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\aiconvoController;
use App\Http\Controllers\aimessageController;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');


Route::get('/aiconvos', [aiconvoController::class, 'index']);

Route::get('/aimessages', [aimessageController::class, 'index']);