package engine;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyl
 */

public class Time 
{
	//nanoseconds in a second
	public static final float SECOND = 1000000000;
	
	private long lastUpdate;
	private long lastFixedUpdate;

	public Time()
	{
		lastUpdate = System.nanoTime();
		lastFixedUpdate = System.nanoTime();
	}
	
	public void saveLastUpdate() { lastUpdate = System.nanoTime(); }
	public void saveLastFixedUpdate() { lastFixedUpdate = System.nanoTime(); }
	
	public float deltaTime() { return (float) (System.nanoTime() - lastUpdate) / SECOND; }
	public float fixedDeltaTime() { return (float) (System.nanoTime() - lastFixedUpdate) / SECOND; }
}
