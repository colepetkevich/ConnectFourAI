package neuralnetwork;

import game.DataSetHandler;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;

public class Trainer
{
    private static final String NEURAL_NETWORK_PATH = "res/files/NeuralNetwork.dat";

    public static void main(String[] args)
    {
        NeuralNetwork nn = null;

        if (new File(NEURAL_NETWORK_PATH).exists())
            nn = new NeuralNetwork(NEURAL_NETWORK_PATH);
            //otherwise create a new neural network
        else
            nn = new NeuralNetwork(42, 7);

        DataSetHandler dataSetHandler = new DataSetHandler();
        DataSetHandler.GameData gameData = dataSetHandler.readData();

        System.out.println("Training Began at " + LocalTime.now());
        System.out.println("Initial Accuracy: " + nn.getAccuracy(gameData.getInputs(), gameData.getOuputs()));
        System.out.println("Initial Mean Squared Error: " + nn.getMeanSquaredError(gameData.getInputs(), gameData.getOuputs()));
        System.out.println();

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

        //saving current neural network to binary file
        nn.saveNetwork(NEURAL_NETWORK_PATH);

        System.out.println("Training Ended at " + LocalTime.now());
        System.out.println("Final Accuracy: " + nn.getAccuracy(gameData.getInputs(), gameData.getOuputs()));
        System.out.println("Final Mean Squared Error: " + nn.getMeanSquaredError(gameData.getInputs(), gameData.getOuputs()));
    }
}
