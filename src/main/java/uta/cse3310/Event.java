package uta.cse3310;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;

public class Event {
    private int Game_ID;
    private String User_ID;
    private int button;



    public void loginSuccess(WebSocket connection){
        JsonObject json = new JsonObject();
        json.addProperty("type", "loginSuccess");
        connection.send(json.toString());
        System.out.println("json: " + json);
    }

    public void Error(WebSocket connection, String message){
        JsonObject json = new JsonObject();
        json.addProperty("type", "error");
        json.addProperty("message", message);
        connection.send(json.toString());
    }

}
