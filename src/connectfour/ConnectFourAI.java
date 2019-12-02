package connectfour;

import java.util.Arrays;

public class ConnectFourAI
{
    private static final int TRANSPOSITION_TABLE_SIZE = 16780523; //prime number

    private static final int[] MOVE_ORDER;
    static
    {
        int[] moveOrder = new int[Board.COLUMNS];

        int index = 0;
        int bestMove = Board.COLUMNS / 2;
        moveOrder[index++] = bestMove;

        for (int i = 1; i <= (Board.COLUMNS - 1) / 2 ; i++)
        {
            moveOrder[index++] = bestMove - i;
            moveOrder[index++] = bestMove + i;
        }

        MOVE_ORDER = moveOrder;
    }

    private final int MAX_DEPTH;

    private TranspositionTable tt;

    public ConnectFourAI(int maxDepth)
    {
        MAX_DEPTH = maxDepth;
    }

    public int getBestMove(Board game)
    {
        tt = new TranspositionTable(TRANSPOSITION_TABLE_SIZE);
        return miniMax(game, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private int miniMax(Board game, int depth, int alpha, int beta)
    {
        if (game.isFull())
            return 0;

        int alphaOrig = alpha;
        int betaOrig = beta;

        //checking transposition table for moves
        long key = game.getKey();
        long ttKey = tt.getKey(key);
        if (key == ttKey)
        {
            byte ttFlag = tt.getFlag(key);
            int ttValue = tt.get(key);

            if (ttFlag == TranspositionTable.EXACT)
                return ttValue;
            else if (ttFlag == TranspositionTable.LOWER_BOUND)
                alpha = Math.max(alpha, ttValue);
            else if (ttFlag == TranspositionTable.UPPER_BOUND)
                beta = Math.min(beta, ttValue);

            if (alpha >= beta)
                return ttValue;
        }

        boolean canWinThisMove = game.canWinThisMove();
        Board[] nextGames = new Board[Board.COLUMNS];

        //no winning moves
        if (!canWinThisMove)
        {
            boolean canLoseNextMove = game.canLoseNextMove();
            for (int move : MOVE_ORDER)
            {
                if (game.canPlay(move) && (!canLoseNextMove || (canLoseNextMove && game.isBlockingMove(move))))
                {
                    nextGames[move] = new Board(game);
                    nextGames[move].play(move);

                    if (canLoseNextMove)
                        break;
                }
            }
        }
        //winning move
        else
        {
            //can win on first move
            if (depth == 0)
            {
                for (int move : MOVE_ORDER)
                    if (game.canPlay(move) && game.isWinningMove(move))
                        return move;
            }

            return (game.isRedTurn() ? MAX_DEPTH - depth : depth - MAX_DEPTH);
        }

        //if depth is reached return
        if (depth >= MAX_DEPTH)
            return 0;

        int bestMove = -1;
        //maximizing player: RED
        if (game.isRedTurn())
        {
            int value = Integer.MIN_VALUE;
            for (int move : MOVE_ORDER)
            {
                Board nextGame = nextGames[move];
                if (nextGame != null)
                {
                    int miniMax = miniMax(nextGame, depth + 1, alpha, beta);

                    if (miniMax > value)
                    {
                        value = miniMax;
                        bestMove = move;
                    }

                    //alpha beta pruning
                    alpha = Math.max(alpha, value);
                    if (alpha >= beta)
                        break;
                }
            }
            if (depth == 0)
            {
                if (value == 0)
                    return -1;

                return bestMove;
            }

            //adding game to transposition table
            if (value <= alphaOrig)
                tt.add(game.getKey(), value, TranspositionTable.UPPER_BOUND);
            else if (value >= betaOrig)
                tt.add(game.getKey(), value, TranspositionTable.LOWER_BOUND);
            else
                tt.add(game.getKey(), value, TranspositionTable.EXACT);

            return value;
        }
        //minimizing player: YELLOW
        else
        {
            int value = Integer.MAX_VALUE;
            for (Board nextGame: nextGames)
            {
                if (nextGame != null)
                {
                    value = Math.min(value, miniMax(nextGame,depth + 1, alpha, beta));
                    beta = Math.min(beta, value);

                    //alpha beta pruning
                    if (alpha >= beta)
                        break;
                }
            }

            //adding game to transposition table
            if (value <= alphaOrig)
                tt.add(game.getKey(), value, TranspositionTable.UPPER_BOUND);
            else if (value >= betaOrig)
                tt.add(game.getKey(), value, TranspositionTable.LOWER_BOUND);
            else
                tt.add(game.getKey(), value, TranspositionTable.EXACT);

            return value;
        }
    }

    private class TranspositionTable
    {
        private long[] keys;
        private int[] values;
        private byte[] flags;

        private static final byte EXACT = 1;
        private static final byte UPPER_BOUND = Byte.MAX_VALUE;
        private static final byte LOWER_BOUND = Byte.MIN_VALUE;

        private TranspositionTable(int size)
        {
            keys = new long[size];
            Arrays.fill(keys, -1);

            values = new int[size];
            flags = new byte[size];
        }

        public void add(long key, int value, byte flag)
        {
            keys[(int) (key % keys.length)] = key;
            values[(int) (key % keys.length)] = value;
            flags[(int) (key % keys.length)] = flag;
        }

        public int get(long key) { return values[(int) (key % keys.length)]; }
        public long getKey(long key) { return keys[(int) (key % keys.length)]; }
        public byte getFlag(long key) { return flags[(int) (key % keys.length)]; }
    }
}
