package Megagram.springbootBackend02;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class backendController {

    public backendController() {
        //
    }

    @Autowired
    private PublicAIConvoRepository publicAIConvoRepository;

    @Autowired
    private PublicAIMessageRepository publicAIMessageRepository;

    @GetMapping
    public List<PublicAIConvo> getAllConvos() {
        return publicAIConvoRepository.findAll();
    }
    
}
