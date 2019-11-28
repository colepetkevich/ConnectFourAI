package connectfour;

import java.util.Arrays;

public class ConnectFourAI
{
    private static final int RED_VALUE = 100;
    private static final int YELLOW_VALUE = -100;
    private static final int TRANSPOSITION_TABLE_SIZE = 1048576;

    private final int MAX_DEPTH;

    private TranpositionTable tt;

    public ConnectFourAI(int maxDepth)
    {
        MAX_DEPTH = maxDepth;
    }

    public int getBestMove(char[] connectFour)
    {
        tt = new TranpositionTable(TRANSPOSITION_TABLE_SIZE);
        return miniMax(connectFour, true, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private int miniMax(char[] connectFour, boolean redTurn, int depth, int alpha, int beta)
    {
//        if (tt.contains(connectFour, redTurn))
//            return 0;
//        else
//            tt.add(connectFour, redTurn);

        int bestMove = -1;
        int forcedMove = ConnectFour.checkForcedMove(connectFour, redTurn ? ConnectFour.RED : ConnectFour.YELLOW);
        char[][] nextGames = new char[ConnectFour.COLUMNS][];

        //no forced moves
        if (forcedMove < 0)
        {
            for (int move = 0; move < ConnectFour.COLUMNS; move++)
            {
                if (ConnectFour.canInsert(move, connectFour))
                {
                    nextGames[move] = Arrays.copyOf(connectFour, connectFour.length);
                    ConnectFour.insert(move, nextGames[move], redTurn ? ConnectFour.RED : ConnectFour.YELLOW);
                }
            }
        }
        //winning move
        else if (forcedMove < ConnectFour.COLUMNS)
        {
            if (depth == 0)
                return forcedMove;

            return (redTurn ? RED_VALUE + (MAX_DEPTH - depth) : YELLOW_VALUE - (MAX_DEPTH - depth));
        }
        //blocking move
        else
        {
            int blockingMove = forcedMove - ConnectFour.COLUMNS;
            nextGames[blockingMove] = Arrays.copyOf(connectFour, connectFour.length);
            ConnectFour.insert(blockingMove, nextGames[blockingMove],
                    redTurn ? ConnectFour.RED : ConnectFour.YELLOW);
        }

        //if depth is reached return
        if (depth >= MAX_DEPTH)
        {
            return 0;
        }

        //maximizing player
        if (redTurn)
        {
            int value = Integer.MIN_VALUE;
            for (int move = 0; move < ConnectFour.COLUMNS; move++)
            {
                char[] nextGame = nextGames[move];
                if (nextGame != null)
                {
                    int miniMax = miniMax(nextGame, false, depth + 1, alpha, beta);
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

            return value;
        }
        //minimizing player
        else
        {
            int value = Integer.MAX_VALUE;
            for (char[] nextGame: nextGames)
            {
                if (nextGame != null)
                {
                    value = Math.min(value, miniMax(nextGame,true, depth + 1, alpha, beta));
                    alpha = Math.min(beta, value);

                    //alpha beta pruning
                    if (alpha >= beta)
                        break;
                }
            }

            return value;
        }
    }

    private class TranpositionTable
    {
        private long[] table;

        private TranpositionTable(int size)
        {
            table = new long[size];
            Arrays.fill(table, -1);
        }

        public void add(char[] key, boolean redTurn)
        {
            long zor = zor(key, redTurn);

            table[(int) (zor % table.length)] = zor;
        }

        public boolean contains(char[] key, boolean redTurn)
        {
            long zor = zor(key, redTurn);

            return table[(int) (zor % table.length)] == zor;
        }

        private long zor(char[] key, boolean redTurn)
        {
            long zor = 0;
            char turn = !redTurn ? ConnectFour.RED : ConnectFour.YELLOW;

            for (int i = 0; i < key.length; i++)
                zor += (key[i] == turn ? 1 : 0) * pow(2, i);

            return zor;
        }

        private long pow(long base, long exponent)
        {
            if (exponent == 0)
                return 1;

            long value = 1;
            for (int i = 0; i < exponent; i++)
                value *= base;

            return value;
        }


    }
}
