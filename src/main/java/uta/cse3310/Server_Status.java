package uta.cse3310;

public class Server_Status {
    private int concurrentGames;
    private int totalPlayers;
    private final int MAX_GAMES = 10; // Example maximum number of concurrent games
    private final int MAX_PLAYERS = 100; // Example maximum number of total players

    public Server_Status() {
        this.concurrentGames = 0;
        this.totalPlayers = 0;
    }

    public void updateStatus(MainLobby lobby) {
        
        this.concurrentGames = lobby.getNumberOfGames();
        this.totalPlayers = lobby.getTotalPlayers();
    }

    public boolean isMaxGames() {
        return concurrentGames >= MAX_GAMES;
    }

    public boolean isMaxPlayers() {
        return totalPlayers >= MAX_PLAYERS;
    }

    public void displayStatus() {
        System.out.println("Server Status:");
        System.out.println("Concurrent Games: " + concurrentGames);
        System.out.println("Total Players: " + totalPlayers);
    }
}
