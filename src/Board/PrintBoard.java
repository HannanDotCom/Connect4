package Board;
// PrintBoard interface in Board package with linked class

public interface PrintBoard {
    /* PrintBoard is an interface with one default method. It is responsible only for
       printing the board and should be considered separate from Board.java. In this
       way, if a user wishes to implement a different GUI (e.g. using Java FX), the board class does not need to be
       refactored.
     */

    default void printBoard(char[][] board) {
        /** This method is a default interface method which is responsible for printing the game board.**/

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '\0') {
                    System.out.print("| " + board[i][j] + " "); // if non-empty, prints what is at position
                } else {
                    System.out.print("|   "); // else, prints empty square
                }
            }
            System.out.println("|");
        }

        /* One method for any counter in board - prints whatever occupies board[i][j], if empty ('\0') it will print
        an empty space. In this way, if a user wants to modify the game by introducing another player i.e 3 players,
        the printBoard method does not need to be refactored.
         */

        for (int i = 1; i <= board[0].length; i++) {
            System.out.print("  " + i + " ");
        }

        /* Prints out however many columns there are, with appropriate padding. Again, if a user wishes to change the
        size of the board, the printBoard method does not need to be refactored.
         */
    }
}
