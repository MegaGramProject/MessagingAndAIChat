package Megagram.springbootBackend02;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.kms.v1.CryptoKey;
import com.google.cloud.kms.v1.CryptoKey.CryptoKeyPurpose;
import com.google.cloud.kms.v1.CryptoKeyName;
import com.google.cloud.kms.v1.CryptoKeyVersion;
import com.google.cloud.kms.v1.CryptoKeyVersion.CryptoKeyVersionAlgorithm;
import com.google.cloud.kms.v1.CryptoKeyVersionName;
import com.google.cloud.kms.v1.CryptoKeyVersionTemplate;
import com.google.cloud.kms.v1.DecryptResponse;
import com.google.cloud.kms.v1.EncryptResponse;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.cloud.kms.v1.KeyRingName;
import com.google.cloud.kms.v1.ListCryptoKeyVersionsRequest;
import com.google.protobuf.ByteString;



@RestController
public class backendController {

    private final KeyManagementServiceClient client;

    @Autowired
    public backendController(KeyManagementServiceClient client) {
        this.client = client;
    }

    @Autowired
    private PublicAIConvoRepository publicAIConvoRepository;

    @Autowired
    private PublicAIMessageRepository publicAIMessageRepository;

    @GetMapping("/getAllPublicAIConvos")
    @CrossOrigin(origins = "http://localhost:8007")
    public List<PublicAIConvo> getAllPublicAIConvos() {
        return publicAIConvoRepository.findAll();
    }

    @GetMapping("/getAllPublicAIMessages")
    @CrossOrigin(origins = "http://localhost:8007")
    public List<PublicAIMessage> getAllPublicAIMessages() {
        return publicAIMessageRepository.findAll();
    }

    @GetMapping("/getAllPublicAIMessages/{convoid}")
    @CrossOrigin(origins = "http://localhost:8007")
    public List<PublicAIMessage> getAllPublicAIMessagesForConvo(@PathVariable String convoid) {
        return publicAIMessageRepository.findByConvoid(convoid);
    }


    @PostMapping("/addPublicAIConvo")
    @CrossOrigin(origins = "http://localhost:8007")
    public PublicAIConvo addPublicAIConvo(@RequestBody Map<String, String> request) {
        if(request.containsKey("convotitle") && request.containsKey("username") && request.containsKey("convoid")) {
            String convotitle = request.get("convotitle");
            String username = request.get("username");
            String convoid = request.get("convoid");
            PublicAIConvo newConvo = new PublicAIConvo();
            newConvo.setConvotitle(convotitle);
            newConvo.setUsername(username);
            newConvo.setConvoid(convoid);
            publicAIConvoRepository.save(newConvo);
            return newConvo;
        }
        return null;
    }

    @PostMapping("/addPublicAIMessage")
    @CrossOrigin(origins = "http://localhost:8007")
    public PublicAIMessage addPublicAIMessage(@RequestBody Map<String, String> request) throws Exception {
        if(request.containsKey("convoid") && request.containsKey("username") && request.containsKey("sent")
        && request.containsKey("message") && request.containsKey("messageid")) {
            String convoid = request.get("convoid");
            String username = request.get("username");
            Date sent = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(request.get("sent"));
            String message = request.get("message");
            String messageid = request.get("messageid");

            PublicAIMessage newMessage = new PublicAIMessage();
            newMessage.setConvoid(convoid);
            newMessage.setUsername(username);
            newMessage.setMessage(message);
            newMessage.setSent(sent);
            newMessage.setMessageid(messageid);
            publicAIMessageRepository.save(newMessage);
            return newMessage;
        }
        return null;
    }

