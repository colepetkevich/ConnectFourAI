package connectfour;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyl
 */

public class Board
{
    public static final int ROWS = ConnectFour.ROWS;
    public static final int COLUMNS = ConnectFour.COLUMNS;

    private boolean redTurn;

    private long currPlayer;
    private long mask;

    /**
     * Default Constructor for Board Objects
     * @param
     * @return
     * Sets the current turn to be Red's as default
     * Sets the current player to be 0
     * Sets the mask to be 0
     */
    public Board()
    {
        redTurn = true;

        currPlayer = 0;
        mask = 0;
    }

    /**
     * Copy Constructor for a Board
     * @param other
     */
    public Board(Board other)
    {
        redTurn = other.redTurn;

        currPlayer = other.currPlayer;
        mask = other.mask;
    }

    /**
     * Constructor for Board
     * @param connectFour
     * Takes in an array characters that represents a connect four board.
     * @param turn
     * Sets the turn to whoever's turn it is
     */
    public Board(char[] connectFour, char turn)
    {
        if (turn == ConnectFour.RED)
            redTurn = true;

        currPlayer = 0;
        mask = 0;

        int column = 0;
        int row = ROWS - 1;

        for (int index = 0; index < (ROWS + 1) * COLUMNS; index++)
        {
            if (row == -1)
            {
                column++;
                row = ROWS;
            }
            else
            {
                char pos = connectFour[column + row * COLUMNS];

                if (pos != ConnectFour.EMPTY)
                {
                    mask = insert(mask, index);
                    if ((redTurn && pos == ConnectFour.RED) || (!redTurn && pos == ConnectFour.YELLOW))
                        currPlayer = insert(currPlayer, index);
                }
            }

            row--;
        }
    }

    /**
     * canPlay method
     * @param column
     * Checks if the column passed is actually a playable move (not full)
     * @return boolean
     * returns true or false if it is a playable column or not
     */
    public boolean canPlay(int column)
    {
        return (mask & topMask(column)) == 0;
    }

    /**
     * play method
     * @param column
     * Plays a piece into the given column
     * The color is determined by whose turn it currently is
     */
    public void play(int column)
    {
        currPlayer ^= mask;
        mask |= ((mask + bottomMask(column)) & columnMask(column));

        redTurn = !redTurn;
    }

    //PUBLIC METHODS
    /**
     * isFull()
     * @return boolean
     * Returns whether or not the board is completely full
     */
    public boolean isFull() { return (BOARD_MASK ^ mask) == 0; }

    /**
     * canWinThisMove()
     * @return boolean
     * Returns whether or not red can win on their next move
     */
    public boolean canWinThisMove() { return (getWinningMoves() & getPossibleMoves()) != 0; }

    /**
     * canLoseNextMove()
     * @return boolean
     * Returns whether or not red can lose on yellow's next move
     */
    public boolean canLoseNextMove() { return (getOpponentWinningMoves() & getPossibleMoves()) != 0;}

    /**
     * isWinningMove(int column)
     * @param column
     * Which column to check
     * @return boolean
     * Returns true or false whether or not this column is a winning move or not
     */
    public boolean isWinningMove(int column) { return (getWinningMoves() & getPossibleMoves() & columnMask(column)) != 0; }

    /**
     * isBlockingMove(int column)
     * @param column
     * Which column to check
     * @return boolean
     * Returns true or false whether or not this column is a blocking move or not
     */
    public boolean isBlockingMove(int column) { return (getOpponentWinningMoves() & getPossibleMoves() & columnMask(column)) != 0; }

    //PUBLIC GETTERS
    public boolean isRedTurn() { return redTurn; }
    public long getKey() { return currPlayer + mask; }

