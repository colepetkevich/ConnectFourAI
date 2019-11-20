package engine;

import java.awt.*;

public class Line extends Drawable {

    private Color color;

    public Line(Scene scene) {
        super(scene);
    }

    public Line(Drawable parent, Scene scene) {
        super(parent, scene);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);

        if (getVisibility())
        {
            int sceneWidth = scene.getWidth();
            int sceneHeight = scene.getHeight();

            float drawWidth = getSize().x * sceneHeight / (2 * Scene.RADIUS);
            float drawHeight = getSize().y * sceneHeight / (2 * Scene.RADIUS);
            //System.out.println(getPosition().x);

            // x =

            g.drawLine(Math.round(getPosition().x * sceneHeight / (2 * Scene.RADIUS) + sceneWidth / 2 - drawWidth / 2),
                    Math.round(-getPosition().y * sceneHeight / (2 * Scene.RADIUS) + sceneHeight / 2 - drawHeight / 2),
                    Math.round(drawWidth),
                    Math.round(drawHeight));
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void fixedUpdate() {

    }

    @Override
    public void resizeUpdate() {

    }
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
}
