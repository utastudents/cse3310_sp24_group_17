package uta.cse3310;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class SubLobbyTest extends TestCase {

    private SubLobby subLobby;
    private Player player1;
    private Player player2;

    // Setting up Test variables
    public void setUp() {
        player1 = new Player("Alice", null);
        player2 = new Player("Bob", null);
        subLobby = new SubLobby(2, player1);
    }

    // Testing if a player is added
    public void testAddPlayer() {
        subLobby.addPlayer(player2);
        List<Player> players = subLobby.getPlayers();
        assertTrue(players.contains(player2));
    }

    // Testing if a player is removed
    public void testRemovePlayer() {
        subLobby.addPlayer(player2);
        subLobby.removePlayer(player2);
        List<Player> players = subLobby.getPlayers();
        assertFalse(players.contains(player2));
    }

    // Test checking if SubLobby is full
    public void testIsSubLobbyFull() {
        assertFalse(subLobby.isSubLobbyFull());
        subLobby.addPlayer(player2);
        assertTrue(subLobby.isSubLobbyFull());
    }

    public void testCreateOrJoinSubLobby() {
        Vector<SubLobby> activeGames = new Vector<>();
        
        // Test if player is added to new sublobby
        SubLobby newSubLobby = SubLobby.createOrJoinSubLobby(2, activeGames, player2);
        assertNotNull(newSubLobby);
        assertEquals(1, activeGames.size());
        assertEquals(1, activeGames.get(0).getPlayers().size());

        // Test if player is added to an existing sublobby
        SubLobby existingSubLobby = SubLobby.createOrJoinSubLobby(2, activeGames, player1);
        assertNotNull(existingSubLobby);
        assertEquals(1, activeGames.size());
        assertEquals(2, activeGames.get(0).getPlayers().size());

        // Test if max concurrent games limit is reached
        for (int i = 1; i < SubLobby.MAX_ACTIVE_GAMES; i++) {
            activeGames.add(new SubLobby(2, new Player("Player" + i, null)));
        }
        SubLobby fullSubLobby = SubLobby.createOrJoinSubLobby(2, activeGames, new Player("Player", null));
        assertNotNull(fullSubLobby);
        assertEquals(SubLobby.MAX_ACTIVE_GAMES, activeGames.size());
    }

    // Testing if names can be read
    public void testGetPlayerNames() {
        subLobby.addPlayer(player2);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Alice");
        expectedNames.add("Bob");
        assertEquals(expectedNames, subLobby.getPlayerNames());
    }

    // Testing if all players can ready up
    public void testAllPlayersReady() {
        assertFalse(subLobby.allPlayersReady());
        player1.setReady(true);
        assertTrue(subLobby.allPlayersReady());
    }
/*
    // Test if players in a lobby are assigned a color
    public void testReassignColors() {

        subLobby.addPlayer(player2);
        subLobby.addPlayer(player3);
        subLobby.addPlayer(player4);

        subLobby.reassignColors(subLobby);

        // Verify that each player has been assigned a color
        assertEquals("blue", player1.getColor());
        assertEquals("pink", player2.getColor());
        assertEquals("green", player3.getColor());
        assertEquals("orange", player4.getColor());
    }
*/
}
