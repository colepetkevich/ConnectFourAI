package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Scene extends JPanel implements Runnable, Updatable
{	
	//updates values
	public static final float MAX_UPDATES_PER_SECOND = 1000;
	public static final float FIXED_UPDATES_PER_SECOND = 20;
	
	//dimensions
	public static final float RADIUS = 1.0f;

	//Updatable, Drawable, and Coroutine ArrayLists
	protected ArrayList<Updatable> updatables;
	protected ArrayList<Drawable> drawables;
	protected ArrayList<Coroutine> coroutines;
	
	//game state variables
	private boolean isInitilized;
	private boolean isActive;
	
	//graphics variables
	private Timer secondTimer;
	private ActionListener printFPS;
	private int fps;
	private Color backgroundColor;
	
	//ui variables
	public final Anchor CENTER;
	public final Anchor NORTH;
	public final Anchor SOUTH;
	public final Anchor EAST;
	public final Anchor WEST;
	public final Anchor NORTH_EAST;
	public final Anchor NORTH_WEST;
	public final Anchor SOUTH_EAST;
	public final Anchor SOUTH_WEST;
		
	public Scene()
	{	
		super();
		setLayout(null);
		setOpaque(false);
		addResizeListener();
		
		//instantiating all ArrayList
		updatables = new ArrayList<Updatable>();
		drawables = new ArrayList<Drawable>();
		coroutines = new ArrayList<Coroutine>();
		
		//default initialization status and active status
		isInitilized = false;
		isActive = false;
		
		//creating second timer for fps counter
		printFPS = new ActionListener()
		{   
            public void actionPerformed(ActionEvent event)
            {
            	if (isActive)
            	{
            		System.out.println("fps: " + fps);
            		fps = 0;
            	}
            	else
            		secondTimer.stop();
            }
        };
		secondTimer = new Timer(1000, printFPS);
		secondTimer.start();
		fps = 0;
		
		//default backgroundColor
		backgroundColor = Color.BLACK;
				
		//initilizing UI anchors
		CENTER = new Anchor(Anchor.CENTER, this);
		NORTH = new Anchor(Anchor.NORTH, this);
		SOUTH = new Anchor(Anchor.SOUTH, this);
		EAST = new Anchor(Anchor.EAST, this);
		WEST = new Anchor(Anchor.WEST, this);
		NORTH_EAST = new Anchor(Anchor.NORTH_EAST, this);
		NORTH_WEST = new Anchor(Anchor.NORTH_WEST, this);
		SOUTH_EAST = new Anchor(Anchor.NORTH_EAST, this);		
		SOUTH_WEST = new Anchor(Anchor.NORTH_WEST, this);
	}

	public void initialize(JFrame jFrame)
	{
		if (!isInitilized)
		{		
			isActive = true;
			isInitilized = true;
			
			//starts running game and make the JFrame visible
			new Thread(this).start();
			jFrame.setVisible(true);
			
			resizeUpdate();
			fixedUpdate();
			update();
		}
	}
	
	//handles all the game logic and graphics
	public void update()
	{				
		//calls update() on all updatables
		for (int i = updatables.size() - 1; i >= 0; i--)
			updatables.get(i).update();
		
		//runs all coroutines once
		for (int i = coroutines.size() - 1; i >= 0; i--)
			coroutines.get(i).run();
		
		//repaint Scene
		repaint();
		
		fps++;
		Time.saveLastUpdate();
	}
	
	//handles all the I/O
	public void fixedUpdate()
	{		
		//calls fixedUpdate() on all updatables
		for (int i = updatables.size() - 1; i >= 0; i--)
			updatables.get(i).fixedUpdate();
				
		Time.saveLastFixedUpdate();
	}
	
	public void paint(Graphics g)
	{	
		//painting background
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//sorts drawables by layer
		Collections.sort(drawables);
		for (int i = drawables.size() - 1; i >= 0; i--)
			drawables.get(i).draw(g);
		
		//paint all the ui last
		super.paint(g);
	}
	
	public void resizeUpdate() 
	{
		//calls resizeUpdate() on all updatables
		for (int i = updatables.size() - 1; i >= 0; i--)
			updatables.get(i).resizeUpdate();
	}
	
	private void addResizeListener() 
	{
		addComponentListener(new ComponentAdapter( ) 
		{
			  public void componentResized(ComponentEvent event) 
			  {
				  //call resizeUpdate() if Scene has been resized
				  resizeUpdate();
				  //System.out.println("Resized");
			  }
		});
	}
	
	private void secondTimer()
	{
		
	}
	
	public void run() 
	{
		while (isActive)
		{				
			//call fixedUpdate() if enough time has elapsed
			if (Time.fixedDeltaTime() >= 1 / FIXED_UPDATES_PER_SECOND)
				fixedUpdate();
			//call update() if enough time has elapsed
			if (Time.deltaTime() >= 1 / MAX_UPDATES_PER_SECOND)
				update();	
		}
	}
	
	public Color getBackgroundColor() { return backgroundColor; }
	public void setBackgroundColor(Color color) { backgroundColor = color; }
	
	public boolean getActive() { return isActive; }
	public void setActive(boolean isActive) { this.isActive = isActive; }
}
