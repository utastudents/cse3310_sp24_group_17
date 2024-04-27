// package uta.cse3310;
// import junit.framework.TestCase;

// import java.io.IOException;
// import java.util.Arrays;
// import java.util.Random;

// public class GameTest extends TestCase {

//     public Game game;
//     private int max=50;
//     public void setUp() {
//         game = new Game ("src\\main\\java\\uta\\cse3310\\wordsNew.txt", 5, max,1); 
//     }
    

//     public void testMatrixInitialization() {
//         game.initializwithdots();
//         char[][] matrix = game.getMatrix();
//         for (int i = 0; i < max; i++) {
//             for (int j = 0; j < max; j++) {
                
//                 assertEquals('.',matrix[i][j]);
//             }
//     }
// }
    
//     public void testWordPlacement() {
        
//         game.fillWithwords();
//         assertTrue("Expected at least one word to be placed", game.getNumofWords() > 0);
//     }
   

// }
