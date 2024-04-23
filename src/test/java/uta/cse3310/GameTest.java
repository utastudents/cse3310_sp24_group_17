package uta.cse3310;
import junit.framework.TestCase;
import java.util.Arrays;

public class GameTest extends TestCase {
    private Game game;

    public void setUp() {
        
        game = new Game("dummyPath", 5, 10);
        game.initializeMatrix();
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
        
        game.placeWords();
        assertTrue("Expected at least one word to be placed", game.getNumofWords() > 0);
    }

    public void testFillRandom() {
        game.fillRandom();
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

    public void testCheckAnswer() {
        
        assertTrue("Word should be found at the specified coordinates", game.check_ans(0, 0, 0, 3));
        assertFalse("No word should match these coordinates", game.check_ans(0, 0, 5, 5));
    }
}
