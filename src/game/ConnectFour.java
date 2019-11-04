package game;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JFrame;

import engine.Button;
import engine.Scene;
import engine.Vector2;

public class ConnectFour extends JFrame
{	
	//class variables
	private Scene currentScene;
	
	public ConnectFour(String title)
	{
		super();
		setTitle(title);
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		newGameScene();	
	}
	
	public static final int ROWS = 6;
	public static final int COLUMNS = 7;
	public static final char RED = 'R';
	public static final char YELLOW = 'Y';
	public static final char EMPTY = ' ';
	
	private static final Vector2 CONNECT_FOUR_POSITION = new Vector2(0, 0);
	private static final float CONNECT_FOUR_HEIGHT = 1.5f;
	private static final Vector2 CONNECT_FOUR_SIZE = new Vector2(CONNECT_FOUR_HEIGHT * COLUMNS / ROWS, CONNECT_FOUR_HEIGHT);
	
	private char[] connectFour;
	private char turn;
	private Button[] inputButtons;
	
	private void newGameScene()
	{
		//creating a new scene
		setScene(new Scene());
		currentScene.setLayout(null);
		currentScene.setBackgroundColor(Color.DARK_GRAY);		
		
		//creating board
		connectFour = new char[ROWS * COLUMNS];
		Arrays.fill(connectFour, EMPTY);
		turn = RED;	
		//creating board graphics
		ConnectFourBoard connectFourBoard = new ConnectFourBoard(currentScene);
		connectFourBoard.setBoard(CONNECT_FOUR_HEIGHT, ROWS, COLUMNS);
		
		//creating input buttons
		inputButtons = new Button[COLUMNS];
		Vector2 buttonSize = new Vector2(CONNECT_FOUR_SIZE.x / inputButtons.length, CONNECT_FOUR_SIZE.y);
		Vector2 tokenSize = new Vector2(buttonSize.x, buttonSize.x);
		for (int i = 0; i < inputButtons.length; i++)
		{
			int column = i;
			inputButtons[column] = new Button(connectFourBoard, currentScene);
			
			Button button = inputButtons[column];
			button.setLocalSize(buttonSize);
			button.setLocalPosition(CONNECT_FOUR_POSITION.x - CONNECT_FOUR_SIZE.x / 2 +  buttonSize.x * (i + 0.5f), CONNECT_FOUR_POSITION.y);
			button.setVisibility(false);
							
			//making hover token
			Token hoverToken = new Token(connectFourBoard, currentScene);
			hoverToken.setLocalSize(tokenSize);
			hoverToken.setLocalPosition(button.getLocalPosition().x, CONNECT_FOUR_POSITION.y + CONNECT_FOUR_SIZE.y / 2 + tokenSize.y / 2);
			hoverToken.setVisibility(false);
			hoverToken.setLayer(-1);
			
			//setting button enter action
			button.setMouseEnterAction(() ->
			{
				//if it is possible to play a token in column then set it to color of turn and make it visible
				if (canInsert(column))
				{
					if (turn == RED)
						hoverToken.setRed();
					else
						hoverToken.setYellow();
					
					hoverToken.setVisibility(true);
				}
				//otherwise destroy hoverToken
				else
					hoverToken.destroy();
			});
			
			//setting mouse exit action
			button.setMouseExitAction(() ->
			{
				//make hoverToken invisible
				if (canInsert(column))
					hoverToken.setVisibility(false);
			});
			
			//setting button click action
			button.setMouseClickAction(() -> 
			{
				//if row is valid, create a token and send it to the correct position
				if (canInsert(column))
				{
					//insert 
					int row = insert(column);
					
					//create a new token
					Token token = new Token(connectFourBoard, currentScene);
					token.setLocalSize(tokenSize);
					token.setLocalPosition(button.getLocalPosition().x, CONNECT_FOUR_POSITION.y + CONNECT_FOUR_SIZE.y / 2 + token.getLocalSize().y / 2);
					
					//setting token to correct color
					if (turn == YELLOW)
						token.setYellow();
					
					//drop this token and make the hover token invisible
					token.drop(CONNECT_FOUR_POSITION.y + CONNECT_FOUR_SIZE.y / 2 - token.getLocalSize().y * (row + 0.5f));
										
					//go to next turn
					nextTurn();
					if (canInsert(column))
						hoverToken.setVisibility(false);
					else
						hoverToken.destroy();
				}
			});
		}
		
		//creating resetButton
		Button resetButton = new Button(currentScene.NORTH_WEST, currentScene);
		resetButton.setText("RESET");
		resetButton.setFont(new Font(resetButton.getFont().getName(), resetButton.getFont().getStyle(), 40));
		resetButton.setLocalSize(.5f, .25f);
		resetButton.setLocalPosition(.30f, -.175f);
		
		//setting resetButton command
		resetButton.setMouseClickAction(() ->
		{
			newGameScene();
		});
				
		//initializing scene
		currentScene.initialize(this);
	}
	
	private boolean canInsert(int column)
	{
		if (connectFour[column] != EMPTY)
			return false;
		
		 int index = column;
		 while(index + COLUMNS < connectFour.length && connectFour[index + COLUMNS] == EMPTY)
			 index += COLUMNS;
	
		return true;
	}
	
	private int insert(int column)
	{
		if (connectFour[column] != EMPTY)
			return -1;
		
		 int index = column;
		 while(index + COLUMNS < connectFour.length && connectFour[index + COLUMNS] == EMPTY)
			 index += COLUMNS;
	
		connectFour[index] = turn;
		
		//returns the row that the token should be inserted to
		return index / COLUMNS;
	}
	
	private void nextTurn()
	{
		if (turn == RED)
			turn = YELLOW;
		else
			turn = RED;
	}
	
	private void setScene(Scene scene)
	{
		if (currentScene != null)
		{
			currentScene.setEnabled(false);
			currentScene.setActive(false);
			remove(currentScene); 
		}
		
		currentScene = scene;
		add(currentScene);
	}
	
	//main method
	public static void main(String[] args)
	{
		ConnectFour connectFour = new ConnectFour("Connect Four");
	}
}
