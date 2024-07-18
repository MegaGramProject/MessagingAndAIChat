package Megagram.springbootBackend02;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "publicAIConvos")
public class PublicAIConvo {

    @Id
    @Column(name="convoid")
    private String convoid;

    @Column(name="convotitle")
    private String convotitle;

    @Column(name="username")
    private String username;

    public String getConvoid() {
        return this.convoid;
    }

    public String getConvotitle() {
        return this.convotitle;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setConvotitle(String convotitle) {
        this.convotitle = convotitle;
    }

    public void setConvoid(String convoid) {
        this.convoid = convoid;
    }
}

