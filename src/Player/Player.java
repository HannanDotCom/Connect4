package Player;
// modularisation - Player class in Player package with linked subclasses

public abstract class Player {
    // abstract class responsible for the current status of the player (both turn and win);
    /* as 2 types of player can be played against (human and CPU), these require different methods
    of generating moves and so a parent abstract class is the most appropriate.
    it also prevents a user from instantiating a Player object, which does not have a way of generating moves.
    all subclasses inherit Player's fields and methods (inheritance)
     */

    private final char counter; // player counter - made final so cannot be changed during play
    private boolean win; // win status of player
    private final boolean isCPU; // whether player is CPU or not - made final so cannot be changed during play - would disrupt the game
    private boolean currentTurn; // whether player's current turn
    private int position; // player's chosen column for counter placement
    private final int winCondition = 4; // how many counters needed in a row to win game
    /* Will use extensively when checking wins, so needs to be final to not disrupt play or game logic.
    Can also be changed if user wants to change game rules.
     */

    /* constructor assigns this.win as false and this.position outside of board so there is no accidental premature
    counter placement (would throw an ArrayOutOfBoundsException) and guards against a false win;
     current turn is also false so game is not prematurely started until playGame instance is created in Main*/
    public Player(char counter, boolean isCPU) {
        this.counter = counter;
        this.win = false;
        this.isCPU = isCPU;
        this.currentTurn = false;
        this.position = - 1;
    }

    /* getters and setters for fields (which are ALL private) prevents other classes from changing these values
    unintentionally - encapsulation
     */
    public char getCounter() {
        return counter;
    }

    public boolean isWin() {
        return win;
    }

    public boolean isCurrentTurn() {
        return currentTurn;
    }

    public int getPosition() {
        return position;
    }

    public int getWinCondition() {
        return winCondition;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public void setCurrentTurn(boolean currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void toggleTurn() {
        // method toggles turn during play
        if (!this.currentTurn) {
            setCurrentTurn(true);
        } else if (this.currentTurn) {
            setCurrentTurn(false);
        }
    }

    public void generateMove(char[][] board) {
        // empty method that different subclasses implement differently
    }

    public boolean isColumnFull(char[][] board) {
        /* method used by both types of player to validate their chosen move
        if chosen column is occupied, returns true, else returns false
         */
        int occupiedCount = 0; // counts number of non-empty elements in column
        for (int i = 0; i < board.length; i++) {
            if (board[i][getPosition()] != '\0') { //checks for non-empty elements and sums number of them
                occupiedCount += 1;
            }
        }
        return occupiedCount == board.length; //if equal to height of column, column must be full
    }

    public boolean checkRowWin(char[][] board) {
        /* checks if row has win for counter - uses winCondition instead of 4 in case there is a typo or
        user wishes to use another winCondition - encapsulation
         */
        int count = 0; // counts number of continuous player counters
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == this.counter) {
                    while (board[i][j] == this.counter) { // ensure that it counts CONTINUOUS counters by using a while loop
                        count = count + 1;
                        if (j + 1 >= board[i].length) { // guards against ArrayIndexOutOfBoundsException
                            break;
                        } else {
                            j++;
                        }
                    }
                    // once breaks out of while loop, compares count to wincondition - if true, returns true
                    // else, resets the count to 0 and moves onto next position
                    if (count >= this.winCondition) {
                        return true;
                    } else {
                        count = 0;
                    }
                }
            }
        }
        // if never reaches winCondition at end of iteration, returns false
        return false;
    }

    public boolean checkDiagonalWin(char[][] board) {
        // checks both diagonal directions for a win for the counter - same method as checkRow
        // check downwards diagonal
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int d = 0;
                if (d + i < board.length && j + 1 < board[0].length) {
                    if (board[i][j] == this.counter && board[i + d][j + d] == this.counter) {
                        while ((board[i + d][j + d] == this.counter)) { // while loop ensures CONTINUOUS line
                            count += 1;
                            if (d + i + 1 >= board.length || j + d + 1 >= board[0].length) { // guards against ArrayIndexOutOfBoundsException
                                break;
                            } else {
                                d++;
                            }
                        }
                    }
                    if (count >= this.winCondition) {
                        return true;
                    } else {
                        count = 0;
                    }

                }
            }
        }
        // check upwards diagonal and count reset to 0 for upwards diagonal check
        count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int d = 0;
                if ((j - d >= 0) && (i + d < board.length)) {
                    if (board[i][j] == this.counter && board[i + d][j - d] == this.counter) {
                        while ((board[i + d][j - d] == this.counter)) {
                            count += 1;
                            if ((j - d - 1 < 0) || (i + d + 1 >= board.length)) {
                                break;
                            } else {
                                d++;
                            }
                        }
                    }
                    if (count >= this.winCondition) {
                        return true;
                    } else {
                        count = 0;
                    }

                }
            }
        }
        return false; // returns false if count has not returned winCondition for both diagonal directions
    }

    public boolean checkColumnWin(char[][] board) {
        // checks for vertical win - same method used as for checkRow (iterate over board)
        int count = 0;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[j][i] == this.counter) {
                    while ((board[j][i] == this.counter)) { //while loop ensures continuous line of 4!
                        count += 1;
                        if (j + 1 >= board.length) { // guards against ArrayIndexOutOfBoundsException
                            break;
                        } else {
                            j += 1;
                        }

                    }
                    if (count >= this.winCondition) {
                        return true;
                    } else {
                        count = 0; // resets counter if winCondition not reached
                    }
                }

            }

        }
        return false; // returns false if winCondition is not reached
    }

    public boolean checkWin(char[][] board) {
        /* implements the previous check methods into one checkWin method - this enables cleaner code and also
        allows a user to adjust the checkWin method if they wanted to change the game rules;
        method goes through each case to check if it has returned true or not - saves computation time as once it has
        reached one win, it breaks out of the method
         */
        if(checkRowWin(board)) {
            return checkRowWin(board);
        }
        else if(checkDiagonalWin(board)) {
            return checkDiagonalWin(board);
        }
        else if(checkColumnWin(board)) {
            return checkColumnWin(board);
        }
        else {
            return false; // returns false if no win has been detected
        }
    }

}
