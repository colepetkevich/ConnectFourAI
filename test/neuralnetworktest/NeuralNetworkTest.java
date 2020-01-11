package neuralnetworktest;

import neuralnetwork.NeuralNetwork;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class NeuralNetworkTest {

    private static final String NEURAL_NETWORK_PATH = "res/files/NeuralNetwork.dat";
    public NeuralNetwork nn;

    @Test
    public void calculateTest() {
        double[] exampleBoard = new double[42];
        for (int i = 0; i < exampleBoard.length; i++) {
            exampleBoard[i] = 0;
        }
        nn = new NeuralNetwork(NEURAL_NETWORK_PATH);
        double[] prediction = nn.calculate(exampleBoard);

        assertTrue("Test calculateTest failed. This checked if the computer thought the middle column" +
                "was the best for the first move. If this failed, the computer wanted to start somewhere " +
                "no in the middle and therefore failed", prediction[3] >= 0.9);
    }

}