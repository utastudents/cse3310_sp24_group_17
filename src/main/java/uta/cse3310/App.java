package uta.cse3310;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Vector;

public class App extends WebSocketServer {

    private Vector<Game> ActiveGames = new Vector<>();
    private int GameId = 1;
    private int connectionId = 0;

    public App(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message from " + conn.getRemoteSocketAddress() + ": " + message);
        conn.send("Echo: " + message); // Echo back the received message
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        //System.err.println("Error on connection " + conn.getRemoteSocketAddress() + ": " + ex);
    }

    @Override
    public void onStart() {
        
    }

    public static void main(String[] args) {
        
        int port = 9880;
        HttpServer Http = new HttpServer(port, "./html");
        Http.start();
        System.out.println("http Server started on port:" + port);
 
        
     // Set the port for the WebSocket server
        App app = new App(port);
        app.start();
        System.out.println("WebSocket Server started on port: " + port);
    }
}
