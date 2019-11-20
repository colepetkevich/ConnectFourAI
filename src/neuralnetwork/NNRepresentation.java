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
        for (int i = 0; i < totalLayers; i++) {
            int neuronCount = neuronLayerCounts[i];
            for (int j = 0; j < neuronLayerCounts[i]; j++) {

                Circle circ = new Circle(empty, scene);
                circ.setLocalSize(CIRC_SIZE, CIRC_SIZE);
                System.out.println((2 - circ.getLocalSize().y) * j / neuronCount);
                //circ.setLocalPosition(-1 + (float) i / totalLayers, (1 - .5f * circ.getLocalSize().y) - (1 - circ.getLocalSize().y) * (float) j / neuronCount);
                circ.setLocalPosition(-.5f * empty.getLocalSize().x + i * empty.getLocalSize().x / (totalLayers - 1), .5f * empty.getLocalSize().y - .5f * circ.getLocalSize().y - j * (empty.getLocalSize().y - circ.getLocalSize().y) / (neuronCount - 1));
            }
        }

        empty.setLocalScale(1.75f, 1.75f);

        Line line = new Line(scene);
        line.setColor(Color.red);
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
