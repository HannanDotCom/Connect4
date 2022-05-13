package Player;
// modularisation - CPUPlayer class in player package with similar classes

import java.util.*;

public class CPUPlayer extends Player {
    // responsible for generating CPU move, whilst inheriting from abstract class player
    // constructor automatically assigns isCPU as true for this class
    public CPUPlayer(char counter) {
        super(counter, true);
    }

    @Override // override method from Player abstract class (polymorphism)
    public void generateMove(char[][] board) {
        /* AI implementation of generating move - strategy is offensive: builds lines where it is most likely
        to gain a count closest to the winCondition of the board
         */
        Random randomColumn = new Random();
        int[][] maxCountAndColumnForAllDirections = {maxCountAndColumnForVerticalWin(board), maxCountAndColumnForHorizontalWin(board), maxCountAndColumnForUpwardsDiagonalWin(board), maxCountAndColumnForDownwardsDiagonalWin(board)};
        // compare maxCount values
        /* after all maxCount values for each possible directional row formation are collected
        they are then compared against each other. The highest maxCount for a given
        direction 'wins' out, causing placement of the counter in the column
        which facilitates prolonging the line in the given direction
         */

        /* if all maxCount values are the same (generally this will be 0), a random column
        is picked out. */
        if ((maxCountAndColumnForAllDirections[0][0] == maxCountAndColumnForAllDirections[1][0]) && (maxCountAndColumnForAllDirections[1][0] == maxCountAndColumnForAllDirections[2][0]) && (maxCountAndColumnForAllDirections[2][0] == maxCountAndColumnForAllDirections[3][0])) {
            while (true) {
                setPosition(randomColumn.nextInt(7));
                if (!isColumnFull(board)) {
                    break;
                    /* during move generation, the column is checked for full occupancy - if function returns false,
                    the loop breaks and the move is deemed valid
                    Else, the block will loop until an available column is selected by the random generator
                            */
                } else {
                    System.out.println("Already occupied!");
                }
            }
        } else {

            Arrays.sort(maxCountAndColumnForAllDirections, (countA, countB) -> Integer.compare(countA[0], countB[0]));
            /* else, uses Comparator to sort 2D array by first column - the count in that direction - in ascending order
        the highest maxCount 'wins' out
                */

             /* loop checks whether the best option for placing a counter, has space in the column. If yes,
        the loop breaks. If not, the loop tries the next best option. This guards against the CPU trying to place a
        counter in a full column.
                */
            for (int i = maxCountAndColumnForAllDirections.length - 1; i >= 0; i--) { // iterates from highest direction maxCount, descending
                setPosition(maxCountAndColumnForAllDirections[i][1]);
                if (!isColumnFull(board)) { // checks if column is full, if so breaks out of loop (valid move)
                    break;
                }
            }
            while (isColumnFull(board)) { // last resort - if all maxCount columns full, choose random column that is not full
                setPosition(randomColumn.nextInt(7));
            }

        }
    }

    public int [] maxCountAndColumnForVerticalWin(char[][] board) {
        int count = 0; // count
        int maxCountV = 0; //max vertical direction count of a continuous line on the board
        int maxCountVColumn = 0; // column corresponding to counter placement which facilitates increasing maxCount in vertical direction

        /* offensive vertical check - for CPU player's counter, finds the element in the board from where the highest
        vertical count is achieved.
        // returns array containing maxCount and corresponding maxCountColumn

        1) This is done by calculating vertical counts for each position where the player's
        counter is present and comparing it against the current max count.
        2) Then it checks whether there is an open position for that column and whether it is still viable to form a row
        of four with the player's counter (checkUpOccupancy).
        3) If all 3 conditions are met, the count at that particular column is recorded in maxCountV and the column number
        is recorded in maxCountVColumn.
        4)Else, count is reset to 0 and iteration continues.
         */
        for (int i = board[0].length - 1; i >= 0; i--) {
            for (int j = board.length - 1; j >= 0; j--) {
                if (board[j][i] == getCounter()) {
                    while ((board[j][i] == getCounter())) {
                        count += 1;
                        if (j - 1 < 0) { // guards against ArrayIndexOutOfBoundsException
                            break;
                        } else {
                            j -= 1;
                        }
                    }
                    try {
                        if (count > maxCountV && board[j - 1][i] == '\0' && (checkUpOccupancy(board, j, i) + 1 >= getWinCondition())) {
                            maxCountV = count;
                            maxCountVColumn = i;
                        }
                        count = 0;
                    } catch (Exception e) {
                        count = 0;
                    }

                }

            }
        }
        // returns array of final maxCountV and maxCountVColumn
        return new int[]{maxCountV, maxCountVColumn};
    }

