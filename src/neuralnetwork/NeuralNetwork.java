package neuralnetwork;

import graph.Edge;
import graph.MapGraph;

import java.util.Random;

public class NeuralNetwork
{
    private static final double LEARNING_RATE = .01;
    private static final double BIAS_LEARNING_RATE =.0025;

    private int inputCount;
    private int outputCount;

    private Neuron[] neurons;
    private MapGraph weights;

    public NeuralNetwork(int... layerCounts)
    {
        //making sure enough layers are added
        if (layerCounts.length < 2)
            throw new IllegalArgumentException("must be 2 or more layers");

        //determining inputCount and outputCount
        this.inputCount = layerCounts[0];
        this.outputCount = layerCounts[layerCounts.length - 1];

        //calculating total Neurons
        int totalNeurons = 0;
        for (int i = 0; i < layerCounts.length; i++)
            totalNeurons += layerCounts[i];

        //instantiating data structures for Neurons and weights
        neurons = new Neuron[totalNeurons];
        weights = new MapGraph(totalNeurons, false);

        //creating Neurons
        for (int i = 0; i < totalNeurons; i++)
            neurons[i] = new Neuron(i);

        //creating weights
        int src = 0;
        Random random = new Random();
        for (int i = 0; i < layerCounts.length - 1; i++)
        {
            int initialDest = src + layerCounts[i];
            for (; src < initialDest; src++)
            {
                int finalDest = initialDest + layerCounts[i + 1] - 1;
                for (int dest = initialDest; dest <= finalDest; dest++)
                    //weights have a random normally distributed weight value
                    weights.insert(new Edge(src, dest, random.nextGaussian() / Math.sqrt(layerCounts[i])));
            }
        }
    }

    //calculates output based on input
    public double[] calculate(double[] input)
    {
        //forward propagates through NeuralNetwork to calculate output
        forwardPropagate(input);

        //determines output using the outputs of output Neurons
        double[] output = new double[outputCount];
        int initialIndex = neurons.length - outputCount - 1;
        for(int i = 0; i < outputCount; i++)
            output[i] = neurons[initialIndex + i].getOutput();

        return output;
    }

    //trains Neural Network using input and targetOutput
    public void train(double[] input, double[] targetOutput)
    {
        forwardPropagate(input);
        backPropagate(targetOutput);
    }

    //forward propagates through NeuralNetwork
    private void forwardPropagate(double[] input)
    {

    }

    //back propagates through NeuralNetwork
    private void backPropagate(double[] targetOutput)
    {

    }

}
