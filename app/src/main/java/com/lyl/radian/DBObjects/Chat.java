package com.lyl.radian.DBObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by len13 on 23.12.2016.
 */

public class Chat {

    private String transmitterUID;
    private String recieverUID;
    private Map<String, Object> messages; // Key is a timestamp and UID and Value the message

    public Chat() {}

    public Chat(String transmitterUID, String recieverUID, HashMap<String, Object> messages) {
        this.transmitterUID = transmitterUID;
        this.recieverUID = recieverUID;
        this.messages = messages;
    }

    public String getTransmitterUID() {
        return transmitterUID;
    }

    public void setTransmitterUID(String transmitterUID) {
        this.transmitterUID = transmitterUID;
    }

    public String getRecieverUID() {
        return recieverUID;
    }

    public void setRecieverUID(String recieverUID) {
        this.recieverUID = recieverUID;
    }

    public Map<String, Object> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Object> messages) {
        this.messages = messages;
    }

}
