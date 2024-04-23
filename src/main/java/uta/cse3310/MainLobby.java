package uta.cse3310;

import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import org.java_websocket.WebSocket;

public class MainLobby { /* 
    int gameID, numberofPlayers;
    public void login() {
        boolean logging = false;                                                            // Becomes true once Name is created and passes NameCheckers
        while(logging != true) {
            Player user = new Player();
            System.out.println("Enter your name:");
            Scanner inputname = new Scanner(System.in);
            String Name = inputname.nextLine();                                             // User inputs Name
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
        if (MTChecker == false && LengthChecker == true && LanguageChecker == true) {       // If Name passes, Name is given to the Player class for the user
            System.out.println("Username is valid");
            user.name = Name;
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
        switch(SubLobby) {
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
        // function to let player exit lobby
        Button exit = new Button("Exit");
        boolean exitstatus = false;
        while (exitstatus != false) {
            if (exit) {
                System.out.println("Exiting sublobby...");
                exitstatus = true;
            }
        exitstatus = false;
    }
    
    public static void clearLobby(SubLobby lobby) {
        // Some function to remove players after a game finishes
        System.out.println("Hi");
    }
    
    public void readyUp(Event event, Player Player, SubLobby lobby) {
        // function to let players ready up
        Button Ready = new Button("Ready");
        boolean readystatus = false;
        while (readystatus != false) {
            if (Ready) {
                System.out.println("Status: READY");
                readystatus = true;
            }
        }
        while (readystatus != true) {
            if (Ready) {
                System.out.println("STATUS: NOT READY");
                readystatus = false;
            }
        }
    } 

    public boolean isLobbyFullandReady(SubLobby lobby){
        return true;
    }

    void startGame(SubLobby lobby){
        
    } */
    private static final int MAX_PLAYERS = 20;
    private ArrayList<Player> players = new ArrayList<>();


    // add new players to main lobby
    public boolean logIn(WebSocket conn, String name){
        //check for unique username
        for(Player player : players){
            if(player.getName() == name){
                return false;
            }
        }

        if(players.size() < MAX_PLAYERS){
            Player player = new Player(name, conn);
            players.add(player);
            return true;    //if # of players is not max, create player
        }
        else{
            return false;   // if # of players is max, don't create player
        }
    }

    public void logOff(WebSocket conn){
        Player remove = null;
        for(Player player : players){
            if(player.getConn() == conn){
                remove = player;
                break;
            }
        }
        if(remove != null){
            players.remove(remove);
        }
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }
}

