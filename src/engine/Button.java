package engine;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class Button extends UI implements MouseListener
{	
	private boolean isClickable;
	private float fontScale;
	
	private Runnable mouseClickAction;
	private Runnable mouseEnterAction;
	private Runnable mouseExitAction;
	
	public Button(Item parent, Scene scene)
	{
		super(parent, scene);
		
		createDefaultButton();
		
		//add component to parent if parent is UI
		if (parent instanceof UI)
			((UI) parent).component.add(this.component);
		//otherwise add to scene
		else
			scene.add(this.component);
	}
	
	private void createDefaultButton()
	{
		component = new JButton()
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
		
		isClickable = true;
		fontScale = 1;
		mouseClickAction = mouseEnterAction = mouseExitAction = null;

		component.setFocusable(false);
		component.addMouseListener(this);
	}
	
	public void resizeUpdate()
	{
		scaleFontSize();
	}
	
	private void scaleFontSize()
	{
		int fontSize = 1;
		JButton button = (JButton) component;
		Font font = button.getFont();
		FontMetrics fontMetrics;

		do
		{
			font = new Font(font.getName(), font.getStyle(), fontSize);
			fontMetrics = button.getFontMetrics(font);
			fontSize++;
		} while (fontMetrics.getHeight() < fontScale * button.getHeight());
		
		((JButton) component).setFont(new Font(font.getName(), font.getStyle(), fontSize));
	}
	
	public String getText() { return ((JButton) component).getText(); }
	public void setText(String text) { ((JButton) component).setText(text); }
	
	public Font getFont() { return ((JButton) component).getFont(); }
	public void setFont(String name, int style) { ((JButton) component).setFont(new Font(name, style, ((JButton) component).getFont().getSize())); }
	
	public float getFontScale() { return fontScale; }
	public void setFontScale(float fontScale) { this.fontScale = fontScale; }
	
	public boolean isClickable() { return isClickable; }
	public void setClickable(boolean isClickable) { this.isClickable = isClickable; }
	
	public void setMouseClickAction(Runnable action) { mouseClickAction = action; }
	public void setMouseEnterAction(Runnable action) { mouseEnterAction = action; }
	public void setMouseExitAction(Runnable action) { mouseExitAction = action; }
	
	public void mouseClicked(MouseEvent mouseEvent) 
	{
		if (isClickable && mouseClickAction != null)
		{
			//System.out.println("Button Clicked!");
			mouseClickAction.run();
		}
	}

	public void mouseEntered(MouseEvent mouseEvent) 
	{
		if (isClickable && mouseEnterAction != null)
		{
			//System.out.println("Button Entered!");
			mouseEnterAction.run();
		}
	}

	public void mouseExited(MouseEvent mouseEvent) 
	{
		if (isClickable && mouseExitAction != null)
		{
			//System.out.println("Button Exited!");
			mouseExitAction.run();
		}
	}

	public void mousePressed(MouseEvent mouseEvent) {}
	public void mouseReleased(MouseEvent mouseEvent) {}
}
