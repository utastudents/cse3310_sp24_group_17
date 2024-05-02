package uta.cse3310;

import junit.framework.TestCase;

import java.util.Vector;

import org.java_websocket.WebSocket;
import org.mockito.Mockito;

public class MainLobbyTest extends TestCase {
    // Valid username
    String usernamegood = "Bobby2";    
    // Empty username
    String usernameMT = "";
    // Non-existing username
    String usernameNE = "John";

    // Testing LogIn()
    public void testLogIn() {
        MainLobby mainLobby = new MainLobby();
        // Mock WebSocket connection
        WebSocket mockWebSocket = Mockito.mock(WebSocket.class);

        // Logging in with a valid username
        boolean namecheck = mainLobby.logIn(mockWebSocket, usernamegood);
        assertTrue(namecheck);
        
        // Logging in with an empty username
        boolean MTcheck = mainLobby.logIn(mockWebSocket, usernameMT);
        assertFalse(MTcheck);

        // Logging in with a duplicate username
        boolean namecheckcpy = mainLobby.logIn(mockWebSocket, usernamegood);
        assertFalse(namecheckcpy);
    }

    // Testing LogOff()
    public void testLogOff() {
        MainLobby mainLobby = new MainLobby();
        WebSocket mockWebSocket = Mockito.mock(WebSocket.class);
        
        // Logging off with an existing WebSocket
        mainLobby.logIn(mockWebSocket, usernamegood);
        assertTrue(mainLobby.logOff(mockWebSocket));

        // Logging off with a non-existing WebSocket
        assertFalse(mainLobby.logOff(mockWebSocket));
    }

    // Testing ResetUser()
    public void testResetUser() {
        MainLobby mainLobby = new MainLobby();
        WebSocket mockWebSocket = Mockito.mock(WebSocket.class);
        
        // Resetting an existing user
        mainLobby.logIn(mockWebSocket, usernamegood);
        assertTrue(mainLobby.resetUser(usernamegood));

        // Resetting a non-existing user
        assertFalse(mainLobby.resetUser(usernameNE));
    }

    // Testing findPlayerInMainLobby()
    public void testFindPlayerInMainLobby() {
        MainLobby mainLobby = new MainLobby();
        WebSocket mockWebSocket = Mockito.mock(WebSocket.class);
        
        // Finding a player with an existing WebSocket
        mainLobby.logIn(mockWebSocket, usernamegood);
        assertNotNull(mainLobby.findPlayerInMainLobby(mockWebSocket));

        // Finding a player with a non-existing WebSocket
        assertNull(mainLobby.findPlayerInMainLobby(Mockito.mock(WebSocket.class)));

    }

    // Testing FindPlayerWebSocket()
    public void testFindPlayerWebSocket() {
        MainLobby mainLobby = new MainLobby();
        WebSocket mockWebSocket = Mockito.mock(WebSocket.class);
        
        // Finding a WebSocket with an existing username
        mainLobby.logIn(mockWebSocket, usernamegood);
        assertNotNull(mainLobby.findPlayerWebSocket(usernamegood));

        // Finding a WebSocket with a non-existing username
        assertNull(mainLobby.findPlayerWebSocket(usernameNE));
    }

    // Testing removefromSubLobby()
    public void testremovefromSubLobby() {
        MainLobby mainLobby = new MainLobby();
        Vector<SubLobby> activeGames = new Vector<>();
        
        WebSocket mockWebSocket1 = Mockito.mock(WebSocket.class);
        WebSocket mockWebSocket2 = Mockito.mock(WebSocket.class);
        WebSocket mockWebSocket3 = Mockito.mock(WebSocket.class);

        Player player1 = new Player("Player1",mockWebSocket1);
        Player player2 = new Player("Player2",mockWebSocket2);
        Player player3 = new Player("Player3",mockWebSocket3);

        SubLobby subLobby1 = new SubLobby(2, player1);
        SubLobby subLobby2 = new SubLobby(3, player3);

        // Add the player to the SubLobby
        subLobby1.addPlayer(player2);
 
        // Add sample Sublobbies to activeGames
        activeGames.add(subLobby1);
        activeGames.add(subLobby2);
 
        // Call the removeFromSubLobby method with the sample ActiveGames and WebSocket connection
        mainLobby.removeFromSubLobby(activeGames, mockWebSocket2);
 
        // Assert that the player has been removed from the SubLobby
        assertFalse(subLobby1.getPlayers().contains(player2));
     }
}
