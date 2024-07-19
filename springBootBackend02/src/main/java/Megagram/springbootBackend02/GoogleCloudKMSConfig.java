package Megagram.springbootBackend02;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.cloud.kms.v1.KeyManagementServiceSettings;

@Configuration
public class GoogleCloudKMSConfig {


    private String credentialsPath = "/Users/rishavr/Downloads/megagram-428802-476264306d3b.json";

    @Bean
    public KeyManagementServiceClient keyManagementServiceClient() throws IOException {
        GoogleCredentials credentials;
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = GoogleCredentials.fromStream(serviceAccountStream);
        }

        KeyManagementServiceSettings settings = KeyManagementServiceSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();

        return KeyManagementServiceClient.create(settings);
    }
}
