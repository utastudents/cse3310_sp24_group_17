package uta.cse3310;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class LeaderBoardTest {

    private LeaderBoard leaderBoard;

    @BeforeEach
    public void setUp() {
        // Initialize the LeaderBoard before each test
        leaderBoard = new LeaderBoard();
    }

    @Test
    public void testAddToLeaderboard() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Player player3 = new Player("Player3");

        // Add players to the leaderboard
        leaderBoard.addToLeaderboard(player1);
        leaderBoard.addToLeaderboard(player2);
        leaderBoard.addToLeaderboard(player3);

        // Check that the players are added with initial scores of 0
        assertEquals(0, leaderBoard.getScore(player1)); // Player1's score
        assertEquals(0, leaderBoard.getScore(player2)); // Player2's score
        assertEquals(0, leaderBoard.getScore(player3)); // Player3's score
    }

    @Test
    public void testUpdateScores() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Player player3 = new Player("Player3");

        leaderBoard.addToLeaderboard(player1);
        leaderBoard.addToLeaderboard(player2);
        leaderBoard.addToLeaderboard(player3);

        // Update scores for players
        leaderBoard.updateScores(player1, 10);
        leaderBoard.updateScores(player2, 15);
        leaderBoard.updateScores(player3, 5);

        // Test that scores are updated correctly
        assertEquals(10, leaderBoard.getScore(player1));
        assertEquals(15, leaderBoard.getScore(player2));
        assertEquals(5, leaderBoard.getScore(player3));
    }

    @Test
    public void testGetLeaderboard() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Player player3 = new Player("Player3");

        leaderBoard.addToLeaderboard(player1);
        leaderBoard.addToLeaderboard(player2);
        leaderBoard.addToLeaderboard(player3);

        leaderBoard.updateScores(player1, 15);
        leaderBoard.updateScores(player2, 10);
        leaderBoard.updateScores(player3, 20);

        // Test that leaderboard is sorted by scores in descending order
        List<Map.Entry<Player, Integer>> sortedLeaderboard = leaderBoard.getLeaderboard();
        assertEquals(player3, sortedLeaderboard.get(0).getKey()); // Player3 should be first with score 20
        assertEquals(player1, sortedLeaderboard.get(1).getKey()); // Player1 should be second with score 15
        assertEquals(player2, sortedLeaderboard.get(2).getKey()); // Player2 should be third with score 10
    }
}
