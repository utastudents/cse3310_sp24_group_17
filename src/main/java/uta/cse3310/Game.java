package uta.cse3310;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.PublicKey;

class Game {
    private  int MAX = 50;
    private  static final Random random = new Random();
    private int gameID;
    private char[][] wordMatrix = new char[MAX][MAX];
    private List<String> wordsList = new ArrayList<>();
    private ArrayList<String> wordsInMatrix = new ArrayList<>();
    
     

    public Game(String filePath, int numOfWords,int gridSize) {
        this.MAX=gridSize;
        this.gameID = random.nextInt(1000);
        initializeMatrix();
        try {
            readWordsFromFile(filePath, numOfWords);
            
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        placeWords();
        fillRandom();
    }
    public Game(String filePath, int numOfWords,int gridSize,int x) {
        this.MAX=gridSize;
        this.gameID = random.nextInt(1000);
        initializeMatrix();
        try {
            readWordsFromFile(filePath, numOfWords);
            
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } 
    }

    public void initializwithdots(){
        initializeMatrix();

    }
    public void fillWithwords (){
        placeWords();
    }
    public void fillWithrandom(){
        fillRandom();
    }
    public char[][] getMatrix(){
        return wordMatrix;
    }

    public void initializeMatrix() {
        for (int i = 0; i < MAX; i++) {
            Arrays.fill(wordMatrix[i], '.');
        }
    }
    

    private void readWordsFromFile(String filePath, int numOfWords) throws IOException {
        List<String> allWords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Arrays.stream(line.split("\\s+"))
                      .filter(word -> word.length() >= 4 && word.length() <= 10)
                      .map(String::toUpperCase) // Convert each word to uppercase
                      .forEach(allWords::add);
            }
        }
    
        Collections.shuffle(allWords);
    
