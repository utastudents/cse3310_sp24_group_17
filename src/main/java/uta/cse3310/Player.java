package uta.cse3310;
<<<<<<<<< Temporary merge branch 1

=========
>>>>>>>>> Temporary merge branch 2
import org.java_websocket.WebSocket;

public class Player {
    private int color;
    private String name;
    private int inGameScore;
    private int totalScore;
    private WebSocket conn;

    //Constructor
    public Player(String name, WebSocket conn){
        color = 0;
        this.name = name;
        inGameScore = 0;
        totalScore = 0;
        this.conn = conn;
    }

    //Getters and Setters
    public int getColor(){
        return color;
    }

    public String getName(){
        return name;
    }

    public int getInGameScore(){
        return inGameScore;
    }

    public int getTotalScore(){
        return totalScore;
    }

    public WebSocket getConn(){
        return conn;
    }

    public void setColor(int color){
        this.color = color;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setInGameScore(int inGameScore){
        this.inGameScore = inGameScore;
    }

    public void setTotalScore(int totalScore){
        this.totalScore = totalScore;
    }

    public void setConn(WebSocket conn){
        this.conn = conn;
>>>>>>>>> Temporary merge branch 2
    }
}
