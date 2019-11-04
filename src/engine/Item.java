package engine;

import java.util.ArrayList;

public abstract class Item
{	
	private static final Vector2 DEFAULT_LOCAL_POSITION = new Vector2(0, 0);
	private static final Vector2 DEFAULT_LOCAL_SIZE = new Vector2(1, 1);
	private static final Vector2 DEFAULT_LOCAL_SCALE = new Vector2(1, 1);
	private static final boolean DEFAULT_VISIBILITY = true;
	
	protected Item parent;
	protected ArrayList<Item> children;
	
	private Vector2 localPosition;
	private Vector2 localSize;
	private Vector2 localScale;
	private boolean visibility;
	
	private Vector2 position;
	private Vector2 size;
	private Vector2 scale;
	
	//default constructor for Anchor's only
	public Item()
	{
		this.parent = null;
		children = new ArrayList<Item>();
		
		//default local position, local size, local scale, and visibility
		localPosition = DEFAULT_LOCAL_POSITION;
		localSize = DEFAULT_LOCAL_SIZE;
		localScale = DEFAULT_LOCAL_SCALE;
		visibility = DEFAULT_VISIBILITY;
		
		//setting position, size, and scale without accounting for a parent
		scale = localScale;
		size = new Vector2(scale.x * localSize.x, scale.y * localSize.y);
		position = new Vector2(scale.x * localPosition.x, scale.y * localPosition.y);
	}
	
	public Item(Item parent)
	{
		this.parent = parent;
		parent.children.add(this);
		children = new ArrayList<Item>();
		
		//default local position, local size, and local scale
		localPosition = DEFAULT_LOCAL_POSITION;
		localSize = DEFAULT_LOCAL_SIZE;
		localScale = DEFAULT_LOCAL_SCALE;
		
		//visibility set to parent
		visibility = parent.visibility;
		
		//setting position, size, and scale
		scale = new Vector2(parent.scale.x * localScale.x, parent.scale.y * localScale.y);
		size = new Vector2(scale.x * localSize.x, scale.y * localSize.y);
		position = new Vector2(parent.position.x + scale.x * localPosition.x, parent.position.y + scale.y * localPosition.y);
	}
	
	public Vector2 getLocalPosition() { return localPosition; }
	public void setLocalPosition(float x, float y) { setLocalPosition(new Vector2(x, y)); } 
	public void setLocalPosition(Vector2 localPosition) 
	{ 
		this.localPosition = localPosition;
		
		if (parent != null)
			setPosition(new Vector2(parent.position.x + scale.x * localPosition.x, parent.position.y + scale.y * localPosition.y));
		else
			setPosition(new Vector2(scale.x * localPosition.x, scale.y * localPosition.y));
	}
	
	public Vector2 getLocalSize() { return localSize; }
	public void setLocalSize(float x, float y) { setLocalSize(new Vector2(x, y)); } 
	public void setLocalSize(Vector2 localSize) 
	{ 
		this.localSize = localSize;
		
		setSize(new Vector2(scale.x * localSize.x, scale.y * localSize.y));
	}
	
	public Vector2 getLocalScale() { return localScale; }
	public void setLocalScale(float x, float y) { setLocalScale(new Vector2(x, y)); } 
	public void setLocalScale(Vector2 localScale) 
	{ 
		this.localScale = localScale;
		
		if (parent != null)
			setScale(new Vector2(parent.scale.x * localScale.x, parent.scale.y * localScale.y));
		else
			setScale(localScale);
	}
	
	public boolean getVisibility() { return visibility; }
	public void setVisibility(boolean visibility) 
	{ 
		this.visibility = visibility; 
	
		//set the visibility of the children as well
		for (Item child : children)
			child.setVisibility(visibility);
	}
	
	public Vector2 getPosition() { return position; }
	private void setPosition(Vector2 position)
	{
		this.position = position;
		
		//update only the 
		for (Item child : children)
			child.setPosition(new Vector2(position.x + child.scale.x * child.localPosition.x, position.y + child.scale.y * child.localPosition.y));
	}
	
	public Vector2 getSize() { return size; }
	private void setSize(Vector2 size)
	{
		this.size = size;
		
		//local size does not affect size of children
	}
	
	public Vector2 getScale() { return scale; }
	private void setScale(Vector2 scale)
	{
		this.scale = scale;
		
		//update the scale, size
		for (Item child : children)
		{
			child.setScale(new Vector2(scale.x * child.localScale.x, scale.y * child.localScale.y));
			child.setLocalSize(child.localSize);
			child.setLocalPosition(child.localPosition);
		}
	}
	
	//abstract method for destroying Item
	public abstract void destroy();
	
//	public Item()
//	{
//		parent = null;
//		children = new ArrayList<Item>();
//		
//		//default position, size, and visibility
//		position = DEFAULT_POSITION;
//		size = DEFAULT_SIZE;
//		isVisible = DEFAULT_VISIBILITY;
//	}
//		
//	public Item(Item parent)
//	{
//		this.parent = parent;
//		children = new ArrayList<Item>();
//		
//		//default position and size, but parent's visibility
//		position = DEFAULT_POSITION;
//		size = DEFAULT_SIZE;
//		isVisible = parent.isVisible;
//		
//		parent.children.add(this);
//	}
//		
//	public final Vector2 getPosition() { return position; }
//	public void setPosition(float x, float y) { setPosition(new Vector2(x, y)); }
//	public void setPosition(Vector2 position) { this.position = position; }
////	{ 
////		Vector2 deltaPosition = new Vector2(position.x - this.position.x, position.y - this.position.y);
////		
////		//setting new position
////		this.position = position;
////		
////		//updating the position of all children
////		for (int i = 0; i < children.size(); i++)
////		{
////			Item item = children.get(i);
////			//recursively updates the position of the children's children
////			item.setPosition(new Vector2(item.position.x + deltaPosition.x, item.position.y + deltaPosition.y));
////		}
////	}
////	//updates position without updating children positions
////	private void setPos(Vector2 position) { this.position = position; }
//	
//	public Vector2 getSize() { return size; }
//	public void setSize(float x, float y) { setSize(new Vector2(x, y)); }
//	public void setSize(Vector2 size) { this.size = size; }
////	{ 
////		Vector2 deltaSize = new Vector2(size.x / this.size.x, size.y / this.size.y);
////		
////		//setting new size
////		this.size = size;
////		
////		//updating the size and position of all children
////		for (int i = 0; i < children.size(); i++)
////		{
////			Item child = children.get(i);
////			//recursively updates the size of the children's children
////			child.setSize(new Vector2(child.size.x * deltaSize.x, child.size.y * deltaSize.y));
////			
////			//updates the position of child non-recursively
////			Vector2 childParentDisplacement = new Vector2(child.position.x - position.x, child.position.y - position.y);
////			child.setPos(new Vector2(position.x + deltaSize.x * childParentDisplacement.x, position.y + deltaSize.y * childParentDisplacement.y));
////		}
////	}
//
//	
}
