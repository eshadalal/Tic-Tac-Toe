package project2;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

class Board { // manages tic-tac-toe grid

    private char[][] board = new char[3][3];

    public Board() { 

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                board[row][column] = ' ';  
            }
        }
    }

    public void placeMarkers(int row, int column, char symbol) { 
        board[row][column] = symbol;
        
    }

    public void displayBoard() { 
        for (int row = 0; row < 3; row++) {
            System.out.println("------------");
            for (int column = 0; column < 3; column++) {
                System.out.print("| " + board[row][column] + " ");
            }
            System.out.println("|");

        }

        System.out.println("------------");

    }

    public boolean checkValidMove(int row, int column) { 

        if ((row < 3 && row >= 0) && (column < 3 && column >= 0) && board[row][column] == ' ') { 
            return true;
        } else {
            return false;
        }
    }

    public boolean checkWinningCondition(char currentSymbol) { 

        for (int i = 0; i < 3; i++) { 
            if ((board[i][0] == currentSymbol && board[i][1] == currentSymbol && board[i][2] == currentSymbol)  || // three in a row/column
                (board[0][i] == currentSymbol && board[1][i] == currentSymbol && board[2][i] == currentSymbol)) {
                
                return true;
            }
        }
            if ((board[0][0] == currentSymbol && board[1][1] == currentSymbol && board[2][2] == currentSymbol) || // diagonals
                (board[0][2] == currentSymbol && board[1][1] == currentSymbol && board[2][0] == currentSymbol)) {
                
                return true;
            }

            return false;
    }
} 

abstract class Player { // represents player  

    char currentSymbol;

    public Player(char currentSymbol) { 
        this.currentSymbol = currentSymbol;
    }

    public char getCurrentSymbol() {
        return currentSymbol;
    }

    public abstract void makeMove(FlexibleBoard flexibleGameBoard, int N, int M);

}

class FlexibleGame { // step 7 - upgraded game

    private Player player1;
    private Player player2;
    private FlexibleBoard flexibleGameBoard;
    private int N; 
    private int M;

    public FlexibleGame(Player player1, Player player2, int N, int M) { // constructor - initialize game 
        this.player1 = player1;
        this.player2 = player2;
        this.N = N; 
        this.M = M;
        this.flexibleGameBoard = new FlexibleBoard(N, M);

    }

    public void gamePlay() { // manages player turns 

        Scanner scanner = new Scanner(System.in);
        Player currentPlayer = player1;

        int usedTiles = 0; 
        boolean gameOver = false;

        while (!gameOver && usedTiles < (N*N)) { 
            flexibleGameBoard.displayBoard(); 
            currentPlayer.makeMove(flexibleGameBoard, N, M);
            usedTiles++; 
            
            if (flexibleGameBoard.checkWinningCondition(currentPlayer.getCurrentSymbol())) {
                flexibleGameBoard.displayBoard();
                System.out.println(currentPlayer + " wins!");  
                gameOver = true;          

            } else if (usedTiles == (N*N)) { 
                flexibleGameBoard.displayBoard();
                System.out.println("It's a tie!"); 
                gameOver = true;            
            }

            if (currentPlayer == player1) {
                currentPlayer = player2;
            } else {
                currentPlayer = player1;
            }
            
        }

        Boolean invalidChoice = true;
        int restart = -1;

        while (invalidChoice) { 
            System.out.println("Do you want to play again? (1 for play again / 0 for no)");
            try {
                restart = scanner.nextInt();
                if (restart == 1 || restart == 0) {
                    invalidChoice = false; 
                } else {
                    System.out.println("Invalid input. Please enter either 1 or 0.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter either 1 or 0.");
                scanner.nextLine(); 
            }

        }

        if (restart == 1) { 
            flexibleGameBoard = new FlexibleBoard(N, M); 
            gameOver = false; 
            usedTiles = 0; 
            gamePlay(); 
        } else { 
            gameOver = true;
        }
        } 

} 

class Human extends Player {

    public Human(char symbol) { 
        super(symbol);
    }

    public void makeMove(FlexibleBoard flexibleGameBoard, int N, int M) {
        Scanner scanner = new Scanner(System.in);

        int row = 0;
        int column = 0;
        boolean invalidInput = true;

        while (invalidInput) { 
            System.out.println("Enter your row: ");
            try {
                row = scanner.nextInt();

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); 
                continue; 
            }

            System.out.println("Enter your column: "); 
            try {
                column = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); 
                continue; 
        }

        if (flexibleGameBoard.checkValidMove(row, column)) { 
            invalidInput = false;
        } else { 
            System.out.println("Invalid move. Choose another square.");
        }
    } 
        
    flexibleGameBoard.placeMarkers(row, column, getCurrentSymbol());
    
    }   

}

class FlexibleComputer extends Player { 

    public FlexibleComputer(char currentSymbol) { 
        super(currentSymbol);
    }
    
    @Override
    public void makeMove(FlexibleBoard flexibleGameBoard, int N, int M) {

        Random random = new Random();
        int row = 0;
        int column = 0;

        if (N % 2 != 0) { // if center square exists
            if (flexibleGameBoard.checkValidMove(N/2, N/2)) { // take center square if empty
                row = N/2; 
                column = N/2;
    
                flexibleGameBoard.placeMarkers(row, column, getCurrentSymbol());
                System.out.println("Computer played their move.");
            } else { 
                randomMove(flexibleGameBoard, N, random);
            }
        } else { 
            randomMove(flexibleGameBoard, N, random);    
        }
}

