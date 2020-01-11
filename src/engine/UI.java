package engine;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyl
 */

import javax.swing.JComponent;

public abstract class UI extends Item implements Updatable
{
	protected Scene scene;
	protected JComponent component;
	
	public UI(Item parent, Scene scene)
	{
		super(parent);
		
		this.scene = scene;
		component = null;
		
		scene.updatables.add(this);
	}
	
	public void update()
	{
		if (component != null)
		{
			int sceneWidth = scene.getWidth();
			int sceneHeight = scene.getHeight();
			
			float componentWidth = getSize().x * sceneHeight / (2 * Scene.RADIUS);
			float componentHeight = getSize().y * sceneHeight / (2 * Scene.RADIUS);
			
			component.setBounds(
					Math.round(getPosition().x * sceneHeight / (2 * Scene.RADIUS) + sceneWidth / 2 - componentWidth / 2), 
					Math.round(-getPosition().y * sceneHeight / (2 * Scene.RADIUS) + sceneHeight / 2 - componentHeight / 2), 
					Math.round(componentWidth), 
					Math.round(componentHeight));	
		}
	}
	
	public void fixedUpdate() {}
	public void resizeUpdate() {}
	
	public void destroy()
	{
		if (component != null)
			scene.remove(component);
		
		scene.updatables.remove(this);
		
		//destroy all children as well
		for (int i = 0; i < children.size(); i++)
			children.get(i).destroy();
	}
}
