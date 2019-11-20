package graph;

import java.io.Serializable;

public class Edge implements Serializable
{
    private int source;
    private int destination;
    private double weight;

    public Edge(int source, int destination)
    {
        this.source = source;
        this.destination = destination;
        this.weight = 0.0;
    }

    public Edge(int source, int destination, double weight)
    {
        this(source, destination);
        this.weight = weight;
    }

    public int getSource() { return source; }
    public int getDestination() { return destination; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public boolean equals(Edge other)
    {
        return source == other.source && destination == other.destination;
    }

    public int hashCode() { return source + destination; }

    public String toString()
    {
        return "W: " + weight + " @ Src: " + source + ", Dest: " + destination;
    }
}
