package uta.cse3310;

import java.util.HashMap;
import java.util.Map;

public class LeaderBoard {
    private Map<String, Integer> leaderboard;

    public LeaderBoard() {
     
        this.leaderboard = new HashMap<>();
    }

    public void addToLeaderboard(Player player) {
       
        if (player != null) {
            String playerName = player.getName();
            if (!leaderboard.containsKey(playerName)) {
                leaderboard.put(playerName, 0);
            }
        }
    }

    public void updateScores(Player player, int score) {
     
        if (player != null && score > 0) {
            String playerName = player.getName();
            if (leaderboard.containsKey(playerName)) {
                int currentScore = leaderboard.get(playerName);
                leaderboard.put(playerName, currentScore + score);
            }
        }
    }

    public int getScore(String playerName) {
    
        return leaderboard.getOrDefault(playerName, 0);
    }

    public Map<String, Integer> getLeaderboard() {
       
        return leaderboard;
    }
}
