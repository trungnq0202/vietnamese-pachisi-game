/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2019C
  Assessment: Final Project
  Created date: 01/01/2020
  By: Group 10 (3426353,3791159,3742774,3748575,3695662)
  Last modified: 14/01/2020
  By: Group 10 (3426353,3791159,3742774,3748575,3695662)
  Acknowledgement: none.
*/

package models;

import java.io.Serializable;

public class Message implements Serializable {
    // fields
    private String sender = "";
    private String action = "";
    private Object data;

    // constructors
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
