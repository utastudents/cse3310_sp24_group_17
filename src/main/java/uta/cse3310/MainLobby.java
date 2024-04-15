package uta.cse3310;

import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainLobby {
    int gameID, numberofPlayers;
    public void login() {
        boolean logging = false;                                                            // Becomes true once Name is created and passes NameCheckers
        while(logging != true) {
            Player user = new Player();
            String Name = user.name();                                                      // User inputs name
            boolean MTChecker = false;                                                      // Checks if Name is empty
            if (Name.isEmpty()) {                                                        
                System.out.println("No characters detected");
                MTChecker = true;
            }
            boolean LengthChecker = true;                                                   // Checks if Name is long
            int length = 8;
            int charcount = 0;
            for (int i = 0; i < Name.length(); i++) {                                       // Counts number of characters in name
                charcount++;
            }
            if (charcount < length) {
                System.out.println("Username is not long enough");
                LengthChecker = false;
            }
            boolean LanguageChecker = true;                                                 // Checks if Name contains inappropriate words
            while(LanguageChecker != false) {
                String[] BadWords = {"fuck", "shit", "bitch", "ass", "dick", "penis"};      // List of Bad Words 
                int N = 6;                                                                  // Number of words in the list
                String lowername = Name.toLowerCase();
                for (int j = 0; j < N; j++) {                        
                    if(lowername.contains(BadWords[j])) {
                        System.out.println("Name contains an inappropriate word");
                        LanguageChecker = false;
                        break;
                    }
                }
                break;
                
            }
        if (MTChecker == true || LengthChecker == false || LanguageChecker == false) {      // If Name fails any checkers, user has to enter another name
            System.out.println("Username is invalid, Please try again");
        }
        if (MTChecker == false && LengthChecker == true && LanguageChecker == true) {       // If Name passe
            System.out.println("Username is valid");
            logging = true;
        }
        }
    }
    
    public int selectLobby() {
      boolean selectedLobby = false;    // Becomes true once a valid sublobby is selected
      // Prompt user to choose a lobby
      Button two = new Button("TwoPlayer");
      Button three = new Button("ThreePlayer");
      Button four = new Button("FourPlayer");
      int SubLobby = 0; // Will either be 2, 3, or 4 (Number of players) based on button press
      while (selectedLobby != true) {
        switch(sublobby) {
            case 2:
                System.out.println("Entering 2 Player sublobby...");
                selectedLobby = true;
                break;
            case 3:
                System.out.println("Entering 3 Player sublobby...");
                selectedLobby = true;
                break;
            case 4:
                System.out.println("Entering 4 Player sublobby...");
                selectedLobby = true;
                break;
            default:
                System.out.println("Not a valid response, Please try again");
                break;
        }
      }
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

