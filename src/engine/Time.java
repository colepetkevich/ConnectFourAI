package engine;

public class Time 
{
	//nanoseconds in a second
	public static final float SECOND = 1000000000;
	
	private static long lastUpdate = System.nanoTime();
	private static long lastFixedUpdate = System.nanoTime();
	
	public void saveLastUpdate() { lastUpdate = System.nanoTime(); }
	public void saveLastFixedUpdate() { lastFixedUpdate = System.nanoTime(); }
	
	public float deltaTime() { return (float) (System.nanoTime() - lastUpdate) / SECOND; }
	public float fixedDeltaTime() { return (float) (System.nanoTime() - lastFixedUpdate) / SECOND; }
}