    //EQUALS AND TOSTRING
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Board))
            return false;

        Board other = (Board) obj;

        return redTurn == other.redTurn && currPlayer == other.currPlayer && mask == other.mask;
    }
    public String toString()
    {
        String maskString = Long.toBinaryString(mask);
        String currPlayerString = Long.toBinaryString(currPlayer);

        char[][] board = new char[Board.ROWS][Board.COLUMNS];

        int column = 0;
        int row = board.length - 1;
        while (column < COLUMNS)
        {
            if (row == -1)
            {
                column++;
                row = board.length;
            }
            else
            {
                boolean isEmpty = maskString.length() > 0 ? maskString.charAt(maskString.length() - 1) == '0' : true;
                boolean isCurrPlayer = currPlayerString.length() > 0 ? currPlayerString.charAt(currPlayerString.length() - 1) == '1' : false;

                if (isEmpty)
                    board[row][column] = '.';
                else if ((redTurn && isCurrPlayer) || (!redTurn && !isCurrPlayer))
                    board[row][column] = ConnectFour.RED;
                else
                    board[row][column] = ConnectFour.YELLOW;

            }

            maskString = maskString.length() > 0 ? maskString.substring(0, maskString.length() - 1) : "";
            currPlayerString = currPlayerString.length() > 0 ? currPlayerString.substring(0, currPlayerString.length() - 1) : "";

            row--;
        }

        String output = "";
        for (row = 0; row < board.length; row++)
        {
            output += " ";
            for (column = 0; column < board[0].length; column++)
                output += board[row][column] + " ";
            output += '\n';
        }

        return output;
    }

    //PRIVATE HELPER METHODS
    private long getPossibleMoves() { return (mask + BOTTOM_MASK) & BOARD_MASK; }
    private long getWinningMoves() { return getWinningPosition(currPlayer, mask); }
    private long getOpponentWinningMoves() { return getWinningPosition(currPlayer ^ mask, mask); }

    //PRIVATE STATIC HELPER METHODS AND VARIABLES
    private static boolean canInsert(long board, int index) { return (board & (1l << index)) == 0; }
    private static long insert(long board, int index) { return board + (1l << index); }

    private static final long BOARD_MASK;
    static
    {
        long boardMask = (1l << ((ROWS+1) * COLUMNS)) - 1;
        for (int index = ROWS; index < (ROWS + 1) * COLUMNS; index += ROWS + 1)
            boardMask -= 1l << index;
        BOARD_MASK = boardMask;
    }

    private static final long BOTTOM_MASK;
    static
    {
        long bottomMask = 0;
        for (int index = 0; index < (ROWS + 1) * COLUMNS; index += ROWS + 1)
            bottomMask = insert(bottomMask, index);
        BOTTOM_MASK = bottomMask;
    }

    private static long columnMask(int col) { return ((1l << ROWS) - 1) << col * (ROWS + 1); }
    private static long topMask(int col) { return 1l << ((ROWS - 1) + col * (ROWS + 1)); }
    private static long bottomMask(int col) { return 1l << col * (ROWS + 1); }

    private static long getWinningPosition(long currPlayer, long mask)
    {
        // vertical;
        long r = (currPlayer << 1) & (currPlayer << 2) & (currPlayer << 3);

        //horizontal
        long p = (currPlayer << (ROWS + 1)) & (currPlayer << 2 * (ROWS + 1));
        r |= p & (currPlayer << 3 * (ROWS + 1));
        r |= p & (currPlayer >> (ROWS + 1));
        p = (currPlayer >> (ROWS + 1)) & (currPlayer >> 2 * (ROWS + 1));
        r |= p & (currPlayer << (ROWS + 1));
        r |= p & (currPlayer >> 3 * (ROWS + 1));

        //diagonal 1
        p = (currPlayer << ROWS) & (currPlayer << 2 * ROWS);
        r |= p & (currPlayer << 3 * ROWS);
        r |= p & (currPlayer >> ROWS);
        p = (currPlayer >> ROWS) & (currPlayer >> 2 * ROWS);
        r |= p & (currPlayer << ROWS);
        r |= p & (currPlayer >> 3 * ROWS);

        //diagonal 2
        p = (currPlayer << (ROWS + 2)) & (currPlayer << 2 * (ROWS + 2));
        r |= p & (currPlayer << 3 * (ROWS + 2));
        r |= p & (currPlayer >> (ROWS + 2));
        p = (currPlayer >> (ROWS + 2)) & (currPlayer >> 2 * (ROWS + 2));
        r |= p & (currPlayer << (ROWS + 2));
        r |= p & (currPlayer >> 3 * (ROWS + 2));

        return r & (BOARD_MASK ^ mask);
    }
}
