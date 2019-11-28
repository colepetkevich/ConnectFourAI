package connectfour;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataSetHandler
{
    public static final double TRANSLATED_RED = 1.0;
    public static final double TRANSLATED_YELLOW = -1.0;
    public static final double TRANSLATED_EMPTY = 0.0;

    private static final String GAME_DATA_PATH = "res/files/GameData.txt";

    private ArrayList<char[]> boards;
    private ArrayList<Integer> moves;

    public DataSetHandler()
    {
        boards = new ArrayList<char[]>();
        moves = new ArrayList<Integer>();
    }

    public void savaData()
    {
        BufferedWriter outputFile = null;

        try
        {
            File file = new File(GAME_DATA_PATH);

            outputFile = new BufferedWriter(new FileWriter(GAME_DATA_PATH, file.exists()));

            for (int i = 0; i < boards.size(); i++)
                outputFile.write(Arrays.toString(boards.get(i)) + " - " + moves.get(i) + "\n");

            outputFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //clear data once it has been saved
        clearData();
    }

    public GameData readData()
    {
        GameData gameData = new GameData();
        BufferedReader inputFile = null;

        try
        {
            inputFile = new BufferedReader(new FileReader(GAME_DATA_PATH));

            //read all lines from inputFile
            String line;
            while ((line = inputFile.readLine()) != null)
            {
                //determining input
                char[] board = new char[ConnectFour.COLUMNS * ConnectFour.ROWS];
                for (int i = 0; i < board.length; i++)
                    board[i] = line.charAt(1 + 3 * i);

                double[] input = translateBoard(board);

                //determining output
                double[] output = new double[ConnectFour.COLUMNS];
                Arrays.fill(output, 0.0);
                output[Integer.parseInt(line.charAt(line.length() - 1) + "")] = 1.0;

                //adding input and output to gameData
                gameData.add(input, output);
            }

            inputFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return gameData;
    }

    public void addData(char[] board, int move)
    {
        boards.add(board);
        moves.add(move);

        addAllOrientations(board, move);
    }

    public void clearData()
    {
        boards.clear();
        moves.clear();
    }

    private void addAllOrientations(char[] board, int move)
    {
        final int COLUMNS = ConnectFour.COLUMNS;

        char[] reflectedBoard = new char[board.length];
        int reflectedMove = COLUMNS - move - 1;

        for (int i = 0; i < board.length; i++)
            reflectedBoard[COLUMNS * (i / COLUMNS) + (COLUMNS - i % COLUMNS - 1)] = board[i];

        //only add relected board and move if board is not a duplicate
        if (!Arrays.equals(board, reflectedBoard))
        {
            boards.add(reflectedBoard);
            moves.add(reflectedMove);
        }
    }

    public class GameData
    {
        private ArrayList<double[]> inputs;
        private ArrayList<double[]> outputs;

        public GameData()
        {
            inputs = new ArrayList<double[]>();
            outputs = new ArrayList<double[]>();
        }

        private void add(double[] input, double[] output)
        {
            inputs.add(input);
            outputs.add(output);
        }

        public int size() { return inputs.size(); }

        public ArrayList<double[]> getInputs() { return inputs; }
        public ArrayList<double[]> getOutputs() { return outputs; }
    }

    //static methods
    public static double[] translateBoard(char[] board)
    {
        //determining translation
        double[] translation = new double[board.length];
        for (int i = 0; i < board.length; i++)
        {
            switch (board[i])
            {
                case ConnectFour.RED:
                    translation[i] = TRANSLATED_RED;
                    break;
                case ConnectFour.YELLOW:
                    translation[i] = TRANSLATED_YELLOW;
                    break;
                default:
                    translation[i] = TRANSLATED_EMPTY;
                    break;
            }
        }

        return translation;
    }

    private static double translatedChar(char c)
    {
        if (c == ConnectFour.RED)
            return TRANSLATED_RED;
        else if (c == ConnectFour.YELLOW)
            return TRANSLATED_YELLOW;

        return TRANSLATED_EMPTY;
    }
}
