package engine;

import javax.swing.*;
import java.awt.*;

public class Label extends UI
{
    private float localFontScale;

    public Label(Item parent, Scene scene)
    {
        super(parent, scene);

        createDefaultLabel();

        //add component to parent if parent is UI
        if (parent instanceof UI)
            ((UI) parent).component.add(this.component);
            //otherwise add to scene
        else
            scene.add(this.component);
    }

    private void createDefaultLabel()
    {
        component = new JLabel()
        {
            public void paint(Graphics g)
            {
                if (getVisibility())
                {
                    setOpaque(true);
                    super.paint(g);
                }
                else
                    setOpaque(false);
            }
        };

        //making layout of component null
        component.setLayout(null);

        //center text
        ((JLabel) component).setVerticalAlignment(SwingConstants.CENTER);
        ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
    }

    //overriding parent method so that it scales font too
    public void setLocalScale(Vector2 localScale)
    {
        super.setLocalScale(localScale);
        scaleFontSize();
    }

    private Vector2 previousScale = Vector2.ZERO;
    public void update()
    {
        super.update();

        //if there is a change in scale, update font scale as well
        if (!getScale().equals(previousScale))
            scaleFontSize();

        previousScale = getScale();
    }

    public void resizeUpdate()
    {
        scaleFontSize();
    }

    private void scaleFontSize()
    {
        int fontSize = 1;
        JLabel button = (JLabel) component;
        Font font = button.getFont();
        FontMetrics fontMetrics;

        do
        {
            font = new Font(font.getName(), font.getStyle(), fontSize);
            fontMetrics = button.getFontMetrics(font);
            fontSize++;
        } while (fontMetrics.getHeight() < localFontScale * getLocalScale().y * button.getHeight());

        ((JLabel) component).setFont(new Font(font.getName(), font.getStyle(), fontSize));
    }

    public Color getColor() { return ((JLabel) component).getBackground(); }
    public void setColor(Color color) { ((JLabel) component).setBackground(color);}

    public String getText() { return ((JLabel) component).getText(); }
    public void setText(String text) { ((JLabel) component).setText(text); }

    public Font getFont() { return ((JLabel) component).getFont(); }
    public void setFont(String name, int style) { ((JLabel) component).setFont(new Font(name, style, ((JLabel) component).getFont().getSize())); }

    public Color getTextColor() { return ((JLabel) component).getForeground(); }
    public void setTextColor(Color color) { ((JLabel) component).setForeground(color); }

    public float getLocalFontlScale() { return localFontScale; }
    public void setLocalFontScale(float localFontScale) { this.localFontScale = localFontScale; }
}
