package connectfourtest;

import connectfour.Board;
import connectfour.ConnectFourAI;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class ConnectFourAITest
{
    private static final Board EMPTY_BOARD = new Board();
    private static final int NO_MOVE = -1;
    private static final int[] BEST_MOVE_CASES = new int[] { 4, 4, 2, 2 };

    private static final int MAX_DEPTH = 10;

    private ConnectFourAI ai;
    private Board board;

    @Before
    public void createAI()
    {
        ai = new ConnectFourAI(MAX_DEPTH);
        board = new Board(EMPTY_BOARD);
    }

    @Test
    public void getNoMove()
    {
        assertEquals(ai.getBestMove(board), NO_MOVE);
    }

    @Test
    public void getBestMoveOne()
    {
        board.play(3); //RED
        board.play(0); //YELLOW
        board.play(3); //RED
        board.play(0); //YELLOW
        board.play(2); //RED
        board.play(5); //YELLOW
        board.play(2); //RED
        board.play(2); //YELLOW

        assertEquals(ai.getBestMove(board), BEST_MOVE_CASES[0]);
    }

    @Test
    public void getBestMoveTwo()
    {
        board.play(3); //RED
        board.play(3); //YELLOW
        board.play(2); //RED
        board.play(6); //YELLOW

        assertEquals(ai.getBestMove(board), BEST_MOVE_CASES[1]);
    }

    @Test
    public void getBestMoveThree()
    {
        board.play(3); //RED
        board.play(4); //YELLOW
        board.play(3); //RED
        board.play(4); //YELLOW
        board.play(3); //RED
        board.play(3); //YELLOW
        board.play(4); //RED
        board.play(5); //YELLOW
        board.play(4); //RED
        board.play(6); //YELLOW

        assertEquals(ai.getBestMove(board), BEST_MOVE_CASES[2]);
    }

    @Test
    public void getBestMoveFour()
    {
        board.play(3); //RED
        board.play(6); //YELLOW
        board.play(3); //RED
        board.play(6); //YELLOW
        board.play(3); //RED
        board.play(3); //YELLOW
        board.play(2); //RED
        board.play(1); //YELLOW
        board.play(2); //RED
        board.play(5); //YELLOW
        board.play(4); //RED
        board.play(4); //YELLOW

        assertEquals(ai.getBestMove(board), BEST_MOVE_CASES[3]);
    }
}
