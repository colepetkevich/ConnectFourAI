package neuralnetwork;

import java.io.Serializable;

public class NetworkData implements Serializable
{
    public Neuron[] neurons;
    public WeightNetwork weights;
    public int inputCount;
    public int outputCount;

    public NetworkData(Neuron[] neurons, WeightNetwork weights, int inputCount, int outputCount)
    {
        this.neurons = neurons;
        this.weights = weights;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
    }
}
