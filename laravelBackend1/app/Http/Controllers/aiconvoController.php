<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\aiconvo;
use App\Models\aimessage;

class aiconvoController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $convos = aiconvo::all();
        return response()->json($convos, 200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {

        $convo = aiconvo::create($request->all());

        return response()->json($convo, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show($convoid)
    {
        $convo = aiconvo::find($convoid);

        if (is_null($convo)) {
            return response()->json(['message' => 'Conversation not found'], 404);
        }

        return response()->json($convo, 200);
    }

    public function showByUsername($username)
    {
        $convos = aiconvo::where('username', $username)->get();
        
        if ($convos->isEmpty()) {
            return response()->json(['message' => 'Conversations not found'], 404);
        }

        return response()->json($convos, 200);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $convoid)
    {

        $convo = aiconvo::find($convoid);

        if (is_null($convo)) {
            return response()->json(['message' => 'Conversation not found'], 404);
        }

        $convo->update($request->all());

        return response()->json($convo, 200);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($convoid)
    {

        $convo = aiconvo::find($convoid);

        if (is_null($convo)) {
            return response()->json(['message' => 'Conversation not found'], 404);
        }

        // Delete the conversation
        $convo->delete();

        // Find and delete associated messages
        $messages = aimessage::where('convoid', $convoid)->get();
        
        if (!$messages->isEmpty()) {
            foreach ($messages as $message) {
                $message->delete();
            }
        }

        return response()->json(null, 204);
    }

}
