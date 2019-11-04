package engine;

import java.util.concurrent.Callable;

public class Coroutine 
{
	private Scene scene;
	
	private Callable<Boolean> condition;
	private Runnable loopBlock;
	private Runnable postloopBlock;
	
	public Coroutine(Scene scene, Callable<Boolean> condition, Runnable loopBlock, Runnable postloopBlock)
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
				postloopBlock.run();
				scene.coroutines.remove(this);
			}
				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
