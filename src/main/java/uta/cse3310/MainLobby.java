package uta.cse3310;

import org.java_websocket.WebSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainLobby {
    private static final int MAX_PLAYERS = 20;
    private List<Player> players = new CopyOnWriteArrayList<>();

    public boolean addPlayerToMainLobby(WebSocket conn, String name) {
        if (players.size() < MAX_PLAYERS) {
            Player player = new Player(name, conn);
            players.add(player);
            broadcastPlayerListUpdate(); // Optional: Notify all players of the new player
            return true;
        } else {
            return false;
        }
    }

    public void removePlayerFromMainLobby(WebSocket conn) {
        Player toRemove = players.stream()
                .filter(player -> player.getPlayerConn().equals(conn))
                .findFirst()
                .orElse(null);
        if (toRemove != null) {
            players.remove(toRemove);
            broadcastPlayerListUpdate(); // Optional: Notify all players of the player removal
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    // Optional: Method to broadcast the current player list to all players
    private void broadcastPlayerListUpdate() {
        String message = "Updated player list: " + players.stream()
                .map(Player::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No players");
        for (Player player : players) {
            player.sendMessage(message);
        }
    }
}
