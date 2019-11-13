package graph;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class MapGraph extends AbstractGraph
{
    private LinkedHashMap<Integer, Edge>[] outgoingEdges;

    public MapGraph(int numV, boolean isDirected)
    {
        super(numV, isDirected);
        outgoingEdges = new LinkedHashMap[numV];
        for (int i = 0; i < numV; i++) {
            outgoingEdges[i] = new LinkedHashMap<>();
        }
    }

    public void insert(Edge edge)
    {
        int source = edge.getSource();
        int dest = edge.getDestination();
        double weight = edge.getWeight();
        outgoingEdges[source].put(dest, edge);
        if (!isDirected())
        {
            Edge reverseEdge = new Edge(dest, source, weight);
            outgoingEdges[dest].put(source, reverseEdge);
        }
    }

    public boolean isEdge(int source, int dest)
    {
        return outgoingEdges[source].containsKey(dest);
    }
    public Edge getEdge(int source, int dest)
    {
        return outgoingEdges[source].get(dest);
    }

    public Iterator<Edge> edgeIterator(int source)
    {
        return outgoingEdges[source].values().iterator();
    }
}
