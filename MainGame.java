import java.util.Random;
import java.util.Scanner;

public class MainGame {
    private static final int SIZE = 4;
    private static final int WINNING_TITLE = 2048;
    private static int[][] board;
    private static int score;
    private static boolean gameOver;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the 2048 Game");
        System.out.println("Use the following keys to move: ");
        System.out.println("w - Up");
        System.out.println("s - Down");
        System.out.println("a - Left");
        System.out.println("d - Right");
        System.out.println("Try to reach " + WINNING_TITLE + " to win!");
        
        do {
            initializeBoard(); // Initialize the game board
            printBoard(); // Print the initial game board

            while (!gameOver) {
                moveTiles(); // Allow the user to move the tiles
                generateNewTileOptimized(); // Generate a new tile with higher probability of a match
                printBoard(); // Print the updated game board
            }

            if (isBoardFull()) {
                System.out.println("Game Over!");
            } else if (checkForWin()) {
                System.out.println("Congratulations! You won!");
            }

            System.out.println("Your Score: " + score);

            System.out.print("Do you want to replay the game? (yes/no): ");
            String replayChoice = scanner.nextLine().toLowerCase();

            if (!replayChoice.equals("yes")) {
                System.out.println("Thank you for playing the 2048 Game. GoodBye!");
                break;
            }
        }
    }
}