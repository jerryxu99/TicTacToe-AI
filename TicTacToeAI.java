/*
 * This is an AI that plays tic tac toe perfectly using the recursive 'minimax' algorithm.
 * There is also the option to play human vs. human instead of human vs. AI.
 *
 */

import java.util.Scanner;

public class TicTacToeAI {

    //this function checks if the player corresponding to the mark has won the game
    public static int checkBoard(char board[][], char mark) {

        for (int i = 0; i <3; i++) {
            // check for horizontal win
            if (board[i][0] == mark && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return 1;
            }
            // check for vertical win
            if (board[0][i] == mark && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                return 1;
            }
            // check for diagonal win
            if ((board[0][0] == mark && board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
                    (board[0][2] == mark && board[0][2] == board[1][1] && board[1][1] == board[2][0])) {
                return 1;
            }
        }

        // check for draw - all squares full or 9 moves made
        int moveCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 'X' || board[i][j] == 'O') {
                    moveCount++;
                }
            }
        }
        if (moveCount == 9) {
            return 0;
        }

        return -1; // -1 means game not over, 0 means draw, 1 means win
    }

    //this function places a move for the player corresponding to the mark at position/index
    public static void placeMove(char board[][], char mark, int index) {
        //we need to subtract one from position since the input is from 1-9
        index--;
        board[index / 3][index % 3] = mark;
    }

    //this function checks if a certain index on the board is empty
    public static boolean emptySquareTest(char board[][], int index) {
        //we need to subtract one from index since the input is from 1-9
        index--;
        if (board[index / 3][index % 3] == 'X' || board[index / 3][index % 3] == 'O') {
            return false;
        }
        return true;
    }

    //resets the board
    public static void clearBoard(char board[][]) {
        // assign char numbers to all the squares
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = (char)(((int) '1') + ((i * 3) + j));
            }
        }
    }

    //prints out the board
    public static void displayBoard(char board[][]) {
        System.out.println();

        for (int i = 0; i < 3; i++) {
            System.out.print(" ");

            for (int j = 0; j < 3; j++) {
                System.out.print("  " + board[i][j]);

                if ((j == 0) || (j == 1)) {
                    System.out.print("  |");
                }
            }
            System.out.println();

            if ((i == 0) || (i == 1)) {
                System.out.println(" -----+-----+------");
            }
        }
    }

    public static char getOpponent(char mark) {
        if (mark == 'X') {
            return 'O';
        }
        return 'X';
    }

    /*This is a recursive algorithm that assigns every possible move a value depending on the outcome of the game
     *that would follow if a move were to be placed there. This algorithm is commonly known as minimax
     */
    public static int[] getBestMove(char[][] board, char mark) {//return int[] is {scoreForTheMove, move}

        //check if the board has a winner (base case)
        if (checkBoard(board, mark) == 1) {
            return new int[]{1, -1};
        }
        if (checkBoard(board, getOpponent(mark)) == 1) {
            return new int[]{-1, -1};
        }

        int move = -1;
        int score = -2;

        //go through each possible move on the board and assign that move a value
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (emptySquareTest(board, i * 3 + j + 1)) {//check if the space is empty
                    //copy the board to test out a move
                    char[][] tempBoard = new char[3][3];
                    copyBoard(board, tempBoard);

                    tempBoard[i][j] = mark;//try the move
                    //calculate the best move (and its value) for the enemy now after that new move.
                    //the score is -1 * __ because if the opponent wins after you place a move there, then it is a
                    //losing move, and thus has a score of -1. (Vice versa for if the opponent loses from that move)
                    int scoreForTheMove =  -1 * getBestMove(tempBoard, getOpponent(mark))[0];
                    //make sure we are picking the best move
                    if (scoreForTheMove > score) {
                        score = scoreForTheMove;
                        move = i * 3 + j + 1;
                    }
                }
            }
        }

        if (move == -1) {
            return new int[]{0, -1}; //no move wins -- there is a draw
        }
        return new int[]{score, move};
    }

    //copies the original char[][] board into a new char[][]
    public static void copyBoard(char[][] orig, char[][] copy) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copy[i][j] = orig[i][j];
            }
        }
    }

    public static void placeHumanMove(char[][] gameboard, char turn) {
        Scanner scan = new Scanner(System.in);
        // get valid move
        int move = -1;
        while ((move < 1) || (move > 9)) {
            displayBoard(gameboard);
            System.out.println("Where would you like to place your '" + turn + "' ? [1-9] : ");
            move = scan.nextInt();

            // if the square is occupied, make them enter another move
            if (!emptySquareTest(gameboard, move)) {
                move = -1;
            }
        }

        // place the move on the board
        placeMove(gameboard, turn, move);
    }

    public static void main(String[] args) {
        //initialize the game
        char gameboard[][] = new char[3][3];
        int gameStatus;
        boolean gameover = false;
        char turn = 'O';
        boolean humanvshuman;

        Scanner scan = new Scanner(System.in);

        clearBoard(gameboard);

        System.out.println("Want to play human vs. AI? (y/n) Any answer other than 'y' or 'n' will result in a " +
                "human vs. human game");
        if (scan.nextLine().equals("y")) {
            humanvshuman = false;
        } else {
            humanvshuman = true;
        }


        // game loop
        while (!gameover) {
            if (turn == 'O') {
                //switch player
                turn = 'X';
                if (humanvshuman) {
                    placeHumanMove(gameboard, turn);
                } else {//human vs. AI
                    placeMove(gameboard, turn, getBestMove(gameboard, turn)[1]);
                }
            } else {
                //switch player
                turn = 'O';
                placeHumanMove(gameboard, turn);
            }
            // check if that move won or tied the game
            gameStatus = checkBoard(gameboard, turn);

            if (gameStatus == 0) {
                displayBoard(gameboard);
                System.out.println("GAME OVER: DRAW");
                gameover = true;
            }
            if (gameStatus == 1) {
                displayBoard(gameboard);
                System.out.println("GAME OVER: '" + turn + "' WINS!");
                gameover = true;
            }
        } // while (!gameover)    
    }
}