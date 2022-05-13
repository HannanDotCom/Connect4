package Connect4;
// Main class in Connect4 package as where game is instantiated

import Player.CPUPlayer;
import Player.HumanPlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Connect 4! The aim of the game is to place four of your counters in a row!");
        System.out.println("The row can be vertical, horizontal or diagonal!\n");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PlayGame game = new PlayGame();

        while (game.isPlayGame()) {
            try {
                System.out.println("Would you like to play against a human or the CPU?");
                System.out.println("1: Human");
                System.out.println("2: CPU");

                HumanPlayer player1 = new HumanPlayer('r');

                String answer = br.readLine();
                int numberChoice = Integer.parseInt(answer); // converts answer to int

                if (numberChoice != 1 && numberChoice != 2) {
                    System.out.println("Oops! Input must be 1 or 2!");
                } else {
                    if (numberChoice == 1) {
                        HumanPlayer player2 = new HumanPlayer('y'); // play game with human
                        game.playGame(player1, player2); // instantiates game
                    } else if (numberChoice == 2) {
                        CPUPlayer player2 = new CPUPlayer('y'); // play game with CPU
                        game.playGame(player1, player2); // instantiates game
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
                System.out.println(e.getStackTrace()[0].getLineNumber());

            }
        }

    }
}
