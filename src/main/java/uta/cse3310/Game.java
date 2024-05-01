package uta.cse3310;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class WordDetail {
    String word;
    int startRow;
    int startCol;
    int direction; // This can be used to retrieve more details about how the word is placed.

    public WordDetail(String word, int startRow, int startCol, int direction) {
        this.word = word;
        this.startRow = startRow;
        this.startCol = startCol;
        this.direction = direction;
    }
}

class Game {
    private int MAX;
    private static final Random random = new Random();
    private int gameID;
    private char[][] wordMatrix;
    private List<String> wordsList;
    private ArrayList<String> wordsInMatrix;
    public ArrayList<WordDetail> wordsInMatrixDetails;
    private int currentIndex = 0;

    public Game(String filePath, int numOfWords, int gridSize) {
        this.MAX = gridSize;
        this.wordMatrix = new char[MAX][MAX];
        this.wordsList = new ArrayList<>();
        this.wordsInMatrix = new ArrayList<>();
        this.wordsInMatrixDetails = new ArrayList<>();
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
                      .map(String::toUpperCase)
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
            int row = 0, col = 0, direction = 0;
            for (int attempts = 0; attempts < 100 && !placed; attempts++) {
                row = random.nextInt(MAX);
                col = random.nextInt(MAX);
                direction = random.nextInt(8);
                placed = tryPlaceWord(word, row, col, direction);
                if (placed) {
                    wordsInMatrix.add(word);
                    wordsInMatrixDetails.add(new WordDetail(word, row, col, direction));
                }
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

    public int[] getRandomWordCoordinates() {
        if (wordsInMatrixDetails.isEmpty()) {
            return null; 
        }
        
        WordDetail selected = wordsInMatrixDetails.get(currentIndex);
        int[] coordinates = new int[]{selected.startRow, selected.startCol};

        currentIndex++;  // Move to the next index
        if (currentIndex >= wordsInMatrixDetails.size()) {
            currentIndex = 0;  // Reset to start if reached the end of the list
        }

        return coordinates;
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

    public ArrayList<String> getWordsInMatrix(){
        return wordsInMatrix;
    }
    public char[][] getMatrix(){
        return wordMatrix;
    }
    
    //d

}



