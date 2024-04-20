package uta.cse3310;

public class Server_Status {
    private int concurrentGames;
    private int totalPlayers;
    private int maxConcurrentGames; 
    private int maxTotalPlayers;

    // Constructor
    public Server_Status(int maxConcurrentGames, int maxTotalPlayers) {
        this.maxConcurrentGames = maxConcurrentGames;
        this.maxTotalPlayers = maxTotalPlayers;
        this.concurrentGames = 0;
        this.totalPlayers = 0;
    }

    
    public void updateStatus(MainLobby lobby) {
       
        //this.concurrentGames = lobby.getConcurrentGames();
        //this.totalPlayers = lobby.getTotalPlayers();
    }

   
    public boolean isMaxGames() {
        return concurrentGames >= maxConcurrentGames;
    }

    public boolean isMaxPlayers() {
        return totalPlayers >= maxTotalPlayers;
    }

   
    public void displayStatus() {
        System.out.println("Server Status:");
        System.out.println("Concurrent Games: " + concurrentGames + "/" + maxConcurrentGames);
        System.out.println("Total Players: " + totalPlayers + "/" + maxTotalPlayers);
    }
}
