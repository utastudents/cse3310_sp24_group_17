package uta.cse3310;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ChatBox {
    private List<String> messages = new ArrayList<>();

    public void addMessage(String message) {
        JsonObject chatJson = new JsonObject();
        messages.add(message);  // Store message
        chatJson.addProperty("type", "chatMessage");
        
    }

    public List<String> getMessages() {
        return messages;  // Retrieve all messages
    }

    public boolean checkLanguage(String line){
        return true;
    }

}
