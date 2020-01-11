package engine;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyl
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageFactory
{
	public static BufferedImage getImageFromPath(String imagePath) 
	{
		BufferedImage image = null;
		try 
		{
			image = ImageIO.read(new File(imagePath));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return image;
	}
}
