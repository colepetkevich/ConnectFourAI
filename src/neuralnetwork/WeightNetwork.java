package neuralnetwork;

import graph.Edge;
import graph.MapGraph;

import java.io.Serializable;
import java.util.Iterator;

public class WeightNetwork implements Serializable
{
    private MapGraph nextWeights;
    private MapGraph previousWeights;

    public WeightNetwork(int neuronCount)
    {
        nextWeights = new MapGraph(neuronCount, true);
        previousWeights = new MapGraph(neuronCount, true);
    }

    public void insert(Edge edge)
    {
        //insert both directions
        if (edge.getSource() <= edge.getDestination())
        {
            nextWeights.insert(edge);
            previousWeights.insert(new Edge(edge.getDestination(), edge.getSource(), edge.getWeight()));
        }
        else
        {
            nextWeights.insert(new Edge(edge.getDestination(), edge.getSource(), edge.getWeight()));
            previousWeights.insert(edge);
        }
    }

    public boolean isWeight(int source, int dest) { return nextWeights.isEdge(source, dest) || previousWeights.isEdge(source, dest); }

    public Edge getWeight(int source, int dest)
    {
        Edge edge = previousWeights.getEdge(source, dest);
        if (edge == null)
            return nextWeights.getEdge(source, dest);

        return edge;
    }
    
    public Iterator<Edge> nextWeights(int neruonIndex) { return nextWeights.edgeIterator(neruonIndex); }
    public Iterator<Edge> previousWeights(int neuronIndex) { return previousWeights.edgeIterator(neuronIndex); }
}
