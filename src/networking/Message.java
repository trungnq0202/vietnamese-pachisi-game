package networking;
import models.Horse;

import java.io.Serializable;
import java.time.LocalTime;

public class Message implements Serializable {

    String senderName = "";
    String content;
    LocalTime time = LocalTime.now();


    Message (String content){
        this.content = content;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public String getContent() {
        return this.content;
    }

    public String getTime() {
        return this.time.toString();
    }
}
