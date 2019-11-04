package game;

import java.awt.Graphics;

import engine.Drawable;
import engine.Image;
import engine.Scene;

public class ConnectFourBoard extends Drawable
{ 	
	private static final String BOARD_TILE_PATH = "res/BoardTile.png";
	
	public ConnectFourBoard(Scene scene)
	{
		super(scene);
	}
	
	public ConnectFourBoard(Drawable parent, Scene scene)
	{
		super(parent, scene);
	}
	
	public void setBoard(float height, int rows, int columns)
	{
		//calculating the width based on height, rows, and columns
		float width = height * columns / rows;
		
		final int NUM_OF_TILES = rows * columns;
		//setting boardTiles position and size off of this ConnectFourBoard's position and size
		for (int i = 0; i < NUM_OF_TILES; i++)
		{
			int row = i / columns;
			int column = i % columns;
					
			//creating new 
			Image tile = new Image(this, scene);
			tile.setImage(BOARD_TILE_PATH);
			tile.setLocalSize(width / columns, width / columns);
			tile.setLocalPosition(getLocalPosition().x - width / 2 + tile.getLocalSize().x * (column + 0.5f), getLocalPosition().y + getLocalSize().y / 2 - tile.getLocalSize().y * (row - 0.5f));
			//make tiles slightly bigger so that there are no gap between tiles
			tile.setLocalSize(1.01f * tile.getLocalSize().x, 1.01f * tile.getLocalSize().y);
		}
		
		
	}

	public void update() {}
	public void fixedUpdate() {}
	public void resizeUpdate() {}

	public void draw(Graphics g) {}
}
