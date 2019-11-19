package connectfour;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class WinSetCreator
{
    private static final String ORIGINAL_DATA_SET_PATH = "res/files/connect-four-data-set.txt";
    private static final String WIN_DATA_SET_PATH = "res/files/connect-four-win-set.txt";

    public static final double INTERPRETED_RED = 1.0;
    public static final double INTERPRETED_YELLOW = -1.0;
    public static final double INTERPRETED_TIE = 0.0;

    public static void main(String[] args)
    {
        FileWriter outputFile = null;
        //reading original data set file
        try (BufferedReader br = Files.newBufferedReader(Paths.get(ORIGINAL_DATA_SET_PATH), StandardCharsets.UTF_8))
        {
            outputFile = new FileWriter(new File(WIN_DATA_SET_PATH));

            String line;
            while ((line = br.readLine()) != null)
            {
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");

                //determining board
                char[] board = new char[42];
                for (int i = 0; i < board.length; i++)
                {
                    double move = lineScanner.nextDouble();

                    if (move == INTERPRETED_RED)
                        board[i] = ConnectFour.RED;
                    else if (move == INTERPRETED_YELLOW)
                        board[i] = ConnectFour.YELLOW;
                    else
                        board[i] = ConnectFour.EMPTY;
                }

                //determining move
                double winner = INTERPRETED_TIE;
                if (lineScanner.hasNextDouble())
                    winner = lineScanner.nextDouble();
                //if winner is red save this connectfour
                if (winner == INTERPRETED_RED)
                {
                    outputFile.write(Arrays.toString(board) + "\n");
                }
            }

            outputFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
