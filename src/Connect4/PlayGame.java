package Connect4;
// PlayGame class in Connect4 package as instantiates Connect4 game

import Board.Board;
import Player.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

public class PlayGame {
    // class in which game status is controlled (if game is won and whether game is occurring)
    private boolean gameWon; // determines if game is won
    private boolean playGame; // determines if active game is being played

    // default constructor sets gameWon as false (to prevent accidental wins) and playGame as true
    public PlayGame() {
        this.gameWon = false;
        this.playGame = true;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isPlayGame() {
        return playGame;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public void setPlayGame(boolean playGame) {
        this.playGame = playGame;
    }
    public void playGame(Player player1, Player player2) {
        /* method for initiating game against any player - general so not dependent on subclasses but
        abstract classes (abstraction);
        ensures loose coupling between classes
         */
        Board gameBoard = new Board();
        gameBoard.printBoard(gameBoard.getBoard());
        // in case both booleans are set to true or false, this is one last guard to ensure they are both false
        player1.setCurrentTurn(false);
        player2.setCurrentTurn(false);

        // generates number either 0 or 1 which will determine which player goes first to vary the gameplay;
        // this is particularly important for the CPU gameplay (game is harder when CPU goes first)
        Random numberGenerator = new Random();
        int playerSelection = numberGenerator.nextInt(2); // generates either 0 or 1

        if(playerSelection == 1) {
            player1.setCurrentTurn(true);
            System.out.println("\nPlayer 1 goes first!");
        }

        else {
            player2.setCurrentTurn(true);
            System.out.println("\nPlayer 2 goes first!");
        }

        while (!isGameWon()) { // by default isGameWon set to false
            if (player1.isCurrentTurn() && !gameBoard.isBoardFull()) { // game board is checked to be full each turn
                playerTurn(player1, gameBoard); // turn initiated
                if (player1.isWin()) {
                    System.out.println("\nCongratulations, player 1 wins!"); // if player wins after turn, then loop breaks
                    setGameWon(true);
                } else { // else, player 2's turn
                    player1.toggleTurn();
                    player2.toggleTurn();
                }
            }

            else if (player2.isCurrentTurn() && !gameBoard.isBoardFull()) {
                playerTurn(player2, gameBoard);
                if (player2.isWin()) {
                    System.out.println("\nCongratulations, player 2 wins!");
                    setGameWon(true);
                } else {
                    player2.toggleTurn();
                    player1.toggleTurn();
                }

            }
            else {
                setGameWon(true); // this occurs when board is full (isBoardFull = true)
                break; // breaks out of loop
            }
        }
        /* while loop ultimately repeats until isGameWon is true, then endGame() is called which ends the game
        and allows the player to play again if they wish
         */
        endGame();
    }

    public void playerTurn(Player player, Board gameBoard) {
        /* generic turn for any subclass of Player - same for any
        type of player, human or CPU - enables loose coupling of classes
        and class dependency on abstraction rather than implementation;
        easier to extend code without having to modify current code
         */
        System.out.println("\n" + player.getCounter() + "'s turn!");
        player.generateMove(gameBoard.getBoard()); // generates move
        gameBoard.placeCounter(player.getPosition(), player.getCounter()); // places counter
        gameBoard.printBoard(gameBoard.getBoard()); // prints new board
        if(player.checkWin(gameBoard.getBoard())) { // checks win after each turn
            player.setWin(true);
        }
    }

    public void endGame() {
        /* method called when game is won - allows user to play again if they wish */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if (this.gameWon) {
            while (true) {                            // while loop guards against invalid inputs from user
                System.out.println("Play again? Y/N");
                try {
                    String answer = br.readLine();
                    if (answer.equalsIgnoreCase("y")) {
                        setGameWon(false); // resets game
                        return;
                    } else if (answer.equalsIgnoreCase("n")) {
                        setPlayGame(false); // exits
                        break;
                    } else {
                        System.out.println("Please input a valid choice.");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    break;
                }
            }
        }

        else {
        }
    }

}