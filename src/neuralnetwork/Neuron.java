package neuralnetwork;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyl
 */

import java.io.Serializable;

public class Neuron implements Serializable
{
    private static final long serialVersionUID = 3884689762876358004L;

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

    public boolean equals(Object o) {
        if (! (o instanceof  Neuron) ) {
            return false;
        }
        Neuron oN = (Neuron) o;

        if (oN.getBias() != this.getBias()) {
            return false;
        }

        if (oN.getIndex() != this.getIndex()) {
            return false;
        }

        if (oN.getdEdO() != this.getdEdO()) {
            return false;
        }

        if (oN.getdEdI() != this.getdEdI()) {
            return false;
        }

        if (oN.getInput() != this.getInput()) {
            return false;
        }

        if (oN.getOutput() != this.getOutput()) {
            return false;
        }

        return true;
    }
}