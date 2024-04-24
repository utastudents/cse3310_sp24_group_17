package uta.cse3310;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.InetSocketAddress;
import java.util.Vector;

public class App extends WebSocketServer {

    private Vector<Game> ActiveGames = new Vector<>();
    private MainLobby mainLobby = new MainLobby();

    public App(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // Initial message when player connects
        conn.send("Welcome");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        mainLobby.removePlayerFromMainLobby(conn);
        broadcast("Player has left");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        String type = json.get("type").getAsString();
        if (type.equals("login")) {
            String username = json.get("username").getAsString();
            Player player = new Player(username, conn);
            mainLobby.addPlayerToMainLobby(conn, username);
            broadcast("New player: " + username);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started successfully");
    }

    public void broadcast(String message) {
        mainLobby.getPlayers().forEach(player -> {
            player.sendMessage(message);
        });
    }


    public static void main(String[] args) {
        // Set the port for the Http server - 9017
        Integer Http_port = 9017;
        String PortString = System.getenv("HTTP_PORT");
        if (PortString != null) {
           Http_port = Integer.parseInt(PortString);
        }
        HttpServer Http = new HttpServer(Http_port, "./html");
        Http.start();
        System.out.println("http Server started on port:" + Http_port);


     // Set the port for the WebSocket server - 9117
        Integer Websocket_port = 9117;
        PortString = System.getenv("WEBSOCKET_PORT");
        if (PortString != null) {
           Websocket_port = Integer.parseInt(PortString);
        }
        App app = new App(Websocket_port);
        app.start();
        System.out.println("WebSocket Server started on port: " + Websocket_port);
    }

}
