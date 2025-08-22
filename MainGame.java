import java.util.Random;
import java.util.Scanner;

public class MainGame {
    private static final int SIZE = 4;
    private static final int WINNING_TILE = 2048;
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
        System.out.println("Try to reach " + WINNING_TILE + " to win!");
        
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
        } while (true);
    }

    private static void initializeBoard() {
        board = new int[SIZE][SIZE]; // Create the game board
        score = 0; // Initialize the score
        gameOver = false; // Set the game over flag to false

        generateNewTile(); // Generate the initial tiles
        generateNewTile();
    }

    private static void printBoard() {
        System.out.println("Score: " + score);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + "\t");
            }
            System.out.println();
        }
    }

    private static void generateNewTile() {
        Random random = new Random();
        int value = random.nextInt(10) < 9 ? 2 : 4; // Generate a new tile value (90% chance of 2, 10% chance of 4)

        while (true) {
            int row = random.nextInt(SIZE); // Generate random row and comlumn indices
            int col = random.nextInt(SIZE);

            if (board[row][col] == 0) { // Check if the cell is empty
                board[row][col] = value; // Place the new tile in the empty cell
                break;
            }
        }
    }

    private static void generateNewTileOptimized() {
        Random random = new Random();
        int value = random.nextInt(10) < 9 ? 2 : 4; // Generate a new tile value (90% chance of 2, 10% chance of 4)

        // Find all empty cells with neighboring tiles of the same value
        int[] emptyRow = new int[SIZE * SIZE];
        int[] emptyCol = new int[SIZE * SIZE];
        int emptyCount = 0;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    if ((row > 0 && board[row - 1][col] == value)
                            || (row < SIZE - 1 && board[row + 1][col] == value)
                            || (col > 0 && board[row][col - 1] == value)
                            || (col < SIZE - 1 && board[row][col + 1] == value)) {
                        emptyRow[emptyCount] = row;
                        emptyCol[emptyCount] = col;
                        emptyCount++;
                    }
                }
            }
        }

        if (emptyCount > 0) {
            int index = random.nextInt(emptyCount);
            int row = emptyRow[index];
            int col = emptyCol[index];
            board[row][col] = value;
        } else {
            // If there are no empty cells with neighboring tiles of the same value,
            // generate the new tile in any random empty cell.
            while (true) {
                int row = random.nextInt(SIZE);
                int col = random.nextInt(SIZE);

                if (board[row][col] == 0) {
                    board[row][col] = value;
                    break;
                }
            }
        }
    }

    private static void moveTiles() {
        Scanner scanner = new Scanner(System.in);
        String direction;

        do {
            direction = scanner.nextLine().toLowerCase(); // Get the user's move direction

            switch (direction) {
                case "w":
                    moveUp(); // Move the tiles upwards
                    break;
                case "s":
                    moveDown(); // Move the tiles downwards
                    break;
                case "a":
                    moveLeft(); // Move the tiles to the left
                    break;
                case "d":
                    moveRight(); // Move the tile to the right
                    break;
            }
        } while (!direction.equals("w") && !direction.equals("s") && !direction.equals("a") && !direction.equals("d"));

        if (!gameOver) {
            if (isBoardFull() && !hasValidMoves()) {
                gameOver =  true; // Set the game over flag if the board is full and no more valid moves can be made
                System.out.println("No more valid moves. Game Over!");

                System.out.print("Do you want to try again? yes/no: ");
                String tryAgainChoice = scanner.nextLine().toLowerCase();

                if (tryAgainChoice.equals("yes")) {
                    initializeBoard(); // Reset the game board and starta new round
                    printBoard();
                    gameOver =  false; // Reset the game over flag for a new game
                } else {
                    System.out.println("Thank you for playing the 2048 Game");
                }
            }
        }
    }

    private static boolean hasValidMoves() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    return true; // If there is an empty cell, there is a valid move
                }
                if (row < SIZE - 1 && board[row][col] == board[row + 1][col]) {
                    return true; // If there are adjacent cells with the same value, there is a valid move
                }
                if (col < SIZE - 1 && board[row][col] == board[row][col + 1]) {
                    return true; // If there are adjacent calls with the same value, there is a valid move
                }
            }
        }
        return false; // If no valid moves are found, return false
    }

    private static void moveUp() {
        for (int col = 0; col < SIZE; col++) {
            for (int row = 1; row < SIZE; row++) {
                if (board[row][col] != 0) {
                    int currentRow = row;

                    while (currentRow > 0  && board[currentRow - 1][col] == 0) {
                        board[currentRow - 1][col] = board[currentRow][col];
                        board[currentRow][col] = 0;
                        currentRow--;
                    }

                    if (currentRow > 0 && board[currentRow - 1][col] == board[currentRow][col]) {
                        board[currentRow - 1][col] *= 2;
                        score += board[currentRow - 1][col];
                        board[currentRow][col] = 0;
                    }
                }
            }
        }
    }

    private static void moveDown() {
        for (int col = 0; col < SIZE; col++) {
            for (int row = SIZE - 2; row >= 0; row--) {
                if (board[row][col] != 0) {
                    int currentRow = row;

                    while (currentRow < SIZE - 1 && board[currentRow + 1][col] == 0) {
                        board[currentRow + 1][col] = board[currentRow][col];
                        board[currentRow][col] = 0;
                        currentRow++;
                    }

                    if (currentRow < SIZE - 1 && board[currentRow + 1][col] == board[currentRow][col]) {
                        board[currentRow + 1][col] *= 2;
                        score += board[currentRow + 1][col];
                        board[currentRow][col] = 0;
                    }
                }
            }
        }
    }

    private static void moveLeft() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 1; col < SIZE; col++) {
                if (board[row][col] != 0) {
                    int currentCol = col;

                    while (currentCol > 0 && board[row][currentCol - 1] == 0) {
                        board[row][currentCol -1] = board[row][currentCol];
                        board[row][currentCol] = 0;
                        currentCol--;
                    }

                    if (currentCol > 0 && board[row][currentCol - 1] == board[row][currentCol]) {
                        board[row][currentCol - 1] *= 2;
                        score += board[row][currentCol - 1];
                        board[row][currentCol] = 0;
                    }
                }
            }
        }
    }

    private static void moveRight() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = SIZE - 2; col >= 0; col--) {
                if (board[row][col] != 0) {
                    int currentCol = col;

                    while (currentCol < SIZE - 1 && board[row][currentCol + 1] == 0) {
                        board[row][currentCol + 1] = board[row][currentCol];
                        board[row][currentCol] = 0;
                        currentCol++;
                    }

                    if (currentCol < SIZE - 1 && board[row][currentCol + 1] == board[row][currentCol]) {
                        board[row][currentCol + 1] *= 2;
                        score += board[row][currentCol + 1];
                        board[row][currentCol] = 0;
                    }
                }
            }
        }
    }

    private static boolean isBoardFull() {
        for (int[] row : board) {
            for (int tile : row) {
                if (tile == 0) {
                    return false; // Return false if any cell is empty
                }
            }
        }
        return true; // Return true if all cells are filled

    }

    private static boolean checkForWin() {
        for (int[] row : board) {
            for (int tile : row) {
                if (tile == WINNING_TILE) {
                    return true;
                }
            }
        }
        return false;
    }
}  