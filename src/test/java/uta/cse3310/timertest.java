package uta.cse3310;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class LeaderBoardTest {

    private LeaderBoard leaderBoard;
    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    public void setUp() {
        // Initialize the LeaderBoard and players before each test
        leaderBoard = new LeaderBoard();
        player1 = new Player("Alice");
        player2 = new Player("Bob");
        player3 = new Player("Charlie");

        // Add players to the leaderboard
        leaderBoard.addToLeaderboard(player1);
        leaderBoard.addToLeaderboard(player2);
        leaderBoard.addToLeaderboard(player3);
    }

    @Test
    public void testAddToLeaderboard() {
        // Test that players are added to the leaderboard with initial scores of 0
        assertEquals(0, leaderBoard.getLeaderboard().get(0).getValue()); // Alice's score
        assertEquals(0, leaderBoard.getLeaderboard().get(1).getValue()); // Bob's score
        assertEquals(0, leaderBoard.getLeaderboard().get(2).getValue()); // Charlie's score
    }

    @Test
    public void testUpdateScores() {
        // Update scores for players
        leaderBoard.updateScores(player1, 10);
        leaderBoard.updateScores(player2, 15);
        leaderBoard.updateScores(player3, 5);

        // Test that scores are updated correctly
        assertEquals(10, leaderBoard.getLeaderboard().get(0).getValue()); // Alice's score
        assertEquals(15, leaderBoard.getLeaderboard().get(1).getValue()); // Bob's score
        assertEquals(5, leaderBoard.getLeaderboard().get(2).getValue()); // Charlie's score
    }

    @Test
    public void testGetLeaderboard() {
        // Update scores for players
        leaderBoard.updateScores(player1, 15);
        leaderBoard.updateScores(player2, 10);
        leaderBoard.updateScores(player3, 20);

        // Test that leaderboard is sorted by scores in descending order
        List<Map.Entry<String, Integer>> sortedLeaderboard = leaderBoard.getLeaderboard();
        assertEquals("Charlie", sortedLeaderboard.get(0).getKey()); // Charlie should be first with score 20
        assertEquals("Alice", sortedLeaderboard.get(1).getKey()); // Alice should be second with score 15
        assertEquals("Bob", sortedLeaderboard.get(2).getKey()); // Bob should be third with score 10
    }
}
