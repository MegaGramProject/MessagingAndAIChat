<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\aiconvoController;
use App\Http\Controllers\aimessageController;
use App\Http\Kernel;


Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');


Route::get('/aiconvos', [aiconvoController::class, 'index']);

Route::get('/aimessages', [aimessageController::class, 'index']);

Route::get('/aiconvo/{convoid}', [aiconvoController::class, 'show']);

Route::get('/aimessage/{messageid}', [aimessageController::class, 'show']);

Route::get('/aiconvos/{username}', [aiconvoController::class, 'showByUsername']);

Route::get('/aimessages/{convoid}', [aimessageController::class, 'showByConvoId']);

Route::post('/aiconvo', [aiconvoController::class, 'store']);

Route::post('/aimessage', [aimessageController::class, 'store']);

Route::patch('/aiconvo/{convoid}', [aiconvoController::class, 'update']);

Route::patch('/aimessage/{messageid}', [aimessageController::class, 'update']);

Route::delete('/aiconvo/{convoid}', [aiconvoController::class, 'destroy']);

Route::delete('/aimessage/{messageid}', [aimessageController::class, 'destroy']);

