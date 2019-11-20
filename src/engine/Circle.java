package engine;

import java.awt.*;

public class Circle extends Drawable
{
    private Color color;

    public Circle(Scene scene)
    {
        super(scene);
        color = Color.WHITE;
    }

    public Circle(Drawable parent, Scene scene)
    {
        super(parent, scene);
        color = Color.WHITE;
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

            g.fillOval(Math.round(getPosition().x * sceneHeight / (2 * Scene.RADIUS) + sceneWidth / 2 - drawWidth / 2),
                    Math.round(-getPosition().y * sceneHeight / (2 * Scene.RADIUS) + sceneHeight / 2 - drawHeight / 2),
                    Math.round(drawWidth),
                    Math.round(drawHeight));
        }
    }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    @Override
    public void update() {

    }

    @Override
    public void fixedUpdate() {

    }

    @Override
    public void resizeUpdate() {

    }
}
