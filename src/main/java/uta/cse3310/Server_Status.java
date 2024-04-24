package uta.cse3310;

import org.java_websocket.WebSocket;

public class Player {
    private int color;
    private String name;
    private int inGameScore;
    private int totalScore;
    private WebSocket conn;

    // Constructor
    public Player(String name, WebSocket conn) {
        this.color = 0;  // Default color index, you might want to change how color is managed based on your game logic
        this.name = name;
        this.inGameScore = 0;
        this.totalScore = 0;
        this.conn = conn;
    }

    // Getters and Setters
    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getInGameScore() {
        return inGameScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public WebSocket getConn() {
        return conn;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInGameScore(int inGameScore) {
        this.inGameScore = inGameScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setConn(WebSocket conn) {
        this.conn = conn;
    }

    // Method to send a message through the WebSocket connection
    public void sendMessage(String message) {
        if (this.conn != null && this.conn.isOpen()) {
            this.conn.send(message);
        } else {
            System.out.println("Failed to send message, connection is closed or null.");
        }
    }
}