        if (allWords.size() > numOfWords) {
            wordsList = allWords.subList(0, numOfWords);
        } else {
            wordsList = allWords;
        }
    }
    
    

    public void placeWords() {
        for (String word : wordsList) {
            boolean placed = false;
            for (int attempts = 0; attempts < 100 && !placed; attempts++) {
                int row = random.nextInt(MAX);
                int col = random.nextInt(MAX);
                int direction = random.nextInt(8); // 0-7 for eight possible directions
                placed = tryPlaceWord(word, row, col, direction);
            }
            if (!placed) {
                //System.out.println("Failed to place: " + word);
            }
            else{
                wordsInMatrix.add(word);
            }
        }
    }

    private boolean tryPlaceWord(String word, int startRow, int startCol, int direction) {
        int dRow = 0, dCol = 0;
        switch (direction) {
            case 0: dRow = 0; dCol = 1; break;  // Right
            case 1: dRow = 1; dCol = 0; break;  // Down
            case 2: dRow = 0; dCol = -1; break; // Left
            case 3: dRow = -1; dCol = 0; break; // Up
            case 4: dRow = 1; dCol = 1; break;  // Diagonal Down-Right
            case 5: dRow = 1; dCol = -1; break; // Diagonal Down-Left
            case 6: dRow = -1; dCol = -1; break; // Diagonal Up-Left
            case 7: dRow = -1; dCol = 1; break; // Diagonal Up-Right
        }
        int row = startRow, col = startCol;
        for (int i = 0; i < word.length(); i++) {
            if (!isWithinBounds(row, col) || (wordMatrix[row][col] != '.' && wordMatrix[row][col] != word.charAt(i))) {
                return false;
            }
            row += dRow;
            col += dCol;
        }
        row = startRow;
        col = startCol;
        for (int i = 0; i < word.length(); i++) {
            wordMatrix[row][col] = word.charAt(i);
            row += dRow;
            col += dCol;
        }
        return true;
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < MAX && col >= 0 && col < MAX;
    }

    private void printMatrix() {
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                System.out.print(wordMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    private int[] getCoordinates(int startR, int startC, int endR,int endC) {
        int[] ans = {0, 0, 0, 0}; 
        ans[0] = startR;    
        ans[1] = startC;    
        ans[2] = endR;   
        ans[3] = endC;    
        return ans;
    }
    private boolean mapword(int direction, int startRow, int startCol, int endRow, int endCol) {
        StringBuilder capture = new StringBuilder();
        
        switch (direction) {
            case 0: 
                for (int i = startCol; i <= endCol; i++) {
                    capture.append(wordMatrix[startRow][i]);
                }
                break;
            case 1: 
                for (int i = startCol; i >= endCol; i--) {
                    capture.append(wordMatrix[startRow][i]);
                }
                break;
            case 2: 
                for (int i = startRow; i <= endRow; i++) {
                    capture.append(wordMatrix[i][startCol]);
                }
                break;
            case 3: 
                for (int i = startRow; i >= endRow; i--) {
                    capture.append(wordMatrix[i][startCol]);
                }
                break;
            case 4: 
                for (int r = startRow, c = startCol; r <= endRow && c <= endCol; r++, c++) {
                    capture.append(wordMatrix[r][c]);
                }
                break;
            case 5: 
                for (int r = startRow, c = startCol; r <= endRow && c >= endCol; r++, c--) {
                    capture.append(wordMatrix[r][c]);
                }
                break;
            case 6: 
                for (int r = startRow, c = startCol; r >= endRow && c >= endCol; r--, c--) {
                    capture.append(wordMatrix[r][c]);
                }
                break;
            case 7: 
                for (int r = startRow, c = startCol; r >= endRow && c <= endCol; r--, c++) {
                    capture.append(wordMatrix[r][c]);
                }
                break;
        }
        return wordsInMatrix.contains(capture.toString());
    }
    
    public boolean check_ans(int startR, int startC, int endR,int endC) {
        int[] coordinates = getCoordinates(startR, startC, endR, endC);
    
        int startRow = coordinates[0];
        int startCol = coordinates[1];
        int endRow = coordinates[2];
        int endCol = coordinates[3];
    
        if (endRow == startRow && endCol > startCol) {  
            return mapword(0, startRow, startCol, endRow, endCol);
        }
        else if (endRow == startRow && endCol < startCol) {  
            return mapword(1, startRow, startCol, endRow, endCol);
        } 
        else if (endRow > startRow && endCol == startCol) { 
            return mapword(2, startRow, startCol, endRow, endCol);
        } 
        else if (endRow < startRow && endCol == startCol) { 
            return mapword(3, startRow, startCol, endRow, endCol);
        } 
        else if (endRow > startRow && endCol > startCol) {  
            return mapword(4, startRow, startCol, endRow, endCol);
        } 
        else if (endRow > startRow && endCol < startCol) {  
            return mapword(5, startRow, startCol, endRow, endCol);
        } 
        else if (endRow < startRow && endCol < startCol) {  
            return mapword(6, startRow, startCol, endRow, endCol);
        } 
        else if (endRow < startRow && endCol > startCol) {  
            return mapword(7, startRow, startCol, endRow, endCol);
        }
        return false;
    }
    public void fillRandom(){
        Random random = new Random();
        for(int i = 0; i < MAX; i++){
            for(int j = 0; j < MAX; j++){
                if(wordMatrix[i][j] == '.'){  
                    wordMatrix[i][j] = (char) ('A' + random.nextInt(26));
                }
            }
        }
    }
    public int getNumofWords(){
        return wordsInMatrix.size();
    }
    public int getGameID(){
        return this.gameID;
    }

    // need to change how the data is displayed
    public String getGridAndWordsAsJson() {
        JsonObject gridJson = new JsonObject();
        JsonArray rows = new JsonArray();
        JsonArray wordsJson = new JsonArray();
    
        for (char[] row : wordMatrix) {
            rows.add(new String(row));
        }
        for (String word : wordsInMatrix) {
            wordsJson.add(word);
        }
        gridJson.addProperty("type", "StartGame");
        gridJson.add("grid", rows);
        gridJson.add("words", wordsJson);
        return gridJson.toString();
    }
    //d


}



