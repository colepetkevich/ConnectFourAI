package temp;

import connectfour.ConnectFour;

import java.time.LocalTime;
import java.util.Arrays;

public class AIDemo
{
    public static void main(String[] args)
    {
        char[] connectFour = new char[ConnectFour.ROWS * ConnectFour.COLUMNS];
        Arrays.fill(connectFour, ConnectFour.EMPTY);

        ConnectFour.insert(3, connectFour, ConnectFour.RED);
        ConnectFour.insert(0, connectFour, ConnectFour.YELLOW);
        ConnectFour.insert(5, connectFour, ConnectFour.RED);
        ConnectFour.insert(2, connectFour, ConnectFour.YELLOW);
        ConnectFour.insert(2, connectFour, ConnectFour.RED);
        ConnectFour.insert(1, connectFour, ConnectFour.YELLOW);
        ConnectFour.insert(3, connectFour, ConnectFour.RED);
        ConnectFour.insert(1, connectFour, ConnectFour.YELLOW);

        ConnectFourAI connectFourAI = new ConnectFourAI(9);

        System.out.println("Program Began: " + LocalTime.now());
        System.out.println(connectFourAI.getBestMove(connectFour));

        System.out.println("Program Complete: " + LocalTime.now());
    }
}
