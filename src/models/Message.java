package models;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender = "";
    private String action = "";
    private Object data;

    public Message() {};

    public Message(String action, Object data) {
        // messages from server doesn't need to specify sender
        this.sender = null;
        this.action = action;
        this.data = data;
    }

    public Message(String sender, String action, Object data) {
        this.sender = sender;
        this.action = action;
        this.data = data;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return this.sender;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }
}
