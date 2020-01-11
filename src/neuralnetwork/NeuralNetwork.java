package neuralnetwork;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyle
 */

import graph.Edge;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class NeuralNetwork
{
    private static final double LEARNING_RATE = .01;
    private static final double BIAS_LEARNING_RATE =.0025;

    private int inputCount;
    private int outputCount;

    private Neuron[] neurons;
    private WeightNetwork weights;

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
        weights = new WeightNetwork(totalNeurons);

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

    //default constructor get data from data file path
    public NeuralNetwork(String filePath)
    {
        readNetwork(filePath);
    }

    //calculates output based on input
    public double[] calculate(double[] input)
    {
        //forward propagates through NeuralNetwork to calculate output
        forwardPropagate(input);

        //determines output using the outputs of output Neurons
        double[] output = new double[outputCount];
        int initialOutputIndex = neurons.length - outputCount;
        for(int i = 0; i < outputCount; i++)
            output[i] = neurons[initialOutputIndex + i].getOutput();

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
        for (Neuron neuron: neurons)
        {
            //finding the index of neuron
            int index = neuron.getIndex();

            //if the neuron is an input Neuron, set the correct index of input as the Neuron's output
            if (index < inputCount)
            {
                neuron.setOutput(input[index]);
            }
            //otherwise calculate its input and output
            else
            {
                //calculating Neuron's input
                double neuronInput = neuron.getBias();
                for (Iterator<Edge> previousWeights = weights.previousWeights(index); previousWeights.hasNext();)
                {
                    Edge weight = previousWeights.next();
                    neuronInput += weight.getWeight() * neurons[weight.getDestination()].getOutput();

                }

                //setting the Neuron's input and output
                neuron.setInput(neuronInput);
                neuron.setOutput(activation(neuronInput));
            }
        }
    }

    //back propagates through NeuralNetwork
    private void backPropagate(double[] targetOutput)
    {
        int initialOutputIndex = neurons.length - outputCount;
        int targetOutputIndex = targetOutput.length - 1;

        //iterating through neurons backwards
        for (int index = neurons.length - 1; index >= 0; index--)
        {
            //finding the Neuron at the current index
            Neuron neuron = neurons[index];

            //if the neuron is an output Neuron
            //set the Neuron's dError/dOuput, dError/dInput, and bais using the targerOutput
            if (index >= initialOutputIndex)
                neuron.setdEdO(errorDerivative(neuron.getOutput(), targetOutput[targetOutputIndex--]));
            //otherwise the neuron is not an output Neuron
            else
            {
                //set weight values for all weights attached to neuron
                double dEdO = 0.0;
                for (Iterator<Edge> nextWeights = weights.nextWeights(index); nextWeights.hasNext();)
                {
                    Edge weight = nextWeights.next();
                    Neuron nextNeuron = neurons[weight.getDestination()];

                    //calculating new weight value
                    double newWeightValue = weight.getWeight() - LEARNING_RATE * neuron.getOutput() * nextNeuron.getdEdI();

                    //setting the weight of current weight and its reverse weight
                    weight.setWeight(newWeightValue);
                    Edge reverseWeight = weights.getWeight(weight.getDestination(), weight.getSource());
                    reverseWeight.setWeight(newWeightValue);

                    dEdO += newWeightValue * nextNeuron.getdEdI();
                }

                //set the Neuron's dError/dOuput
                neuron.setdEdO(dEdO);
            }

            //set Neuruon's dError/dInput, and bias
            neuron.setdEdI(activationDerivative(neuron.getInput()) * neuron.getdEdO());
            neuron.setBias(neuron.getBias() - BIAS_LEARNING_RATE * neuron.getdEdI());
        }
    }

    public double getAccuracy(ArrayList<double[]> inputs, ArrayList<double[]> outputs)
    {
        int passes = 0;

        for (int i = 0; i < inputs.size(); i++)
        {
            //getting the prediction arrays from inputs array list and storing a copy of them
            double[] prediction = calculate(inputs.get(i));

            int outputIndex = maxIndex(outputs.get(i));
            int predictionIndex = maxIndex(prediction);

            //if prediction is valid count it as a pass
            if (predictionIndex == outputIndex)
                passes++;
        }

        return (double) passes / inputs.size();
    }

    public double getMeanSquaredError(ArrayList<double[]> inputs, ArrayList<double[]> outputs)
    {
        double meanSquaredError = 0;

        for (int i = 0; i < inputs.size(); i++)
        {
            //getting the prediction arrays from inputs array list and storing a copy of them
            double[] prediction = calculate(inputs.get(i));

            for (int j = 0; j < prediction.length; j++)
                meanSquaredError += error(outputs.get(i)[j], prediction[j]);

        }

        return 2 * meanSquaredError / inputs.size();
    }

    //saves network data
    public void saveNetwork(String filePath)
    {
        ObjectOutputStream binaryOutput = null;
        try
        {
            binaryOutput = new ObjectOutputStream(new FileOutputStream(filePath));
            binaryOutput.writeObject(new NetworkData(neurons, weights, inputCount, outputCount));

            binaryOutput.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //reads network data
    public void readNetwork(String filePath)
    {
        ObjectInputStream binaryInput = null;

        try
        {
            binaryInput = new ObjectInputStream(new FileInputStream(filePath));
            NetworkData data = (NetworkData) binaryInput.readObject();
            neurons = data.neurons;
            weights = data.weights;
            inputCount = data.inputCount;
            outputCount = data.outputCount;

            binaryInput.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    //activation function
    public static double activation(double x)
    {
        return 1 / (1 + Math.exp(-x));
    }

    //derivative of activation function
    private static double activationDerivative(double x)
    {
        return activation(x) * (1 - activation(x));
    }

    //error function
    private static double error(double output, double targetOutput)
    {
        //1/2(output - targetOutput)^2
        double diff = output - targetOutput;
        return 0.5 * diff * diff;
    }

    //derivative of error function
    private static double errorDerivative(double output, double targetOutput)
    {
        return output - targetOutput;
    }

    //returns max index of array
    public static int maxIndex(double[] array)
    {
        int index = 0;
        double max = Double.MIN_VALUE;

        for (int i = 0; i < array.length; i++)
        {
            if (array[i] > max)
            {
                max = array[i];
                index = i;
            }
        }
        return index;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }
    public WeightNetwork getWeightNetwork() {
        return weights;
    }
}