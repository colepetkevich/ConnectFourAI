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
        if (getVisibility())
        {
            g.setColor(color);

            int sceneWidth = scene.getWidth();
            int sceneHeight = scene.getHeight();

            float drawWidth = getSize().x * sceneHeight / (2 * Scene.RADIUS);
            float drawHeight = getSize().y * sceneHeight / (2 * Scene.RADIUS);

            Vector2 positionOne = new Vector2(getLocalPosition().x - .5f * getLocalSize().x, getLocalPosition().y - .5f * getLocalSize().y);
            Vector2 positionTwo = new Vector2(getLocalPosition().x + .5f * getLocalSize().x, getLocalPosition().y + .5f * getLocalSize().y);
            //System.out.println(positionOne + " & " + positionTwo);


            g.drawLine(Math.round(positionOne.x * sceneHeight / (2 * Scene.RADIUS) + sceneWidth / 2),
                    Math.round(-positionOne.y * sceneHeight / (2 * Scene.RADIUS) + sceneHeight / 2),
                    Math.round(positionTwo.x * sceneHeight / (2 * Scene.RADIUS) + sceneWidth / 2),
                    Math.round(-positionTwo.y * sceneHeight / (2 * Scene.RADIUS) + sceneHeight / 2));
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
