<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\aiconvo;

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
    public function show($id)
    {
        $convo = aiconvo::find($id);

        if (is_null($convo)) {
            return response()->json(['message' => 'Conversation not found'], 404);
        }

        return response()->json($convo, 200);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {

        $convo = aiconvo::find($id);

        if (is_null($convo)) {
            return response()->json(['message' => 'Conversation not found'], 404);
        }

        $convo->update($request->all());

        return response()->json($convo, 200);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)
    {
        $convo = aiconvo::find($id);

        if (is_null($convo)) {
            return response()->json(['message' => 'Conversation not found'], 404);
        }

        $convo->delete();

        return response()->json(null, 204);
    }
}
