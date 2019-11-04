package engine;

public class Vector2
{
	public final float x;
	public final float y;
	
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Vector2 other) { return x == other.x && y == other.y; }
	
	public String toString()
	{
		return "X: " + x + ", Y: " + y; 
	}
}
