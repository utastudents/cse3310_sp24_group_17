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
        // No functionality needed here
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // No functionality needed here
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // No functionality needed here
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        // No functionality needed here
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        // No functionality needed here
    }

    @Override
    public void onStart() {
        // No functionality needed here
    }

    public static void main(String[] args) {
        int port = 9880; // Set the port for the WebSocket server
        App app = new App(port);
        app.start();
        System.out.println("WebSocket Server started on port: " + port);
    }
}
