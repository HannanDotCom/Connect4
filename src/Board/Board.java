package Board;
// modularisation - Board class in Board package with linked interfaces

public class Board implements PrintBoard {
    /* responsible for the status of the board - methods are placing the counter and
    monitoring board capacity.
    Implements PrintBoard in order to print the board to the terminal (interface) and inherits method */
    private char[][] board; // board array
    private boolean counterPlaced; // checks if a counter has been placed

    /* Implemented a default constructor to set board size as traditional rules however, constructor with parameters
    still available if a user wishes to change the size of the board when called from Main (polymorphism - overloading)*/

    public Board() {
        this.board = new char[6][7]; // default size as per rules
        this.counterPlaced = false;
    }

    public Board(char[][] board) {
        this.board = board;
        this.counterPlaced = false;
    }

    /* board array itself and counterPlaced fields are only accessible by other classes using getters (both are set to private)
    - promotes encapsulation of code as cannot be modified by other classes.
     */

    public char[][] getBoard() {
        return board;
    }

    public boolean isCounterPlaced() {
        return counterPlaced;
    }

    public void setCounterPlaced(boolean counterPlaced) {
        this.counterPlaced = counterPlaced;
    }

    public boolean isBoardFull() {
        /* checks if board is completely full, guards against invalid counter placement and can be used for any size
        board
         */
        int occupiedCount = 0; // count for how many non-empty elements in board
        for(int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                if (board[i][j] != '\0') { //only checks if non-empty element - does not depend on counters
                    occupiedCount += 1;
                } else {
                }
            }
        }
        // if occupiedCount equal to size of 2D array, indicates board is full; prints a statement and returns false
        if(occupiedCount == (this.board.length * this.board[0].length)) {
            System.out.println("Board is full!");
            System.out.println("No-one wins!");
            return true;
        }

        else {
            return false;
        }
    }

    public void placeCounter(int position, char counter) {
        // takes in position and counter to place counter at column
        setCounterPlaced(false); // sets boolean to false initially, in case it is still true
        while (!isCounterPlaced()) {
            for (int i = board.length - 1; i >= 0; i--) {
                if (!isCounterPlaced()) {
                    if (board[i][position] != '\0') { // checks if non-empty, if so, will iterate to next row up
                        // skip
                    } else {
                        board[i][position] = counter;
                        setCounterPlaced(true); // breaks out of loop when element is empty and sets it to counter
                    }
                }
            }
        }
    }

}


