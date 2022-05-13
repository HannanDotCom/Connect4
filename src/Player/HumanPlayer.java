package Player;
// modularisation - HumanPlayer class in Player package with similar classes

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HumanPlayer extends Player {
    /* inherits Player fields and methods but allows unique method of generateMove.
    This is different to a CPU player and so both players need to be abstractions of Player
     */
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // constructor assigns isCPU as false when creating an instance

    public HumanPlayer(char counter) {
        super(counter, false);
    }

    @Override // override method from Player abstract class (polymorphism)
    public void generateMove(char[][] board) {
        // method for parsing user input to select column position
        while (true) {
            // use of a while loop allows user to re-enter column if an invalid input is given
            try {
                System.out.println("Please enter the column you wish to place your counter:");
                String userInput = br.readLine();
                int move = Integer.parseInt(userInput); // parses userInput as int
                if (move <= 7 && move > 0) { // guards against inputs not within the range of the board
                    this.setPosition(move - 1); // index of board starts at 0
                    if(!isColumnFull(board)) { // checks if valid move i.e column at capacity
                        break;
                    }
                    else {
                        System.out.println("Oops! Invalid move - column is full! Please try again!");
                    }
                }
                else {
                    System.out.println("Must be an integer between 1 and 7!");
                }

            } catch (NumberFormatException e) { // if input is not numerical, catches exception and requires user to input
                                                // valid number
                System.out.println("Must be an integer between 1 and 7!");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
