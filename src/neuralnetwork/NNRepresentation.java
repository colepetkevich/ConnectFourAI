package neuralnetwork;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyl
 */

import connectfour.DataSetHandler;
import engine.*;
import engine.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NNRepresentation extends JFrame implements KeyListener
{
    private Scene scene;
    private NeuralNetwork gameNN;
    private boolean randomColors;

    private static final float MAX_UPDATES_PER_SECOND = 1;
    private static final float FIXED_UPDATES_PER_SECOND = 1;
    private static final int DEFAULT_WIDTH = 540;
    private static final int DEFAULT_HEIGHT = 400;

    public NNRepresentation() {
        super("Neural Network Representation");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);
        randomColors = true;
        redraw();
    }

    public NNRepresentation(NeuralNetwork gameNN, boolean randomColors) {
        super("Neural Network Representation");

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        addKeyListener(this);

        this.randomColors = randomColors;
        this.gameNN = gameNN;

        redraw();
    }

    private int index;
    private void createRep()
    {
        Image empty = new Image(scene);
        empty.setLocalSize(2.75f, 1.75f);

        int[] neuronLayerCounts = Trainer.NEURON_LAYER_COUNTS;
        int totalNeurons = 0;
        int totalLayers = neuronLayerCounts.length;
        final float CIRC_SIZE = .03f;

        for (int i = 0; i < totalLayers; i++)
            totalNeurons += neuronLayerCounts[i];

        Circle[] neurons = new Circle[totalNeurons];
        index = 0;
        for (int i = 0; i < totalLayers; i++) {
            int neuronCount = neuronLayerCounts[i];
            for (int j = 0; j < neuronLayerCounts[i]; j++) {

                final int INDEX = index;
                Circle circ = new Circle(empty, scene)
                {
                    public void update()
                    {
                        if (randomColors) {
                            setColor(Color.WHITE);
                        }
                        else {
                            Neuron[] nnNeurons = gameNN.getNeurons();
                            double output = nnNeurons[INDEX].getOutput();

                            Color neuronColor = Color.DARK_GRAY;

                            //if input neuron
                            if (INDEX < neuronLayerCounts[0])
                            {
                                if (output == DataSetHandler.TRANSLATED_RED)
                                    neuronColor = Color.RED;
                                else if (output == DataSetHandler.TRANSLATED_YELLOW)
                                    neuronColor = Color.YELLOW;
                            }
                            else if (output >= .5)
                                neuronColor = Color.WHITE;

                            setColor(neuronColor);
                        }
                    }
                };
                circ.setLocalSize(CIRC_SIZE, CIRC_SIZE);

                neurons[index] = circ;
                circ.setLocalPosition(-.5f * empty.getLocalSize().x + i * empty.getLocalSize().x / (totalLayers - 1), .5f * empty.getLocalSize().y - .5f * circ.getLocalSize().y - j * (empty.getLocalSize().y - circ.getLocalSize().y) / (neuronCount - 1));
                index++;
            }
        }

        WeightNetwork wN = gameNN.getWeightNetwork();
        Neuron[] nnNeurons = gameNN.getNeurons();
        int src = 0;
        for (int i = 0; i < neuronLayerCounts.length - 1; i++)
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

                    final int SRC = src;
                    final int DEST = dest;
                    Line line = new Line(empty, scene)
                    {
                        public void update()
                        {
                            double weight = nnNeurons[SRC].getOutput() * wN.getWeight(SRC, DEST).getWeight();

                            if (!randomColors){
                                setVisibility(true);
                                if (weight > 1)
                                    setColor(Color.WHITE);
                                else if (weight < -1)
                                    setColor(Color.DARK_GRAY );
                                else
                                    setVisibility(false);
                            }
                        }
                    };

                    if (randomColors)
                    {
                        line.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                        line.setLayer(1 - (float) (10 * Math.random()));
                    }

                    line.setLocalPosition((positionOne.x + positionTwo.x) / 2, (positionOne.y + positionTwo.y) / 2);
                    line.setLocalSize(positionOne.x - positionTwo.x, positionOne.y - positionTwo.y);
                    //line.setColor(new Color((int) (255.0f * src / totalNeurons), 12, 233));
                }
            }
        }

        scene.initialize();
        scene.update();
        setVisible(true);
    }

    public void redraw()
    {
        //removing previous scene
        if (scene != null)
        {
            scene.setEnabled(false);
            scene.setActive(false);
            remove(scene);
        }

        //creating new updated scene
        scene = new Scene(MAX_UPDATES_PER_SECOND, FIXED_UPDATES_PER_SECOND);
        add(scene);
        scene.setLayout(null);

        if (gameNN != null)
        {
            createRep();
        }
    }

    public void dispose()
    {
        super.dispose();
        scene.setActive(false);
    }

    public void changeColors(NeuralNetwork gameNN)
    {
        this.gameNN = gameNN;
        randomColors = !randomColors;
        redraw();
    }

    public boolean isRandomColors() {
        return randomColors;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyChar() == 'r')
        {
            changeColors(gameNN);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
