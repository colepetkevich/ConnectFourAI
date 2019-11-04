package engine;

public final class Anchor extends Item implements Updatable
{
	public static final int CENTER = 0;
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	public static final int WEST = 4;
	public static final int NORTH_EAST = 5;
	public static final int NORTH_WEST = 6;
	public static final int SOUTH_EAST = 7;
	public static final int SOUTH_WEST = 8;

	
	private Scene scene;
	private int anchor;
	
	public Anchor(int anchor, Scene scene)
	{
		super();
		
		this.scene = scene;
		this.anchor = anchor;
		
		scene.updatables.add(this);
	}
			
	public void update() {}
	public void fixedUpdate() {}
	
	public void resizeUpdate() 
	{
		switch (anchor)
		{
			case CENTER: 
				setLocalPosition(0, 0);
				break;
			case NORTH:
				setLocalPosition(0, Scene.RADIUS);
				break;
			case SOUTH:
				setLocalPosition(0, -Scene.RADIUS);
				break;
			case EAST:
				setLocalPosition(Scene.RADIUS * scene.getWidth() / scene.getHeight(), 0);
				break;
			case WEST:
				setLocalPosition(-Scene.RADIUS * scene.getWidth() / scene.getHeight(), 0);
				break;
			case NORTH_EAST:
				setLocalPosition(Scene.RADIUS * scene.getWidth() / scene.getHeight(), Scene.RADIUS);
				break;
			case NORTH_WEST:
				setLocalPosition(-Scene.RADIUS * scene.getWidth() / scene.getHeight(), Scene.RADIUS);
				break;
			case SOUTH_EAST:
				setLocalPosition(Scene.RADIUS * scene.getWidth() / scene.getHeight(), -Scene.RADIUS);
				break;
			case SOUTH_WEST:
				setLocalPosition(-Scene.RADIUS * scene.getWidth() / scene.getHeight(), -Scene.RADIUS);
				break;
			default:
				//default anchor is CENTER
				anchor = CENTER;
				setLocalPosition(0, 0);
				break;
		}
	}
	
	//overriding setLocalSize() and setLocalScale() so that Anchor cannot have size or scale altered
	public void setLocalSize(float x, float y) { setLocalSize(new Vector2(0, 0)); } 
	public void setLocalSize(Vector2 localSize) { System.out.println("ERROR: Unable to change Anchor's local size"); }
	
	public void setLocalScale(float x, float y) { setLocalScale(new Vector2(0, 0)); } 
	public void setLocalScale(Vector2 localScale) { System.out.println("ERROR: Unable to change Anchor's local scale"); }
	
	//overriding destroy() so that Anchor cannot be destroyed
	public final void destroy() { System.out.println("ERROR: Unable to destroy Anchor"); }
}
