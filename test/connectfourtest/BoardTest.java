package connectfourtest;

import connectfour.Board;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest
{
    private static final String SINGLE_PLAY_BOARD = " . . . . . . . \n . . . . . . . \n . . . . . . . \n . . . . . . . \n . . . . . . . \n . . . R . . . \n";
    private static final String MULTIPLE_PLAY_BOARD = " . . . . . . . \n . . . . . . . \n . . . . . . . \n . . . Y . . . \n . . . Y . . . \n R R R Y . . . \n";

    private static final int SINGLE_PLAY = 3;
    private static final int[] MULTIPLE_PLAYS = new int[] { 0, 3, 2, 3, 1, 3 };

    private static final Board EMPTY_BOARD = new Board();

    private Board board;

    @Before
    public void createBoard()
    {
        board = new Board(EMPTY_BOARD);
    }

    @Test
    public void testEmpty()
    {
        assertEquals(board, EMPTY_BOARD);
    }

    @Test
    public void testCanPlay()
    {
        for (int i = 0; i < Board.ROWS; i++)
        {
            assertTrue(board.canPlay(SINGLE_PLAY));
            board.play(SINGLE_PLAY);
        }
    }

    @Test
    public void testCannotPlay()
    {
        for (int i = 0; i < Board.ROWS; i++)
            board.play(SINGLE_PLAY);

        assertFalse(board.canPlay(SINGLE_PLAY));
    }

    @Test
    public void testSinglePlay()
    {
        board.play(SINGLE_PLAY);

        assertEquals(board.toString(), SINGLE_PLAY_BOARD);
    }

    @Test
    public void testMultiplePlay()
    {
        for (int i = 0; i < MULTIPLE_PLAYS.length; i++)
            board.play(MULTIPLE_PLAYS[i]);

        assertEquals(board.toString(), MULTIPLE_PLAY_BOARD);
    }

    @Test
    public void testTurn()
    {
        boolean isRedTurn = true;

        for (int i = 0; i < Board.ROWS; i++)
        {
            assertEquals(isRedTurn, board.isRedTurn());

            board.play(SINGLE_PLAY);
            isRedTurn = !isRedTurn;
        }
    }
}
