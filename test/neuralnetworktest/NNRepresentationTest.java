package neuralnetworktest;

import neuralnetwork.NNRepresentation;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class NNRepresentationTest {

    @Test
    public void nnRepDrawTest() {
        NNRepresentation nnRep = new NNRepresentation();
        assertTrue("Default Constructor is not creating random colors",nnRep.isRandomColors());
        nnRep.redraw();
        nnRep.dispose();
    }
}
