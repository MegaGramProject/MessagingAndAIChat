package Megagram.springbootBackend02;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import com.google.cloud.kms.v1.CryptoKeyVersion.CryptoKeyVersionAlgorithm;
import com.google.cloud.kms.v1.CryptoKeyVersionTemplate;
import com.google.cloud.kms.v1.DecryptResponse;
import com.google.cloud.kms.v1.EncryptResponse;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.cloud.kms.v1.KeyRingName;
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

    @GetMapping("/getAllConvos")
    @CrossOrigin(origins = "http://localhost:8007")
    public List<PublicAIConvo> getAllConvos() {
        return publicAIConvoRepository.findAll();
    }

    @GetMapping("/getAllMessages")
    @CrossOrigin(origins = "http://localhost:8007")
    public List<PublicAIMessage> getAllMessages() {
        return publicAIMessageRepository.findAll();
    }


    @PostMapping("/addConvo")
    @CrossOrigin(origins = "http://localhost:8007")
    public PublicAIConvo addConvo(@RequestBody Map<String, String> request) {
        if(request.containsKey("convotitle") && request.containsKey("username")) {
            String convotitle = request.get("convotitle");
            String username = request.get("username");
            PublicAIConvo newConvo = new PublicAIConvo();
            newConvo.setConvotitle(convotitle);
            newConvo.setUsername(username);
            newConvo.setConvoid(UUID.randomUUID().toString());
            publicAIConvoRepository.save(newConvo);
            return newConvo;
        }
        return null;
    }

    @PostMapping("/addMessage")
    @CrossOrigin(origins = "http://localhost:8007")
    public PublicAIMessage addMessage(@RequestBody Map<String, String> request) throws Exception {
        if(request.containsKey("convoid") && request.containsKey("username") && request.containsKey("sent")
        && request.containsKey("message")) {
            String convoid = request.get("convoid");
            String username = request.get("username");
            Date sent = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(request.get("sent"));
            String message = request.get("message");

            PublicAIMessage newMessage = new PublicAIMessage();
            newMessage.setConvoid(convoid);
            newMessage.setUsername(username);
            newMessage.setMessage(message);
            newMessage.setSent(sent);
            newMessage.setMessageid(UUID.randomUUID().toString());
            publicAIMessageRepository.save(newMessage);
            return newMessage;
        }
        return null;
    }

    @PatchMapping("/editConvoTitle/{id}/{newTitle}")
    @CrossOrigin(origins = "http://localhost:8007")
    public ResponseEntity<PublicAIConvo> editConvoTitle(@PathVariable String id, @PathVariable String newTitle) {
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

    @PatchMapping("/editMessage/{id}/{newMessage}")
    @CrossOrigin(origins = "http://localhost:8007")
    public ResponseEntity<PublicAIMessage> editMessage(@PathVariable String id, @PathVariable String newMessage) {
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

    @DeleteMapping("/deleteConvo/{convoid}")
    @CrossOrigin(origins = "http://localhost:8007")
    public ResponseEntity<Boolean> removeConvo(@PathVariable String convoid) {
        Optional<PublicAIConvo> optionalConvo = publicAIConvoRepository.findById(convoid);
        if (optionalConvo.isPresent()) {
            PublicAIConvo convo = optionalConvo.get();
            publicAIConvoRepository.delete(convo);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteMessage/{messageid}")
    @CrossOrigin(origins = "http://localhost:8007")
    public ResponseEntity<Boolean> removeMessage(@PathVariable String messageid) {
        Optional<PublicAIMessage> optionalMessage = publicAIMessageRepository.findById(messageid);
        if (optionalMessage.isPresent()) {
            PublicAIMessage message = optionalMessage.get();
            publicAIMessageRepository.delete(message);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/encryptConvo")
    @CrossOrigin(origins = "http://localhost:8007")
    public ResponseEntity<String> encryptConvo(@RequestBody Map<String, String> request) throws Exception{
        if(request.containsKey("convotitle") && request.containsKey("convoid")) {
            
            try {
                KeyRingName keyRingName = KeyRingName.of("megagram-428802", "global", "aiConvoKeys");

                CryptoKey key =
                    CryptoKey.newBuilder()
                        .setPurpose(CryptoKeyPurpose.ENCRYPT_DECRYPT)
                        .setVersionTemplate(
                            CryptoKeyVersionTemplate.newBuilder()
                                .setAlgorithm(CryptoKeyVersionAlgorithm.GOOGLE_SYMMETRIC_ENCRYPTION))
                        .build();


                client.createCryptoKey(keyRingName, request.get("convoid"), key);
                
                CryptoKeyName keyName = CryptoKeyName.of("megagram-428802", "global", "aiConvoKeys", request.get("convoid"));
                EncryptResponse response = client.encrypt(keyName, ByteString.copyFromUtf8(request.get("convotitle")));
                String ciphertext = Base64.getEncoder().encodeToString(response.getCiphertext().toByteArray());
                
                ByteString ciphertextByteString = ByteString.copyFrom(Base64.getDecoder().decode(ciphertext));
                DecryptResponse decryptedResponse = client.decrypt(keyName, ciphertextByteString);
                String plaintext = decryptedResponse.getPlaintext().toStringUtf8();
                return new ResponseEntity<>("Ciphertext: " + ciphertext + "\nPlaintext: " + plaintext, HttpStatus.OK);

            }
            catch(Exception e) {
                System.out.println("EXCEPTION!!!!\n\n\n" + e.getMessage());
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



    
}
