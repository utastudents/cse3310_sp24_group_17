package uta.cse3310;

import org.java_websocket.WebSocket;

public class Player {
    private String color;
    private String name;
    private int inGameScore;
    private int totalScore;
    private WebSocket conn;
    private boolean isReady = false;

    // Constructor
    public Player(String name, WebSocket conn) {
        this.color = null;
        this.name = name;
        this.inGameScore = 0;
        this.totalScore = 0;
        this.conn = conn;
    }

    // Getters and Setters
    public String getColor() {
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

    public boolean isReady() {
        return isReady;
    }

    public void setColor(String color) {
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

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }
}