    private void randomMove(FlexibleBoard flexibleGameBoard, int N, Random random) { 
        int row = random.nextInt(N); 
        int column = random.nextInt(N); 

        while (flexibleGameBoard.checkValidMove(row, column) == false) {
            row = random.nextInt(N); 
            column = random.nextInt(N); 
        }

        flexibleGameBoard.placeMarkers(row, column, getCurrentSymbol());
        System.out.println("Computer played their move.");
    }

}

class FlexibleBoard extends Board { // manages tic-tac-toe grid

    char[][] flexibleGameBoard;
    private int N; 
    private int M; 

    public FlexibleBoard(int N, int M) { 
        this.N = N;
        this.M = M;
        flexibleGameBoard = new char[N][N];  

        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                flexibleGameBoard[row][column] = ' ';  
            }
        }
    }

    @Override
    public void placeMarkers(int row, int column, char symbol) { 
        flexibleGameBoard[row][column] = symbol;
        
    }

    @Override
    public void displayBoard() { 
        for (int row = 0; row < N; row++) {
            for (int i = 0; i < N; i++) {
                System.out.print("----"); 
            }
            System.out.println(); 

            System.out.print("| ");
            for (int column = 0; column < N; column++) {
                System.out.print(flexibleGameBoard[row][column] + " | ");
            }
            System.out.println(); 

        }
        for (int i = 0; i < N; i++) {
            System.out.print("----");
        }
        
        System.out.println();
    }

    @Override
    public boolean checkValidMove(int row, int column) { 

        if ((row < N && row >= 0) && (column < N && column >= 0) && flexibleGameBoard[row][column] == ' ') { 
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkWinningCondition(char currentSymbol) { 
        // rows
        for (int row = 0; row < N; row++) {
            int sequence = 0;
            for (int column = 0; column < N; column++) {
                if (flexibleGameBoard[row][column] == currentSymbol) {
                    sequence++;
                    if (sequence == M) {
                        return true; 
                    }
                } else {
                    sequence = 0;
                }
            }
        }
        
        //columns
        for (int column = 0; column < N; column++) {
            int sequence = 0;
            for (int row = 0; row < N; row++) {
                if (flexibleGameBoard[row][column] == currentSymbol) {
                    sequence++;
                    if (sequence == M) {
                        return true; 
                    }
                } else {
                    sequence = 0; 
                }
            }
        }

        // diagonals
        for (int row = 0; row <= N - M; row++) { 
            for (int column = 0; column <= N - M; column++) {
                int sequence = 0; 
                for (int i = 0; i < M; i++) {
                    if (flexibleGameBoard[row + i][column + i] == currentSymbol) {
                        sequence++;
                        if (sequence == M) {
                            return true; 
                        }
                    } else {
                        sequence = 0; 
                    }
                }
            }
        }

        return false; 
    }
} 

public class App {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        char player1Symbol = 'X';
        char player2Symbol= 'O';

        int gameMode = 0; 

        while (gameMode < 1 || gameMode > 3) {  
            System.out.println("Choose your game mode:");
            System.out.println("(1) Human vs Human");
            System.out.println("(2) Human vs Computer");
            System.out.println("(3) Computer vs Computer");
            
            if (scanner.hasNextInt()) {
                gameMode = scanner.nextInt();
            } else {
                System.out.println("Please choose a valid game mode.");
                scanner.nextLine(); 
                continue;
            }

            if (gameMode < 1 || gameMode > 3) {
                System.out.println("Invalid selection! Please choose a valid game mode.");
            }
        }
        
        Player player1 = null;
        Player player2 = null;

        switch (gameMode) {
            case 1:
                player1 = new Human(player1Symbol);
                player2 = new Human(player2Symbol);
                break;
            case 2:
                player1 = new Human(player1Symbol);
                player2 = new FlexibleComputer(player2Symbol);
                break;
            case 3:
                player1 = new FlexibleComputer(player1Symbol);
                player2 = new FlexibleComputer(player2Symbol);
                break;
            default:
                System.out.println("Please choose a valid game mode.");
                gameMode = scanner.nextInt();
                break;
        }

        int N = 0; 

        while (N < 3 || N > 20) {  
            System.out.println("Enter the grid size (between 3 and 20 inclusive): ");
        
            if (scanner.hasNextInt()) {
                N = scanner.nextInt();
            } else {
                System.out.println("Please choose a valid grid size.");
                scanner.nextLine(); 
                continue;
            }

            if (N < 3 || N > 20) {
                System.out.println("Please choose a valid grid size.");
            }
        }

        int M = 0; 

        while (M > N || M <= 1) {  
            System.out.println("Enter the winning sequence length: ");
        
            if (scanner.hasNextInt()) {
                M = scanner.nextInt();
            } else {
                System.out.println("Please choose a valid sequence length.");
                scanner.nextLine(); 
                continue;
            }

            if (M > N || M <= 1) {
                System.out.println("Please choose a valid sequence length.");
            }
        }


        FlexibleGame ticTacToeGame = new FlexibleGame(player1, player2, N, M);
        ticTacToeGame.gamePlay();

    }
}
