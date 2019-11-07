package graphtest;

import graph.Edge;
import graph.MapGraph;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class GraphTest
{
    private MapGraph mapGraph;

    @Test
    public void testSingleInsertion()
    {
        mapGraph = new MapGraph(4, false);
        int src = 3;
        int dest = 0;
        Edge edgeOne = new Edge(src, dest);
        mapGraph.insert(edgeOne);

        assertEquals(edgeOne, mapGraph.getEdge(src, dest));
    }

    @Test
    public void testMultipleInsertions()
    {
        mapGraph = new MapGraph(8, false);

        int numV = mapGraph.getNumV();

        //tests 20 insertions
        for (int i = 0; i < 20; i++)
        {
            int src = (int) (numV * Math.random());
            int dest = (int) (numV * Math.random());

            //make sure src and dest are different
            if (src != dest)
            {
                Edge edge = new Edge(src, dest, Math.random());
                mapGraph.insert(edge);

                assertEquals(edge, mapGraph.getEdge(src, dest));
            }
            //if src and dest are not different do a new test
            else
                i--;
        }
    }

    @Test
    public void testIsEdge()
    {
        mapGraph = new MapGraph(4, false);

        int src = 1;
        int dest = 2;
        mapGraph.insert(new Edge(src, dest));

        assertTrue(mapGraph.isEdge(src, dest));
    }

    @Test
    public void testIsNotEdge()
    {
        mapGraph = new MapGraph(4, false);

        int src = 1;
        int dest = 2;
        mapGraph.insert(new Edge(src, dest));

        assertFalse(mapGraph.isEdge(1, 4));
    }

    @Test
    public void testDirected()
    {
        mapGraph = new MapGraph(4, true);

        int src = 3;
        int dest = 2;
        mapGraph.insert(new Edge(src, dest));

        assertFalse(mapGraph.isEdge(dest, src));
    }

    @Test
    public void testNonDirected()
    {
        mapGraph = new MapGraph(4, false);

        int src = 1;
        int dest = 3;
        mapGraph.insert(new Edge(src, dest));

        assertTrue(mapGraph.isEdge(dest, src));
    }

    @Test
    public void testEdgeIterator()
    {
        mapGraph = new MapGraph(12, true);

        int numV = mapGraph.getNumV();
        int src = 7;
        int numberOfEdges = 5;
        LinkedList<Integer> previousDest = new LinkedList();

        //inserting 5 random edges at SRC
        for (int i = 0; i < numberOfEdges; i++)
        {
            int dest = (int) (numV * Math.random());

            //make sure src and dest are different and that current edge was not already created
            if (src != dest && !previousDest.contains(dest))
            {
                previousDest.add(dest);

                Edge edge = new Edge(src, dest, Math.random());
                mapGraph.insert(edge);
            }
            //if src and dest are not different do a new test
            else
                i--;
        }

        Iterator<Edge> edgeIterator = mapGraph.edgeIterator(src);
        int edgeCount = 0;

        while (edgeIterator.hasNext())
        {
            edgeIterator.next();
            edgeCount++;
        }

        assertEquals(numberOfEdges, edgeCount);
    }


}
