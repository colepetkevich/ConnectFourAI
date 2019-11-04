package engine;

import java.awt.Graphics;

public abstract class Drawable extends Item implements Updatable, Comparable<Drawable>
{	
	private static final float DEFAULT_LAYER = 0;
	
	protected Scene scene;
	private float layer;
	
	public Drawable(Scene scene)
	{		
		super(scene.CENTER);
		
		this.scene = scene;
		layer = DEFAULT_LAYER;
		
		scene.updatables.add(this);
		scene.drawables.add(this);
	}
	
	public Drawable(Drawable parent, Scene scene)
	{
		super(parent);
		
		this.scene = scene;
		//child Drawables have their default layer one ahead of parent so that they are visible
		layer = parent.layer + 1;
		
		scene.updatables.add(this);
		scene.drawables.add(this);
	}
	
	public void destroy()
	{
		scene.updatables.remove(this);
		scene.drawables.remove(this);
		
		//destroy all children as well
		for (int i = 0; i < children.size(); i++)
			children.get(i).destroy();
	}
	
	public abstract void draw(Graphics g);
	
	public float getLayer() { return layer; }
	public void setLayer(float layer) { this.layer = layer; }
	
	//uses layer to compare Drawables
	public int compareTo(Drawable other)
	{
		if (layer == other.layer)
			return 0;
		else if (layer > other.layer)
			return -1;

		return 1;
	}
}
