package uta.cse3310;
import junit.framework.TestCase;
import java.util.Arrays;
import java.util.Random;

public class GameTest extends TestCase {
    private Game game;

    public void setUp() {
        Random fixedRandom = new Random(42); 
        game = new Game("testWords.txt" ,5, 10, fixedRandom);
        game.fillWithwords();
        
    }

    public void testMatrixInitialization() {
        char[][] matrix = game.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                assertEquals("Matrix should be initialized with dots.", '.', matrix[i][j]);
            }
        }
    }

    public void testWordPlacement() {
        
        game.fillWithwords();
        assertTrue("Expected at least one word to be placed", game.getNumofWords() > 0);
    }

    public void testFillRandom() {
        game.fillWithrandom();
        char[][] matrix = game.getMatrix();
        boolean hasRandomLetters = false;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != '.') {
                    hasRandomLetters = true;
                    assertTrue("Character must be a letter", Character.isLetter(matrix[i][j]));
                }
            }
        }
        assertTrue("Matrix should contain random letters", hasRandomLetters);
    }

   
}
