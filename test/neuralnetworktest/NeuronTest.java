package neuralnetworktest;

import neuralnetwork.Neuron;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NeuronTest {

    @Test
    public void getAndSetBiasTest() {
        final long TEST_NUMBER = 1;
        Neuron n = new Neuron(1);
        n.setBias(TEST_NUMBER);
        assertEquals("Test getAndSetBiasTest failed, check your bias setters/getters", TEST_NUMBER,((long) (n.getBias())));
    }

    @Test
    public void getAndSetInputTest() {
        final long TEST_NUMBER = 1;
        Neuron n = new Neuron(1);
        n.setInput(TEST_NUMBER);
        assertEquals("Test getAndSetInputTest failed, check your input setters/getters", TEST_NUMBER,((long) (n.getInput())));
    }

    @Test
    public void getAndSetOutput() {
        final long TEST_NUMBER = 1;
        Neuron n = new Neuron(1);
        n.setOutput(TEST_NUMBER);
        assertEquals("Test getAndSetOutput failed, check your output setters/getters", TEST_NUMBER,((long) (n.getOutput())));
    }

    @Test
    public void getAndSetdEdI() {
        final long TEST_NUMBER = 1;
        Neuron n = new Neuron(1);
        n.setdEdI(TEST_NUMBER);
        assertEquals("Test getAndSetdEdI failed, check your dEdI setters/getters", TEST_NUMBER,((long) (n.getdEdI())));
    }

    @Test
    public void getAndSetdEdO() {
        final long TEST_NUMBER = 1;
        Neuron n = new Neuron(1);
        n.setdEdO(TEST_NUMBER);
        assertEquals("Test getAndSetdEdO failed, check your dEdO setters/getters", TEST_NUMBER,((long) (n.getdEdO())));
    }

    @Test
    public void getIndexTest() {
        final long TEST_NUMBER = 5;
        Neuron n = new Neuron((int) TEST_NUMBER);
        assertEquals("Test getIndexTest failed. Check your constructor that takes index in as a parameter, or check getIndex", TEST_NUMBER, (long) n.getIndex());
    }
}