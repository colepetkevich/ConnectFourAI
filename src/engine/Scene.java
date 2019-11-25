package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Scene extends JPanel implements Runnable, Updatable
{	
	//updates values
	public final float MAX_UPDATES_PER_SECOND;
	public final float FIXED_UPDATES_PER_SECOND;
	
	//dimensions
	public static final float RADIUS = 1.0f;

	//scene time
	public final Time TIME;
	private Timer secondTimer;
	private ActionListener printFPS;
	private int fps;

	//Updatable, Drawable, and Coroutine Vectors
	protected Vector<Updatable> updatables;
	protected Vector<Drawable> drawables;
	protected Vector<Coroutine> coroutines;
	
	//scene state variables
	private boolean isInitilized;
	private boolean isActive;
	
	//graphics variables
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
		
	public Scene(int maxUpdatesPerSecond, int fixedUpdatesPerSecond)
	{
		super();
		setLayout(null);
		setOpaque(false);
		addResizeListener();

		MAX_UPDATES_PER_SECOND = maxUpdatesPerSecond;
		FIXED_UPDATES_PER_SECOND = fixedUpdatesPerSecond;
		
		//instantiating all ArrayList
		updatables = new Vector<Updatable>();
		drawables = new Vector<Drawable>();
		coroutines = new Vector<Coroutine>();
		
		//default initialization status and active status
		isInitilized = false;
		isActive = false;

		TIME = new Time();
//		//creating second timer for fps counter
//		printFPS = new ActionListener()
//		{
//            public void actionPerformed(ActionEvent event)
//            {
//            	if (isActive)
//            	{
//            		System.out.println("fps: " + fps);
//            		fps = 0;
//            	}
//            	else
//            		secondTimer.stop();
//            }
//        };
//		secondTimer = new Timer(1000, printFPS);
//		secondTimer.start();
//		fps = 0;
		
		//default backgroundColor
		backgroundColor = Color.BLACK;
				
		//initializing UI anchors
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
			
			//starts running Scene and make the JFrame visible
			new Thread(this).start();
			jFrame.setVisible(true);
			
			resizeUpdate();
			fixedUpdate();
			update();
		}
	}
	
	//handles all the connectfour logic and graphics
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
		TIME.saveLastUpdate();
	}
	
	//handles all the I/O
	public void fixedUpdate()
	{		
		//calls fixedUpdate() on all updatables
		for (int i = updatables.size() - 1; i >= 0; i--)
			updatables.get(i).fixedUpdate();

		TIME.saveLastFixedUpdate();
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
	
	public void run() 
	{
		while (isActive)
		{				
			//call fixedUpdate() if enough time has elapsed
			if (TIME.fixedDeltaTime() >= 1 / FIXED_UPDATES_PER_SECOND)
				fixedUpdate();
			//call update() if enough time has elapsed
			if (TIME.deltaTime() >= 1 / MAX_UPDATES_PER_SECOND)
				update();	
		}
	}
	
	public Color getBackgroundColor() { return backgroundColor; }
	public void setBackgroundColor(Color color) { backgroundColor = color; }
	
	public boolean getActive() { return isActive; }
	public void setActive(boolean isActive) { this.isActive = isActive; }
}
