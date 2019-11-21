package neuralnetwork;

import engine.Circle;
import engine.Image;
import engine.Line;
import engine.Scene;
import engine.Vector2;
import neuralnetwork.Trainer;

import javax.swing.*;
import java.awt.*;

public class NNRepresentation extends JFrame {

    private Scene scene;

    public NNRepresentation() {
        super("Neural Network Representation");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        scene = new Scene();
        add(scene);

        createRep();
        scene.initialize(this);

        setVisible(true);
    }

    private void createRep()
    {
        Image empty = new Image(scene);

        int[] neuronLayerCounts = Trainer.NEURON_LAYER_COUNTS;
        int totalNeurons = 0;
        int totalLayers = neuronLayerCounts.length;
        final float CIRC_SIZE = .015f;


        for (int i = 0; i < totalLayers; i++) {
            totalNeurons += neuronLayerCounts[i];
        }
        Circle[] neurons = new Circle[totalNeurons];
        int index = 0;
        for (int i = 0; i < totalLayers; i++) {
            int neuronCount = neuronLayerCounts[i];
            for (int j = 0; j < neuronLayerCounts[i]; j++) {

                Circle circ = new Circle(empty, scene);
                circ.setLocalSize(CIRC_SIZE, CIRC_SIZE);
                neurons[index] = circ;
                //System.out.println((2 - circ.getLocalSize().y) * j / neuronCount);
                //circ.setLocalPosition(-1 + (float) i / totalLayers, (1 - .5f * circ.getLocalSize().y) - (1 - circ.getLocalSize().y) * (float) j / neuronCount);
                circ.setLocalPosition(-.5f * empty.getLocalSize().x + i * empty.getLocalSize().x / (totalLayers - 1), .5f * empty.getLocalSize().y - .5f * circ.getLocalSize().y - j * (empty.getLocalSize().y - circ.getLocalSize().y) / (neuronCount - 1));
                index++;
            }
        }

        // first layer of neurons: 0 to neuronLayerCounts[0] - 1
        // second layer of neurons: neuronLayerCounts[0] - 1 to neuronLayerCounts[1] - 1
        // third layer of neerons: neuronLayerCounts[1] - 1 to neuronLayerCounts[2] - 1

//        // this for loop goes through all the layers except the last one
//        int startIndex = 0;
//        int endIndex = 0;
//        for (int i = 0; i < neuronLayerCounts.length - 1; i++) {
//            endIndex += neuronLayerCounts[i];
//            if (i != 0) {
//                startIndex += neuronLayerCounts[i-1];
//            }
//            // this loops through all the circles on the current layer
//            // we use this to draw a line from this layer to the next layer
//            for (int j = startIndex; j < endIndex; j++) {
//                System.out.println(neuronLayerCounts[i] + " " + neuronLayerCounts[i + 1]);
//                for (int k = neuronLayerCounts[i]; k < neuronLayerCounts[i + 1]; k++) {
//                    // draw a line from circle neurons[j] to circle neurons[k]
//                    Vector2 positionOne = neurons[j].getLocalPosition();
//                    Vector2 positionTwo = neurons[k].getLocalPosition();
//
//                    Line line = new Line(empty, scene);
//                    line.setLocalPosition((positionOne.x + positionTwo.x) / 2, (positionOne.y + positionTwo.y) / 2);
//                    line.setLocalSize(positionOne.x - positionTwo.x, positionOne.y - positionTwo.y);
//                    line.setColor(Color.RED);
//                    System.out.println(line.getLocalPosition());
//                }
//            }
//        }

        int src = 0;
        for (int i = 0; i < /*neuronLayerCounts.length - 1*/1; i++)
        {
            int initialDest = src + neuronLayerCounts[i];
            for (; src < initialDest; src++)
            {
                int finalDest = initialDest + neuronLayerCounts[i + 1] - 1;
                for (int dest = initialDest; dest <= finalDest; dest++)
                {
                    // draw a line from circle neurons[j] to circle neurons[k]
                    Vector2 positionOne = neurons[src].getLocalPosition();
                    Vector2 positionTwo = neurons[dest].getLocalPosition();


                    Line line = new Line(empty, scene);
                    line.setLocalPosition((positionOne.x + positionTwo.x) / 2, (positionOne.y + positionTwo.y) / 2);
                    line.setLocalSize(positionOne.x - positionTwo.x, positionOne.y - positionTwo.y);
                    line.setColor(Color.RED);

                    System.out.println(positionOne + " & " + positionTwo +  " --> " + line.getLocalPosition());
                }
            }
        }

        empty.setLocalScale(1.75f, 1.75f);
    }


//    public void paint(Graphics g) {
//        int scaling = 20;
//        int circleSize = 10;
//        int x = 100;
//        int y;
//        int[] neuralNetwork = Trainer.getNeuronLayerCounts();
//        for (int i = 0; i < neuralNetwork.length; i++ ) {
//            y = 10;
//            for (int j = 0;j < neuralNetwork[i]; j++) {
//                g.fillOval(x,y,circleSize,circleSize);
//                y+=scaling;
//            }
//            x+=scaling;
//        }
//
//
//
//    }


}
