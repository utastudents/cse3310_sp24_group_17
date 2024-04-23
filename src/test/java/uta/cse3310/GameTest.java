package uta.cse3310;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;


public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game("testWords.txt", 5, 10);
    }

    @Test
    public void testMatrixInitialization() {
        char[][] matrix = game.getMatrix();
        for (char[] row : matrix) {
            for (char cell : row) {
                assertEquals('.', cell);
            }
        }
    }

    @Test
    public void testWordPlacement() {
        game.placeWords();
        assertTrue("Expected at least one word placed", game.getNumofWords() > 0);
    }

    @Test
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

    @Test
    public void testCheckAnswer() {
        assertTrue("Word should be found", game.check_ans(0, 0, 0, 3));
        assertFalse("Word should not be found", game.check_ans(0, 0, 5, 5));
    }
}
