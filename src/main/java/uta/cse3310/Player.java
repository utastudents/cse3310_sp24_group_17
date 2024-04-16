package uta.cse3310;
import org.java_websocket.WebSocket;

public class Player {
    private String name;
    private int score;
    private WebSocket playerConn;

    public Player(String name, WebSocket newConn) {
        this.name = name;
        this.playerConn = newConn;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else {
            System.out.println("Invalid name provided!");
        }
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int points) {
        if (points > 0) {
            this.score += points;
        } else {
            System.out.println("Attempted to increase score with invalid points: " + points);
        }
    }

    public WebSocket getPlayerConn() {
        return playerConn;
    }

    public void setPlayerConn(WebSocket playerConn) {
        this.playerConn = playerConn;
    }

    public void sendMessage(String message) {
        if (playerConn != null && message != null && !message.isEmpty()) {
            try {
                playerConn.send(message);
            } catch (Exception e) {
                System.out.println("Failed to send message: " + e.getMessage());
            }
        } else {
            System.out.println("Unable to send message due to null connection or empty message.");
        }
    }

    public void resetScore() {
        this.score = 0;
    }

    public void disconnect() {
        if (playerConn != null) {
            try {
                playerConn.close();
            } catch (Exception e) {
                System.out.println("Failed to close WebSocket connection: " + e.getMessage());
            }
        }
    }
}
