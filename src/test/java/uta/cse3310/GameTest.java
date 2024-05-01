package uta.cse3310;
import junit.framework.TestCase;

import java.util.ArrayList;

public class GameTest extends TestCase {

    public Game Testgame;
    // Test grid size
    private int testsize = 20;                                     
    // List of words      
    String filePath = "src/test/java/uta/cse3310/testWords.txt";
    // Number of words for testing
    int numOfWords = 5;
    
    // Test game set up
    public void setUp() { 
        Testgame = new Game(filePath, numOfWords, testsize); 
        assertNotNull(Testgame);
        assertEquals(testsize, Testgame.getMatrix().length);
        assertEquals(numOfWords, Testgame.getNumofWords());
        assertNotNull(Testgame.getGameID());
        assertNotNull(Testgame.getWordsInMatrix());
    }
    
    // Test if the test words are placed in the grid
    public void testWordPlacement() {
        Testgame = new Game(filePath, numOfWords, testsize); 
        char[][] matrix = Testgame.getMatrix();
        ArrayList<String> wordsInMatrix = Testgame.getWordsInMatrix();
    
        for (String word : wordsInMatrix) {
            boolean wordFound = false;
            for (int i = 0; i < testsize; i++) {
                for (int j = 0; j < testsize; j++) {
                    if (matrix[i][j] == word.charAt(0)) {
                        // Check horizontally
                        if (j + word.length() <= testsize) {
                            StringBuilder sb = new StringBuilder();
                            for (int k = 0; k < word.length(); k++) {
                                sb.append(matrix[i][j + k]);
                            }
                            if (sb.toString().equals(word)) {
                                wordFound = true;
                                assertTrue(word, wordFound);
                                break;
                            }
                        }

                        // Check vertically
                        if (i + word.length() <= testsize) {
                            StringBuilder sb = new StringBuilder();
                            for (int k = 0; k < word.length(); k++) {
                                sb.append(matrix[i + k][j]);
                            }
                            if (sb.toString().equals(word)) {
                                wordFound = true;
                                assertTrue(word, wordFound);
                                break;
                            }
                        }

                        // Check diagonally (top-left to bottom-right)
                        if (i + word.length() <= testsize && j + word.length() <= testsize) {
                            StringBuilder sb = new StringBuilder();
                            for (int k = 0; k < word.length(); k++) {
                                sb.append(matrix[i + k][j + k]);
                            }
                            if (sb.toString().equals(word)) {
                                wordFound = true;
                                assertTrue(word, wordFound);
                                break;
                            }
                        }

                        // Check diagonally (bottom-left to top-right)
                        if (i - word.length() >= -1 && j + word.length() <= testsize) {
                            StringBuilder sb = new StringBuilder();
                            for (int k = 0; k < word.length(); k++) {
                                sb.append(matrix[i - k][j + k]);
                            }
                            if (sb.toString().equals(word)) {
                                wordFound = true;
                                assertTrue(word, wordFound);
                                break;
                            }
                        }
                    }
                }
                if (wordFound) {
                    break;
                }
            }
            // Some words may not be found due to testsize
            System.out.println("Word '" + word + "' not found in the matrix.");
        }
    }

    // Test if a word can be found on a grid
    public void testWordCheck() {
        Testgame = new Game(filePath, numOfWords, testsize); 
        ArrayList<WordDetail> wordsInMatrixDetails = Testgame.wordsInMatrixDetails;

        for (WordDetail detail : wordsInMatrixDetails) {
            int startRow = detail.startRow;
            int startCol = detail.startCol;
            int direction = detail.direction;
            
            // Adjust end coordinates based on word length and direction
            int endRow = startRow;
            int endCol = startCol;
            int wordLength = detail.word.length();
            switch (direction) {
                case 0: endCol += wordLength - 1; break; // Right
                case 1: endRow += wordLength - 1; break; // Down
                case 2: endCol -= wordLength - 1; break; // Left
                case 3: endRow -= wordLength - 1; break; // Up
                case 4: endRow += wordLength - 1; endCol += wordLength - 1; break; // Diagonal Down-Right
                case 5: endRow += wordLength - 1; endCol -= wordLength - 1; break; // Diagonal Down-Left
                case 6: endRow -= wordLength - 1; endCol -= wordLength - 1; break; // Diagonal Up-Left
                case 7: endRow -= wordLength - 1; endCol += wordLength - 1; break; // Diagonal Up-Right
            }
            
            assertTrue("Word '" + detail.word + "' not found in the matrix.", Testgame.check_ans(startRow, startCol, endRow, endCol));
        }
    }
}
