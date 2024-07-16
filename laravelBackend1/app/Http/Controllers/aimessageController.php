<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\aimessage;
use App\Http\Middleware\CustomCorsMiddleware;
use Google\Cloud\TextToSpeech\V1\TextToSpeechClient;
use Google\Cloud\TextToSpeech\V1\SynthesisInput;
use Google\Cloud\TextToSpeech\V1\VoiceSelectionParams;
use Google\Cloud\TextToSpeech\V1\AudioConfig;
use Google\Cloud\TextToSpeech\V1\AudioEncoding;
use Google\Cloud\Storage\StorageClient;
use Illuminate\Support\Str;


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

    public function showByConvoId($convoid)
    {
        $messages = aimessage::where('convoid', $convoid)->get();
        
        if ($messages->isEmpty()) {
            return response()->json(['message' => 'Messages not found'], 404);
        }

        return response()->json($messages, 200);
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
    public function destroy($messageid)
    {
        $message = aimessage::find($messageid);

        if (is_null($message)) {
            return response()->json(['message' => 'Message not found'], 404);
        }

        $message->delete();

        $projectId = 'megagram-428802';
        $bucketName = 'megagram-aichat-inputfiles';
        putenv("GOOGLE_APPLICATION_CREDENTIALS=/Users/rishavr/Downloads/megagram-428802-476264306d3b.json");

    
        $storage = new StorageClient([
            'projectId' => $projectId,
        ]);


        $bucket = $storage->bucket($bucketName);

        foreach ($bucket->objects() as $object) {
            $objectInfo = $object->info();
            if ($objectInfo['metadata']['messageid'] === $messageid) {
                $object->delete();
            }
        }

        return response()->json(null, 204);


    }

    public function spokenVersionOfMessage(Request $request)
    {
            $request->validate([
                'message' => 'required|string',
                'accent' => 'required|string',
                'voiceSpeed' => 'required|string',
                'voiceType' => 'required|string'
            ]);


            $message = $request->input('message');
            $accent = $request->input('accent');
            $voiceSpeed = $request->input('voiceSpeed');
            $voiceType = $request->input('voiceType');

            $accentMappings = [
                'american' => 'en-US',
                'australian' => 'en-AU',
                'british' => 'en-GB',
                'indian' => 'en-IN'
            ];
            
            $voiceSpeedMappings = [
                'speed1' => 1,
                'speed0point25' => 0.25,
                'speed0point5' => 0.5,
                'speed1point5' => 1.5,
                'speed2' => 2,
                'speed2point5' => 2.5,
                'speed3' => 3,
                'speed3point5' => 3.5,
                'speed4' => 4
            ];
            
            $voiceTypeMappings = [
                'american' => [
                    'femaleVoice' => 'en-US-Studio-O',
                    'maleVoice' => 'en-US-Studio-Q'
                ],
                'australian' => [
                    'femaleVoice' =>'en-AU-Neural2-A',
                    'maleVoice' => 'en-AU-Neural2-B'
                ],
                'british' => [
                    'femaleVoice' => 'en-GB-Neural2-A',
                    'maleVoice' => 'en-GB-Neural2-B'
                ],
                'indian' => [
                    'femaleVoice' => 'en-IN-Neural2-A',
                    'maleVoice' => 'en-IN-Neural2-B'
                ]
            ];

        
            if (!isset($accentMappings[$accent])) {
                return response()->json(['error' => 'Invalid accent'], 400);
                \Log::info("ACCENT F");
            }
            if (!isset($voiceSpeedMappings[$voiceSpeed])) {
                return response()->json(['error' => 'Invalid voice speed'], 400);
                \Log::info("voice speed F");
            }
            if (!isset($voiceTypeMappings[$accent][$voiceType])) {
                return response()->json(['error' => 'Invalid voice type'], 400);
                
            }

            putenv("GOOGLE_APPLICATION_CREDENTIALS=/Users/rishavr/Downloads/megagram-428802-476264306d3b.json");

            $client = new TextToSpeechClient();

            $input_text = new SynthesisInput();
            $input_text->setText($message);

            $voice = new VoiceSelectionParams();
            $voice->setLanguageCode($accentMappings[$accent]);
            $voice->setName($voiceTypeMappings[$accent][$voiceType]);

            $audioConfig = new AudioConfig();
            $audioConfig->setAudioEncoding(AudioEncoding::LINEAR16);
            $audioConfig->setSpeakingRate($voiceSpeedMappings[$voiceSpeed]);

            $response = $client->synthesizeSpeech($input_text, $voice, $audioConfig);

            $audioContent = $response->getAudioContent();

            return response($audioContent)->header('Content-Type', 'audio/wav');
    }


    public function sendFilesWithMessage(Request $request) {
        
        $request->validate([
            'messageid' => 'required|string',
        ]);

        $messageId = $request->input('messageid');


        $projectId = 'megagram-428802';
        $bucketName = 'megagram-aichat-inputfiles';
        putenv("GOOGLE_APPLICATION_CREDENTIALS=/Users/rishavr/Downloads/megagram-428802-476264306d3b.json");

    
        $storage = new StorageClient([
            'projectId' => $projectId,
        ]);


        $bucket = $storage->bucket($bucketName);

        foreach ($request->files as $uploadedFile) {
            $uniqueFileName = Str::uuid() . '.' . $uploadedFile->getClientOriginalExtension();
            $bucket->upload(
                fopen($uploadedFile->getPathname(), 'r'),
                [
                    'name' => $uniqueFileName,
                    'metadata' => [
                        'metadata' => [
                            'messageid' => $messageId,
                            'filename' => $uploadedFile->getClientOriginalName(),
                        ]
                    ]
                ]
            );
        }

        return response()->json(['message' => 'Files uploaded successfully'], 200);
    }

}
