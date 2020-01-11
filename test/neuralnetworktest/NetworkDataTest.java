package neuralnetworktest;

import neuralnetwork.NetworkData;
import neuralnetwork.Neuron;
import neuralnetwork.WeightNetwork;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NetworkDataTest {

    @Test
    public void constructorTest() {

        final Neuron[] DUMMY_NEURONS = { new Neuron(0), new Neuron(1) };
        final WeightNetwork DUMMY_WEIGHT_NETWORK = new WeightNetwork(2);
        final int DUMMY_INPUT_COUNT = 2;
        final int DUMMY_OUTPUT_COUNT = 1;

        NetworkData nd = new NetworkData(DUMMY_NEURONS, DUMMY_WEIGHT_NETWORK, DUMMY_INPUT_COUNT, DUMMY_OUTPUT_COUNT);
        assertEquals("Test constructorTest failed. Check your Neurons in NetworkData. (Also could be the " +
                "Neuron's equals method", nd.neurons[0], DUMMY_NEURONS[0]);
        assertEquals("Test constructorTest failed. Check your input count in NetworkData",
                DUMMY_INPUT_COUNT, nd.inputCount);
        assertEquals("Test constructorTest failed. Check your input count in NetworkData",
                DUMMY_OUTPUT_COUNT, nd.outputCount);
    }

}
