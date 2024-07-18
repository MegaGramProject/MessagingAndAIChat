package Megagram.springbootBackend02;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "publicAIConvos")
public class PublicAIMessage {
    @Id
    @Column(name="messageid")
    private String messageid;

    @Column(name="convoid")
    private String convoid;

    @Column(name="message")
    private String message;

    @Column(name="username")
    private String username;

    @Column(name="sent")
    private Date sent;

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getConvoid() {
        return convoid;
    }

    public void setConvoid(String convoid) {
        this.convoid = convoid;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
