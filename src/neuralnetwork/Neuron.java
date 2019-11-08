package neuralnetwork;

import java.io.Serializable;

public class Neuron implements Serializable
{
    private double bias;
    private double input;
    private double output;
    private double dEdI;
    private double dEdO;

    private int index;

    public Neuron(int index) {
        bias = 0.0;
        this.index = index;
    }

    //GETTERS AND SETTERS
    public double getBias() {
        return bias;
    }
    public void setBias(double bias) {
        this.bias = bias;
    }

    public double getInput() {
        return input;
    }
    public void setInput(double input) {
        this.input = input;
    }

    public double getOutput() {
        return output;
    }
    public void setOutput(double output) {
        this.output = output;
    }

    public double getdEdI() {
        return dEdI;
    }
    public void setdEdI(double dEdI) { this.dEdI = dEdI; }

    public double getdEdO() {
        return dEdO;
    }
    public void setdEdO(double dEdO) {
        this.dEdO = dEdO;
    }

    public int getIndex() {
        return index;
    }

}