    @PatchMapping("/editPublicAIConvoTitle/{id}/{newTitle}")
    @CrossOrigin(origins = "http://localhost:8007")
    public ResponseEntity<PublicAIConvo> editPublicAIConvoTitle(@PathVariable String id, @PathVariable String newTitle) {
        Optional<PublicAIConvo> optionalConvo = publicAIConvoRepository.findById(id);
        if (optionalConvo.isPresent()) {
            PublicAIConvo convo = optionalConvo.get();
            convo.setConvotitle(newTitle);
            publicAIConvoRepository.save(convo);
            return new ResponseEntity<>(convo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/editPublicAIMessage/{id}/{newMessage}")
    @CrossOrigin(origins = "http://localhost:8007")
    public ResponseEntity<PublicAIMessage> editPublicAIMessage(@PathVariable String id, @PathVariable String newMessage) {
        Optional<PublicAIMessage> optionalMessage = publicAIMessageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            PublicAIMessage message = optionalMessage.get();
            message.setMessage(newMessage);
            publicAIMessageRepository.save(message);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletePublicAIConvo/{convoid}")
    @CrossOrigin(origins = "http://localhost:8007")
    public ResponseEntity<Boolean> removePublicAIConvo(@PathVariable String convoid) {
        Optional<PublicAIConvo> optionalConvo = publicAIConvoRepository.findById(convoid);
        if (optionalConvo.isPresent()) {
            PublicAIConvo convo = optionalConvo.get();
            publicAIConvoRepository.delete(convo);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletePublicAIMessage/{messageid}")
    @CrossOrigin(origins = "http://localhost:8007")
    public ResponseEntity<Boolean> removePublicAIMessage(@PathVariable String messageid) {
        Optional<PublicAIMessage> optionalMessage = publicAIMessageRepository.findById(messageid);
        if (optionalMessage.isPresent()) {
            PublicAIMessage message = optionalMessage.get();
            publicAIMessageRepository.delete(message);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/encryptAIConvo")
    @CrossOrigin(origins = "http://localhost:8007")
    public ResponseEntity<Map<String, String>> encryptAIConvo(@RequestBody Map<String, String> request){
            if (request.containsKey("convotitle") && request.containsKey("convoid") && request.containsKey("username")) {
                try {
                    KeyRingName keyRingName = KeyRingName.of("megagram-428802", "global", "aiConvoKeys");

                    CryptoKey key = CryptoKey.newBuilder()
                        .setPurpose(CryptoKeyPurpose.ENCRYPT_DECRYPT)
                        .setVersionTemplate(
                            CryptoKeyVersionTemplate.newBuilder()
                                .setAlgorithm(CryptoKeyVersionAlgorithm.GOOGLE_SYMMETRIC_ENCRYPTION))
                        .build();

                    client.createCryptoKey(keyRingName, request.get("convoid"), key);

                    CryptoKeyName keyName = CryptoKeyName.of("megagram-428802", "global", "aiConvoKeys", request.get("convoid"));
                    EncryptResponse response = client.encrypt(keyName, ByteString.copyFromUtf8(request.get("convotitle")));
                    String ciphertext = Base64.getEncoder().encodeToString(response.getCiphertext().toByteArray());

                    EncryptResponse response2 = client.encrypt(keyName, ByteString.copyFromUtf8(request.get("username")));
                    String ciphertext2 = Base64.getEncoder().encodeToString(response2.getCiphertext().toByteArray());

                    Map<String, String> responseBody = new HashMap<>();
                    responseBody.put("convotitle", ciphertext);
                    responseBody.put("username", ciphertext2);

                    return new ResponseEntity<>(responseBody, HttpStatus.OK);

                } catch (Exception e) {
                    System.out.println("EXCEPTION!!!!\n\n\n" + e.getMessage());
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        @PostMapping("/encryptAIMessage")
        @CrossOrigin(origins = "http://localhost:8007")
        public ResponseEntity<Map<String, String>> encryptAIMessage(@RequestBody Map<String, String> request) {
            if (request.containsKey("message") && request.containsKey("convoid") && request.containsKey("username")) {
                try {
                    CryptoKeyName keyName = CryptoKeyName.of("megagram-428802", "global", "aiConvoKeys", request.get("convoid"));
                    EncryptResponse response = client.encrypt(keyName, ByteString.copyFromUtf8(request.get("message")));
                    String ciphertext = Base64.getEncoder().encodeToString(response.getCiphertext().toByteArray());

                    EncryptResponse response2 = client.encrypt(keyName, ByteString.copyFromUtf8(request.get("username")));
                    String ciphertext2 = Base64.getEncoder().encodeToString(response2.getCiphertext().toByteArray());

                    Map<String, String> responseBody = new HashMap<>();
                    responseBody.put("message", ciphertext);
                    responseBody.put("username", ciphertext2);

                    return new ResponseEntity<>(responseBody, HttpStatus.OK);
                }

                catch (Exception f) {
                        System.out.println("EXCEPTION!!!!\n\n\n" + f.getMessage());
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        @PostMapping("/decryptAIConvo/{convoid}")
        @CrossOrigin(origins = "http://localhost:8007")
        public ResponseEntity<ArrayList<ArrayList<String>>> decryptAIConvo(@RequestBody Map<String, List<List<String>>> request, @PathVariable String convoid) {
            if(request.containsKey("encryptedConvoMessages")) {
                try {
                    ArrayList<ArrayList<String>> responseBody = new ArrayList<ArrayList<String>> ();
                    CryptoKeyName keyName = CryptoKeyName.of("megagram-428802", "global", "aiConvoKeys", convoid);
                    ByteString ciphertextByteString;
                    DecryptResponse decryptedResponse;

                    List<List<String>> encryptedConvoMessages = request.get("encryptedConvoMessages");
                    String plaintextMessage;
                    String plaintextMessageUsername;
                    ArrayList<String> decryptedMessage;
                    for(List<String> encryptedMessage : encryptedConvoMessages) {
                        ciphertextByteString = ByteString.copyFrom(Base64.getDecoder().decode(encryptedMessage.get(2)));
                        decryptedResponse = client.decrypt(keyName, ciphertextByteString);
                        plaintextMessage = decryptedResponse.getPlaintext().toStringUtf8();

                        ciphertextByteString = ByteString.copyFrom(Base64.getDecoder().decode(encryptedMessage.get(0)));
                        decryptedResponse = client.decrypt(keyName, ciphertextByteString);
                        plaintextMessageUsername = decryptedResponse.getPlaintext().toStringUtf8();

                        decryptedMessage = new ArrayList<String>();
                        decryptedMessage.add(plaintextMessageUsername);
                        decryptedMessage.add(encryptedMessage.get(1));
                        decryptedMessage.add(plaintextMessage);
                        responseBody.add(decryptedMessage);
                    }
                    return new ResponseEntity<>(responseBody, HttpStatus.OK);
                }
                catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return null;
        }

        @PostMapping("/decryptAIConvoWithSent/{convoid}")
        @CrossOrigin(origins = "http://localhost:8007")
        public ResponseEntity<ArrayList<ArrayList<String>>> decryptAIConvoWithSent(@RequestBody Map<String, List<List<String>>> request, @PathVariable String convoid) {
            if(request.containsKey("encryptedConvoMessages")) {
                try {
                    ArrayList<ArrayList<String>> responseBody = new ArrayList<ArrayList<String>> ();
                    CryptoKeyName keyName = CryptoKeyName.of("megagram-428802", "global", "aiConvoKeys", convoid);
                    ByteString ciphertextByteString;
                    DecryptResponse decryptedResponse;

                    List<List<String>> encryptedConvoMessages = request.get("encryptedConvoMessages");
                    String plaintextMessage;
                    String plaintextMessageUsername;
                    ArrayList<String> decryptedMessage;
                    for(List<String> encryptedMessage : encryptedConvoMessages) {
                        ciphertextByteString = ByteString.copyFrom(Base64.getDecoder().decode(encryptedMessage.get(2)));
                        decryptedResponse = client.decrypt(keyName, ciphertextByteString);
                        plaintextMessage = decryptedResponse.getPlaintext().toStringUtf8();

                        ciphertextByteString = ByteString.copyFrom(Base64.getDecoder().decode(encryptedMessage.get(0)));
                        decryptedResponse = client.decrypt(keyName, ciphertextByteString);
                        plaintextMessageUsername = decryptedResponse.getPlaintext().toStringUtf8();

                        decryptedMessage = new ArrayList<String>();
                        decryptedMessage.add(plaintextMessageUsername);
                        decryptedMessage.add(encryptedMessage.get(1));
                        decryptedMessage.add(plaintextMessage);
                        decryptedMessage.add(encryptedMessage.get(3));
                        responseBody.add(decryptedMessage);
                    }
                    return new ResponseEntity<>(responseBody, HttpStatus.OK);
                }
                catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return null;
        }

        @PostMapping("/getAllAIConvos/{username}")
        @CrossOrigin(origins = "http://localhost:8007")
        public ResponseEntity<ArrayList<List<String>>> getAllAIConvos(@RequestBody Map<String, List<List<String>>> request, @PathVariable String username) {
            if(request.containsKey("aiConvosOfAllUsers")) {
                List<List<String>> aiConvos = request.get("aiConvosOfAllUsers");
                ArrayList<List<String>> output = new ArrayList<List<String>>();
                for(List<String> aiConvo : aiConvos) {
                    String aiConvoId = aiConvo.get(0);
                    String encryptedUsername = aiConvo.get(2);
                    String encryptedConvotitle = aiConvo.get(1);
                    String sent = aiConvo.get(3);
                    if(encryptedUsername!=null){
                        CryptoKeyName keyName = CryptoKeyName.of("megagram-428802", "global", "aiConvoKeys", aiConvoId);
                        ByteString ciphertextByteString = ByteString.copyFrom(Base64.getDecoder().decode(encryptedUsername));
                        DecryptResponse decryptedResponse = client.decrypt(keyName, ciphertextByteString);
                        String plaintextUsername = decryptedResponse.getPlaintext().toStringUtf8();
        
                        if(plaintextUsername.equals(username)) {
                            ArrayList<String> newConvo = new ArrayList<String>();
                            newConvo.add(aiConvoId);
    
                            ciphertextByteString = ByteString.copyFrom(Base64.getDecoder().decode(encryptedConvotitle));
                            decryptedResponse = client.decrypt(keyName, ciphertextByteString);
                            String plaintextConvotitle = decryptedResponse.getPlaintext().toStringUtf8();
    
                            newConvo.add(plaintextConvotitle);
                            newConvo.add(plaintextUsername);
                            newConvo.add(sent);
                            output.add(newConvo);
                        }
                    }
                }
                return new ResponseEntity<>(output, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @PostMapping("/encryptAIMessageForEditing/{convoid}")
        @CrossOrigin(origins = "http://localhost:8007")
        public ResponseEntity<String> encryptAIMessageForEditing(@RequestBody Map<String, String> request, @PathVariable String convoid) {
            try {
                if(request.containsKey("message")) {
                    CryptoKeyName keyName = CryptoKeyName.of("megagram-428802", "global", "aiConvoKeys", convoid);
                    EncryptResponse response = client.encrypt(keyName, ByteString.copyFromUtf8(request.get("message")));
                    String ciphertext = Base64.getEncoder().encodeToString(response.getCiphertext().toByteArray());
                    return new ResponseEntity<>(ciphertext, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

        @PostMapping("/encryptAIConvoTitleForEditing/{convoid}")
        @CrossOrigin(origins = "http://localhost:8007")
        public ResponseEntity<String> encryptAIConvoTitleForEditing(@RequestBody Map<String, String> request, @PathVariable String convoid) {
            try {
                if(request.containsKey("convotitle")) {
                    CryptoKeyName keyName = CryptoKeyName.of("megagram-428802", "global", "aiConvoKeys", convoid);
                    EncryptResponse response = client.encrypt(keyName, ByteString.copyFromUtf8(request.get("convotitle")));
                    String ciphertext = Base64.getEncoder().encodeToString(response.getCiphertext().toByteArray());
                    return new ResponseEntity<>(ciphertext, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

        @DeleteMapping("/destroyAIConvoKey/{convoid}")
        @CrossOrigin(origins = "http://localhost:8007")
        public Boolean destroyAIConvoKey(@PathVariable String convoid) {
            try {
                CryptoKeyName keyName = CryptoKeyName.of("megagram-428802", "global", "aiConvoKeys", convoid);
                ListCryptoKeyVersionsRequest request = ListCryptoKeyVersionsRequest.newBuilder()
                    .setParent(keyName.toString())
                    .build();

                KeyManagementServiceClient.ListCryptoKeyVersionsPagedResponse response = client.listCryptoKeyVersions(request);

                for (CryptoKeyVersion version : response.iterateAll()) {
                    String versionId = version.getName().split("/cryptoKeyVersions/")[1];
                    CryptoKeyVersionName versionName = CryptoKeyVersionName.of(
                    "megagram-428802", "global", "aiConvoKeys", convoid, versionId);
                    client.destroyCryptoKeyVersion(versionName);
                }
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }

        @PostMapping("/getDecryptedAIConvotitle/{convoid}")
        @CrossOrigin(origins = "http://localhost:8007")
        public String getDecryptedAIConvotitle(@PathVariable String convoid, @RequestBody Map<String, String> request) {
            if(request.containsKey("convotitle")) {
                CryptoKeyName keyName = CryptoKeyName.of("megagram-428802", "global", "aiConvoKeys", convoid);
                ByteString ciphertextByteString = ByteString.copyFrom(Base64.getMimeDecoder().decode(request.get("convotitle")));
                DecryptResponse decryptedResponse = client.decrypt(keyName, ciphertextByteString);
                String plaintextConvotitle = decryptedResponse.getPlaintext().toStringUtf8();
                return plaintextConvotitle;
            }
            return "";
        }
        

        @PostMapping("/addPublicAIMessages/{convoid}")
        @CrossOrigin(origins = "http://localhost:8007")
        public Boolean addPublicAIMessages(@RequestBody Map<String, List<List<String>>> request, @PathVariable String convoid) throws Exception {
            if(request.containsKey("messages")) {
                List<List<String>> messages = request.get("messages");
                for(List<String> message : messages) {
                    PublicAIMessage newMessage = new PublicAIMessage();
                    newMessage.setConvoid(convoid);
                    newMessage.setUsername(message.get(0));
                    newMessage.setMessage(message.get(2));
                    newMessage.setSent(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(message.get(3)));
                    newMessage.setMessageid(message.get(1));
                    publicAIMessageRepository.save(newMessage);
                }

                return true;
            }
            return false;
        }

        @GetMapping("/getPublicAIConvoTitle/{convoid}")
        @CrossOrigin(origins = "http://localhost:8007")
        public String getPublicAIConvoTitle(@PathVariable String convoid) {
            Optional<PublicAIConvo> optionalConvo = publicAIConvoRepository.findById(convoid);
            if(optionalConvo.isPresent()) {
                PublicAIConvo convo = optionalConvo.get();
                return convo.getConvotitle();
            }
            return "";
        }
        
    



    
}
