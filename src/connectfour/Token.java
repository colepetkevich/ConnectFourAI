package connectfour;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyl
 */

import engine.Coroutine;
import engine.Drawable;
import engine.Image;
import engine.Scene;
import engine.Time;

public class Token extends Image
{
	private static final String RED_TOKEN_PATH = "res/images/RedToken.png";
	private static final String YELLOW_TOKEN_PATH = "res/images/YellowToken.png";
			
	public Token(Scene scene)
	{
		super(scene);
		
		//default token image
		setRed();
	}
	
	public Token(Drawable parent, Scene scene)
	{
		super(parent, scene);
		
		//default token image
		setRed();
	}
	
	public void setRed() { setImage(RED_TOKEN_PATH); }
	public void setYellow() { setImage(YELLOW_TOKEN_PATH); }
	
	//drop coroutine variables
	private float accelerationY = -9.8f;
	private float velocityY = 0.0f;
	private float bounceFactorY = -0.45f;
	public void drop(float finalY)
	{	
		//creating the drop coroutine
		Coroutine drop = new Coroutine(
				//loop condition: y position is greater or equal to finalY position
				() -> getLocalPosition().y >= finalY,
				//loop block: change velocity and y position depending on the change in time
				() -> {
					velocityY += accelerationY * scene.TIME.deltaTime();
					setLocalPosition(getLocalPosition().x, getLocalPosition().y + velocityY * scene.TIME.deltaTime());
					
					//bounce token if its y position is less than finalY position and its velocityY is great enough
					if (getLocalPosition().y < finalY && Math.abs(velocityY) > .1f)
					{
						setLocalPosition(getLocalPosition().x, finalY);
						velocityY *= bounceFactorY;
					}
				},
				//post loop block: set token to finalY position
				() -> {
					setLocalPosition(getLocalPosition().x, finalY);
				}, scene);
		
		//starting the drop coroutine
		drop.start();
	}
}
