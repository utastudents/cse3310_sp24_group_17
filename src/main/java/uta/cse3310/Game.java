package uta.cse3310;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Game{
public static final int MAX = 50;
private int GameID = 0;
private String[][] word_matrix = new String[MAX][MAX];
private List<String> wordsList = new ArrayList<>();
private String Current_word;
private int vertical = 0;
private int horizontal = 0;
private int button = 0;
private int Start_x[];
private int Start_y[];
private int End_x[];
private int End_y[];

public Game(String filePath, int numOfWords) {
    
}
public void set_button(int value) {
    
}
private int botton_to_coordinate(int botton) { return 0;
}
public boolean is_correct(int button){
  return true;  
}
private boolean check_horizontal_Placement_LT(char word, String word_matrix[][], int horizontal,int vertical) { 
    return true; 
    
}
private boolean check_horizontal_Placement_RT(char word, String word_matrix[][], int horizontal,int vertical) { 
    return true; 
    
}
private boolean check_vertical_Placement_DWN(char word, String word_matrix[][], int horizontal,int vertical) { 
    return true; 
    
}
private boolean check_vertical_Placement_UP(char word, String word_matrix[][], int horizontal,int vertical) {
    return true; 
    
}
private boolean check_diagonal_Placement_DDR(char word, String word_matrix[][], int horizontal,int vertical) { 
    return true; 
    
}
private boolean check_diagonal_Placement_DDL(char word, String word_matrix[][], int horizontal,int vertical) { 
    return true; 
    
}
private boolean check_diagonal_Placement_DUL(char word, String word_matrix[][], int horizontal,int vertical) { 
    return true;
}
private boolean check_diagonal_Placement_DUR(char word, String word_matrix[][], int horizontal,int vertical) { 
    return true; 
    
}
private void Place_horizontal_Placement_LT(char word, String word_matrix[][], int horizontal,int vertical) {
    
}
private void Place_horizontal_Placement_RT(char word, String word_matrix[][], int horizontal,int vertical) {
    
}
private void Place_vertical_Placement_DWN(char word, String word_matrix[][], int horizontal,int vertical) {
    
}
private void Place_vertical_Placement_UP(char word, String word_matrix[][], int horizontal,int vertical) {
    
}
private void Place_diagonal_Placement_DDR(char word, String word_matrix[][], int horizontal,int vertical) {
    
}
private void Place_diagonal_Placement_DDL(char word, String word_matrix[][], int horizontal,int vertical) {
    
}
private void Place_diagonal_Placement_DUL(char word, String word_matrix[][], int horizontal,int vertical) {
    
}
private void Place_diagonal_Placement_DUR(char word, String word_matrix[][], int horizontal,int vertical) {
    
}
public static void create_game(String [][]word_matrix) {
    
}
private int get_random_location(int x) { 
    return 0;
    
}
private int generateGameID() { 
    return 0; 
    
}
private void fill_matrix(String[][] word_matrix) {
    
}
private void readWordsFromFile(String filePath, int numOfWords, List<String> wordsList) throws IOException {
    
}
private boolean check_overflow() { 
    return true; 
    
}
public int getGameID() { return GameID;
}
public String[][] getWordMatrix() { return word_matrix; 
    
}

void on_exit_game(){

}

boolean isGameOver(){
    return true;
}
}

