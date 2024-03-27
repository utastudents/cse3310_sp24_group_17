import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;

class MainLobby extends Player
{
    public static void login() {
        boolean loggingname = false;    // becomes true once Name is created and passes NameChecker
        Player user = new Player();
        // while loop with logging;
            String Name = user.name();
            // if statement that checks if the name entered is empty
            boolean NameChecker = false;    // becomes true once Name is appropriate, unique, and meets length
            int length;                     // minimum number of characters needed for name
            int charcount;                  // number of characters in Name
            int i;
            // for loop with ito count number of characters
            String[] BadWords;              // List of Bad Words
            int N;                          // Number of words in the list
            // while loop with NameChecker
            int j; 
            // for loop with j to check BadWords array
    }
    
    public static int selectLobby() {
      boolean selectedLobby = false;    // Becomes true once a valid sublobby is selected
      // Prompt user to choose a lobby
      Button two = new Button("TwoPlayer");
      Button three = new Button("ThreePlayer");
      Button four = new Button("FourPlayer");
      int SubLobby = 0; // Will either be 2, 3, or 4 (Number of players) based on button press
      // switch cases based on the SubLobby
      // 2 goes to 2PlayerLobby
      // 3 goes to 3PlayerLobby
      // 4 goes to 4PlayerLobby
      // Any other input reprompts the user
      return SubLobby;
      
    }
    
    public static void exitLobby() {
        // Some function to exit lobby
    }
    
    public static void clearLobby() {
        // Some function to remove players and open up the lobby
        System.out.println("Hi");
    }
    
    public int displayScore() {
        Player Scores = new Player();
        // Print out Total Score
        return Scores.TotalScore;
    }
    
    public boolean readyUp() {
        return true;
    } 
}

class TwoPlayerLobby extends MainLobby {
    int MaxPlayersTwo = 2;
    int TwoPlayerCount = 0;
    Button exit2Lobby = new Button("Leave Lobby");
    MainLobby LobbyOptions = new MainLobby();
    
}

class ThreePlayerLobby extends MainLobby {
    int MaxPlayersThree = 3;
    int ThreePlayercount = 0;
    Button exit2Lobby = new Button("Leave Lobby");
    MainLobby LobbyOptions = new MainLobby();
}

class FourPlayerLobby extends MainLobby {
    int MaxPlayersFour = 4;
    int FourPlayerCount = 0;
    Button exit4Lobby = new Button("Leave Lobby");
    MainLobby LobbyOptions = new MainLobby();
}

class Player {
    public String name() {
        System.out.println("Type in a name:");
        Scanner inputname = new Scanner(System.in);
        String username = inputname.nextLine();         // user inputs name
        return username;
    }
    int ColorNum = 0;
    int GameScore = 0; // Game score will update as the player plays a game resets after the game
    int TotalScore = 0; // Total score will update during and after a game
}
