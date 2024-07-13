<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\aimessage;

class aimessageController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $messages = aimessage::all();
        return response()->json($messages, 200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $message = aimessage::create($request->all());

        return response()->json($message, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        $message = aimessage::find($id);

        if (is_null($message)) {
            return response()->json(['message' => 'Message not found'], 404);
        }

        return response()->json($message, 200);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        $message = aimessage::find($id);

        if (is_null($message)) {
            return response()->json(['message' => 'Message not found'], 404);
        }

        $message->update($request->all());

        return response()->json($message, 200);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)
    {
        $message = aimessage::find($id);

        if (is_null($message)) {
            return response()->json(['message' => 'Message not found'], 404);
        }

        $message->delete();

        return response()->json(null, 204);
    }
}
