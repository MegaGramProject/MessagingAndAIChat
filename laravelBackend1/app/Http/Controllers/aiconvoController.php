<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\aiconvo;
use App\Models\aimessage;
use App\Http\Middleware\CustomCorsMiddleware;
use Google\Cloud\Storage\StorageClient;

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

    public function getFilesForConvo($convoid) {
        $messages = aimessage::where('convoid', $convoid)->get();
    
        $messageIds = [];
        if (!$messages->isEmpty()) {
            foreach($messages as $message) {
                $messageIds[] = $message->messageid;
            }
    
            $projectId = 'megagram-428802';
            $bucketName = 'megagram-aichat-inputfiles';
            putenv("GOOGLE_APPLICATION_CREDENTIALS=/Users/rishavr/Downloads/megagram-428802-476264306d3b.json");
    
            $storage = new StorageClient([
                'projectId' => $projectId,
            ]);
    
            $bucket = $storage->bucket($bucketName);
            $filesList = [];
    
            foreach ($bucket->objects() as $object) {
                $objectInfo = $object->info();
                $messageId = $objectInfo['metadata']['messageid'];
    
                if (in_array($messageId, $messageIds)) {
                    $blobData = $object->downloadAsString();
                    $fileName = $objectInfo['metadata']['filename'];
                    $messageId = mb_convert_encoding($messageId, 'UTF-8', 'UTF-8');
                    $encodedBlobData = base64_encode($blobData);
                    $contentType = $objectInfo['contentType'];
    
                    $filesList[] = [$fileName, $messageId, $encodedBlobData, $contentType];
                }
            }
    
            return response()->json($filesList, 200);
        }
    
        return response()->json(null, 204);
    }
    
}
