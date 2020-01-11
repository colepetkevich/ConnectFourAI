package connectfourtest;

import connectfour.ConnectFour;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ConnectFourTest
{
    private static final int SINGLE_INSERT = 3;
    private static final int SINGLE_INSERT_INDEX = (ConnectFour.ROWS - 1) * ConnectFour.COLUMNS + SINGLE_INSERT;

    private static final int[] MULTIPLE_INSERTS = new int[] { 0, 3, 2, 3, 1, 3 };
    private static final int[] MULTIPLE_INSERT_INDEXES = new int[] { 35, 38, 37, 31, 36, 24 };

    private static final char[] EMPTY_CONNECT_FOUR;
    static
    {
        char[] empty = new char[ConnectFour.ROWS * ConnectFour.COLUMNS];
        Arrays.fill(empty, ConnectFour.EMPTY);
        EMPTY_CONNECT_FOUR = empty;
    }

    private char[] connectFour;
    private char turn;

    @Before
    public void createConnectFour()
    {
        connectFour = Arrays.copyOf(EMPTY_CONNECT_FOUR, EMPTY_CONNECT_FOUR.length);
        turn = ConnectFour.RED;
    }

    @Test
    public void testCanInsert()
    {
        for (int i = 0; i < ConnectFour.ROWS; i++)
        {
            assertTrue(ConnectFour.canInsert(SINGLE_INSERT, connectFour));
            ConnectFour.insert(SINGLE_INSERT, connectFour, turn);
            nextTurn();
        }
    }

    @Test
    public void testCannotInsert()
    {
        for (int i = 0; i < ConnectFour.ROWS; i++)
        {
            ConnectFour.insert(SINGLE_INSERT, connectFour, turn);
            nextTurn();
        }

        assertFalse(ConnectFour.canInsert(SINGLE_INSERT, connectFour));
    }

    @Test
    public void testSingleInsert()
    {
        ConnectFour.insert(SINGLE_INSERT, connectFour, turn);

        assertEquals(connectFour[SINGLE_INSERT_INDEX], turn);

        nextTurn();
    }

    @Test
    public void testMultipleInserts()
    {
        for (int i = 0; i < MULTIPLE_INSERTS.length; i++)
        {
            ConnectFour.insert(MULTIPLE_INSERTS[i], connectFour, turn);
            assertEquals(connectFour[MULTIPLE_INSERT_INDEXES[i]], turn);
            nextTurn();
        }
    }

    private void nextTurn()
    {
        if (turn == ConnectFour.RED)
            turn = ConnectFour.YELLOW;
        else
            turn = ConnectFour.RED;
    }
}
