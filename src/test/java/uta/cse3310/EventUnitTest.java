//about 95% done by sanjan and 5% kenil
package uta.cse3310;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import org.java_websocket.WebSocket;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;

public class EventUnitTest extends TestCase {

    private Event event;
    private WebSocket mockConnection;
    private List<Player> subLobbyPlayers;

    public EventUnitTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        event = new Event();
        mockConnection = mock(WebSocket.class);
        subLobbyPlayers = new ArrayList<>();
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);

        when(mockPlayer1.getName()).thenReturn("Alice");
        when(mockPlayer1.getConn()).thenReturn(mockConnection);
        when(mockPlayer2.getName()).thenReturn("Bob");
        when(mockPlayer2.getConn()).thenReturn(mockConnection);

        subLobbyPlayers.add(mockPlayer1);
        subLobbyPlayers.add(mockPlayer2);
    }

    public void testLoginSuccess() {
        event.loginSuccess(mockConnection, "Alice");
        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("type", "loginSuccess");
        expectedJson.addProperty("username", "Alice");

        verify(mockConnection).send(expectedJson.toString());
        System.out.println("testLoginSuccess JSON: " + expectedJson);
    }

    public void testLoginError() {
        String errorMessage = "Username already exists";
        event.loginError(mockConnection, errorMessage);
        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("type", "loginError");
        expectedJson.addProperty("message", errorMessage);

        verify(mockConnection).send(expectedJson.toString());
        System.out.println("testLoginError JSON: " + expectedJson);
    }

    public void testJoinedSubLobbySuccess() {
        event.joinedSubLobbySuccess(mockConnection, subLobbyPlayers);

        JsonArray playerArray = new JsonArray();
        for (Player player : subLobbyPlayers) {
            playerArray.add(player.getName());
        }

        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("type", "subLobbySuccess");
        expectedJson.add("players", playerArray);

        verify(mockConnection, times(2)).send(expectedJson.toString());
        System.out.println("testJoinedSubLobbySuccess JSON: " + expectedJson);
    }

    public void testJoinedSubLobbyError() {
        event.joinedSubLobbyError(mockConnection);
        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("type", "subLobbyError");

        verify(mockConnection).send(expectedJson.toString());
        System.out.println("testJoinedSubLobbyError JSON: " + expectedJson);
    }

    public void testSendChatMessage() {
        String message = "Hello everyone!";
        event.sendChatMessage(mockConnection, "Alice", message);
        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("type", "chatMessage");
        expectedJson.addProperty("playerName", "Alice");
        expectedJson.addProperty("message", "Alice: " + message);

        verify(mockConnection).send(expectedJson.toString());
        System.out.println("testSendChatMessage JSON: " + expectedJson);
    }
}
