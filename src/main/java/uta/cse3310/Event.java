package uta.cse3310;

public class Event {
    private int Game_ID;
    private String User_ID;
    private int button;

    // Constructor
    public Event(int game_ID, String user_ID, int button) {
        this.Game_ID = game_ID;
        this.User_ID = user_ID;
        this.button = button;
    }

    // Getter for Game_ID
    public int getGame_ID() {
        return Game_ID;
    }

    // Setter for Game_ID
    public void setGame_ID(int game_ID) {
        this.Game_ID = game_ID;
    }

    // Getter for User_ID
    public String getUser_ID() {
        return User_ID;
    }

    // Setter for User_ID
    public void setUser_ID(String user_ID) {
        this.User_ID = user_ID;
    }

    // Getter for button
    public int getButton() {
        return button;
    }

    // Setter for button
    public void setButton(int button) {
        this.button = button;
    }
}
