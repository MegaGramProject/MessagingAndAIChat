package Megagram.springbootBackend02;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicAIMessageRepository extends JpaRepository<PublicAIMessage, String> {
}
