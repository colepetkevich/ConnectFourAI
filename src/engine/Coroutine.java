package engine;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyl
 */

import java.util.concurrent.Callable;

public class Coroutine 
{
	private Scene scene;
	
	private Callable<Boolean> condition;
	private Runnable loopBlock;
	private Runnable postloopBlock;
	
	public Coroutine(Callable<Boolean> condition, Runnable loopBlock, Runnable postloopBlock, Scene scene)
	{
		this.scene = scene;
		this.condition = condition;
		this.loopBlock = loopBlock;
		this.postloopBlock = postloopBlock;	
	}
	
	public void start()
	{
		scene.coroutines.add(this);
	}
	
	//runs loops block of coroutine
	public void run()
	{	
		try 
		{
			//while the condition is true, run loopBlock
			if (condition.call())
				loopBlock.run();
			//if the condition is no longer true, run post-loop block then remove this coroutine
			else
			{
				scene.coroutines.remove(this);
				postloopBlock.run();
			}
				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
