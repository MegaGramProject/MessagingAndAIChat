package Megagram.springbootBackend02;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PublicAIMessageRepository extends JpaRepository<PublicAIMessage, String> {
    List<PublicAIMessage> findByConvoid(@Param("convoid") String convoid);
}
