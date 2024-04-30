package uta.cse3310;

import junit.framework.TestCase;

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
}
