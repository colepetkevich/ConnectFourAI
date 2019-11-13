package neuralnetwork;

import game.DataSetHandler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Trainer
{
    public static void main(String[] args)
    {
        NeuralNetwork nn = new NeuralNetwork(42, 7);

        System.out.println("Training Began at " + LocalTime.now());
        System.out.println();

        DataSetHandler dataSetHandler = new DataSetHandler();
        DataSetHandler.GameData gameData = dataSetHandler.readData();

        for (int i = 0; i < 100000; i++)
        {
            ArrayList<double[]> inputsCopy = (ArrayList<double[]>) gameData.getInputs().clone();
            ArrayList<double[]> outputsCopy = (ArrayList<double[]>) gameData.getOuputs().clone();

            while (inputsCopy.size() > 0)
            {
                int randIndex = (int) (inputsCopy.size() * Math.random());
                nn.train(inputsCopy.remove(randIndex), outputsCopy.remove(randIndex));
            }
        }

        System.out.println("Final Accuracy: " + nn.getAccuracy(gameData.getInputs(), gameData.getOuputs()));
        System.out.println("Final Mean Squared Error: " + nn.getMeanSquaredError(gameData.getInputs(), gameData.getOuputs()));
        System.out.println("Training Ended at " + LocalTime.now());
    }
}
