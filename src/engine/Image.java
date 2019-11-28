package engine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Image extends Drawable
{
	private BufferedImage image;
	
	public Image(Scene scene) 
	{
		super(scene);
		
		image = null;
	}
	
	public Image(Drawable parent, Scene scene)
	{
		super(parent, scene);
		
		image = null;
	}
	
	public BufferedImage getImage() { return image; }
	public void setImage(String imagePath) { this.image = ImageFactory.getImageFromPath(imagePath); }
	public void setImage(BufferedImage image) { this.image = image; }

	public void update() {}
	public void fixedUpdate() {}
	public void resizeUpdate() {}

	public void draw(Graphics g) 
	{
		if (image != null)
		{
			if (getVisibility())
			{
				int sceneWidth = scene.getWidth();
				int sceneHeight = scene.getHeight();
				
				float drawWidth = getSize().x * sceneHeight / (2 * Scene.RADIUS);
				float drawHeight = getSize().y * sceneHeight / (2 * Scene.RADIUS);


				g.drawImage(image,
						Math.round(getPosition().x * sceneHeight / (2 * Scene.RADIUS) + sceneWidth / 2 - drawWidth / 2), 
						Math.round(-getPosition().y * sceneHeight / (2 * Scene.RADIUS) + sceneHeight / 2 - drawHeight / 2), 
						Math.round(drawWidth), 
						Math.round(drawHeight), 
						null);
			}
		}
	}
}