    public int [] maxCountAndColumnForHorizontalWin(char[][] board) {
        int count = 0; // count
        int maxCountH = 0; //max horizontal direction count of a continuous line on the board
        int maxCountHColumn = 0; // column corresponding to counter placement which facilitates increasing maxCount in horizontal direction

        /* offensive horizontal check
        // returns array containing maxCount and corresponding maxCountColumn

        1) horizontal counts of all counters of player's type generated
        2) for position, left-hand side and right-hand spaces are checked for occupancy (need to be at least 1 empty)
        3) for position, bottom left side and bottom right side occupancy checked (needs to be occupied for a counter to
        be placed on top)
        4) checkLeftOccupancy and checkRightOccupancy checks if it is viable for a row of 4 to be made for that position
        5) then count of position is checked against current maxCount, if there is a left/right hand side empty space and its corresponding
        space below it being occupied (so counter can be placed on top)
        6) if all conditions are met, column position is recorded
        7) else, iteration continues
         */
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[i].length - 1; j >= 0; j--) {
                if (board[i][j] == getCounter()) {
                    int k = 0;
                    while ((board[i][j - k] == getCounter())) {
                        count += 1;
                        if (j - k - 1 < 0) { // guards against ArrayIndexOutOfBoundsException
                            break;
                        } else {
                            k += 1;
                        }
                    }
                    boolean isBottomLeftOccupied; // checks if bottom left space is occupied at desired position
                    try {
                        isBottomLeftOccupied = board[i + 1][j - k] != '\0';
                    } catch (ArrayIndexOutOfBoundsException e) { // if out of bounds then technically 'occupied'
                        isBottomLeftOccupied = true;
                    }

                    boolean isBottomRightOccupied; // checks if bottom right space is occupied at desired position
                    try {
                        isBottomRightOccupied = board[i + 1][j + 1] != '\0';
                    } catch (ArrayIndexOutOfBoundsException e) { // if out of bounds then technically 'occupied'
                        isBottomRightOccupied = true;
                    }

                    boolean isLeftFree; // checks if free space to left of the potential row
                    try {
                        isLeftFree = board[i][j - 1] == '\0';
                    } catch (ArrayIndexOutOfBoundsException e) { //
                        isLeftFree = false;
                    }

                    boolean isRightFree; // checks if free space to right of potential row
                    try {
                        isRightFree = board[i][j + 1] == '\0';
                    } catch (ArrayIndexOutOfBoundsException e) {
                        isRightFree = false;
                    }


                    if (count > maxCountH && isLeftFree && isBottomLeftOccupied && ((checkLeftSideOccupancy(board, i, j) + checkRightSideOccupancy(board, i, j) + 1) >= getWinCondition())) {
                        maxCountH = count;
                        maxCountHColumn = j - 1;
                    } else if (count > maxCountH && isRightFree && isBottomRightOccupied && ((checkLeftSideOccupancy(board, i, j) + checkRightSideOccupancy(board, i, j) + 1) >= getWinCondition())) {
                        maxCountH = count;
                        maxCountHColumn = j + 1;
                    }
                    count = 0;

                }

            }

        }
        return new int[]{maxCountH, maxCountHColumn};
    }

    public int [] maxCountAndColumnForDownwardsDiagonalWin(char[][] board) {
        int count = 0; // count
        int maxCountDD = 0; //max downwards diagonal direction count of a continuous line on the board
        int maxCountDDColumn = 0; // column corresponding to counter placement which facilitates increasing maxCount in downwards diagonal direction
        // offensive downwards diagonal (dd) check
        // returns array containing maxCount and corresponding maxCountColumn

        /* 1) records dd count of position
           2) checks whether dd position is empty (so counter can be placed)
           3) checks whether the position below the desired position is occupied (so counter has something to be placed
           on top of)
           4) checkUpperLeftOccupancy and checkLowerRightOccupancy return whether a viable row of 4 can be created in the
           dd direction
           5) compares current max count against current count and if previous 3 conditions are all met, then column
           position is recorded
           6) else, iteration continues
         */
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[0].length - 1; j >= 0; j--) {
                int d = 0;
                if ((board[i][j] == getCounter()) && (board[i - d][j - d] == getCounter())) {
                    while ((board[i - d][j - d] == getCounter())) {
                        count += 1;
                        if (i - d - 1 < 0 || j - d - 1 < 0) { // guards against ArrayIndexOutOfBoundsException
                            break;
                        } else {
                            d++;
                        }
                    }
                    boolean isBelowOccupied; // checks if space below desired position is occupied (so can place counter on top)
                    try {
                        isBelowOccupied = board[i + 1 - d][j - d] != '\0';
                    } catch (ArrayIndexOutOfBoundsException e) { // if out of bounds then technically 'occupied'
                        isBelowOccupied = true;
                    }

                    boolean isFree; // checks if desired position is free
                    try {
                        isFree = board[i - d][j - d] == '\0';
                    } catch (ArrayIndexOutOfBoundsException e) {
                        isFree = false;
                    }

                    if (count > maxCountDD && isFree && isBelowOccupied && (checkUpperLeftOccupancy(board, i, j) + checkLowerRightOccupancy(board, i, j) + 1 >= getWinCondition())) {
                        maxCountDD = count;
                        maxCountDDColumn = j - d;
                    }
                    count = 0;


                }

            }
        }
        return new int[]{maxCountDD, maxCountDDColumn};
    }

    public int [] maxCountAndColumnForUpwardsDiagonalWin(char[][] board) {
        int count = 0; // count
        int maxCountUD = 0; //max upwards diagonal direction count of a continuous line on the board
        int maxCountUDColumn = 0; // column corresponding to counter placement which facilitates increasing maxCount in upwards diagonal direction
        // offensive upwards diagonal check
        // returns array containing maxCount and corresponding maxCountColumn
        // operates in the same way as the downwards diagonal check but in the upwards diagonal direction
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = 0; j < board[0].length; j++) {
                int d = 0;
                if ((board[i][j] == getCounter()) && (board[i - d][j + d] == getCounter())) {
                    while ((board[i - d][j + d] == getCounter())) {
                        count += 1;
                        if ((j + d + 1 >= board[0].length) || (i - d - 1 < 0)) { // guards against ArrayIndexOutOfBoundsException
                            break;
                        } else {
                            d++;
                        }
                    }
                    boolean isBelowOccupied; // checks if position below desired position is occupied (so can place counter on top)
                    try {
                        isBelowOccupied = board[i - d + 1][j + d] != '\0';
                    } catch (ArrayIndexOutOfBoundsException e) {
                        isBelowOccupied = true;
                    }

                    boolean isFree; // checks if desired position is empty
                    try {
                        isFree = board[i - d][j + d] == '\0';
                    } catch (ArrayIndexOutOfBoundsException e) {
                        isFree = false;
                    }

                    if (count > maxCountUD && isFree && isBelowOccupied && (checkUpperRightOccupancy(board, i, j) + checkLowerLeftOccupancy(board, i, j) + 1 >= getWinCondition())) {
                        maxCountUD = count;
                        maxCountUDColumn = j + d;
                    }
                    count = 0;
                }

            }
        }
        return new int[]{maxCountUD, maxCountUDColumn};
    }

    public int checkRightSideOccupancy(char[][] board, int i, int j) {
        /* checks horizontal occupancy of spaces from this.counter and returns how many available
        'spots' where the counter can be placed there are, on the right side direction.
        returns the number of available spots
                */
        int k;
        for (k = 0; k < board[0].length - j; k++) {
            if (board[i][j + k] != '\0' && board[i][j + k] != getCounter()) {
                return k - 1;
            }
        }
        return k;
    }

    public int checkLeftSideOccupancy(char[][] board, int i, int j) {
        /* checks horizontal occupancy of spaces from this.counter and returns how many available
        'spots' where the counter can be placed there are, on the left side direction.
        returns the number of available spots
                */
        int k;
        for (k = 0; k <= j; k++) {
            if (board[i][j - k] != '\0' && board[i][j - k] != getCounter()) {
                return k - 1;
            }
        }
        return k;
    }

    public int checkUpperRightOccupancy(char[][] board, int i, int j) {
        /* checks the diagonal occupancy of spaces from this.counter and returns how many available
        'spots' where the counter can be placed there are, in the upper right direction.
        returns the number of available spots
                */
        int k;
        for (k = 0; k <= Math.min(board[0].length - j - 1, i); k++) {
            if (board[i - k][j + k] != '\0' && board[i - k][j + k] != getCounter()) {
                return k - 1;
            }
        }
        return k;
    }

    public int checkLowerRightOccupancy(char[][] board, int i, int j) {
        /* checks the diagonal occupancy of spaces from this.counter and returns how many available
        'spots' where the counter can be placed there are, in the lower right direction.
        returns the number of available spots
         */
        int k;
        for (k = 0; k <= Math.min(board[0].length - j - 1, board.length - i - 1); k++) {
            if (board[i + k][j + k] != '\0' && board[i + k][j + k] != getCounter()) {
                return k - 1;
            }
        }
        return k;
    }

    public int checkLowerLeftOccupancy(char[][] board, int i, int j) {
        /* checks the diagonal occupancy of spaces from this.counter and returns how many available
        'spots' where the counter can be placed there are, in the lower left direction.
        returns the number of available spots
         */
        int k;
        for (k = 0; k <= Math.min(j, board.length - i - 1); k++) {
            if (board[i + k][j - k] != '\0' && board[i + k][j - k] != getCounter()) {
                return k - 1;
            }
        }
        return k;
    }

    public int checkUpperLeftOccupancy(char[][] board, int i, int j) {
        /* checks the diagonal occupancy of spaces from this.counter and returns how many available
        'spots' where the counter can be placed there are, in the upper left direction.
        returns the number of available spots
         */
        int k;
        for (k = 0; k <= Math.min(j, i); k++) {
            if (board[i - k][j - k] != '\0' && board[i - k][j - k] != getCounter()) {
                return k - 1;
            }
        }
        return k;
    }

    public int checkUpOccupancy(char[][] board, int i, int j) {
        /* checks the vertical occupancy of spaces from this.counter and returns how many available
        'spots' where the counter can be placed there are, in the upwards direction.
        returns the number of available spots
         */
        int k;
        for (k = 0; k <= i; k++) {
            if (board[i - k][j] != '\0' && board[i - k][j] != getCounter()) {
                return k - 1;
            }
        }
        return k;

    }

}

