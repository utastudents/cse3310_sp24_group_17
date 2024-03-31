package uta.cse3310;

import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;

public class MainLobby
{
    public void login() {
        boolean loggingname = false;    // becomes true once Name is created and passes NameChecker
        Player user = new Player();
        // while loop with logging;
            //String Name = user.name();
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
    
    public int selectLobby() {
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
    
    public static void exitLobby(Event event, Player player, SubLobby lobby) {
        // Some function to exit lobby
    }
    
    public static void clearLobby(SubLobby lobby) {
        // Some function to remove players after a game finishes
        System.out.println("Hi");
    }
    
    public void readyUp(Event event, Player Player, SubLobby lobby) {
        // function to let players ready up
    } 

    public boolean isLobbyFullandReady(SubLobby lobby){
        return true;
    }

    void startGame(SubLobby lobby){
        
    }
}

