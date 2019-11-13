package game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JFrame;

import engine.Button;
import engine.Image;
import engine.ImageFactory;
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
		
		//newGameScene();
		newMainMenyScene();
	}
	
	private static final Color MAIN_MENU_BACKGROUND_COLOR = new Color(246, 234, 123);
	
	private static final String LOGO_PATH = "res/images/Logo00.png";
	private static final Vector2 LOGO_POSITION = new Vector2(0, 0.55f);
	private static final float LOGO_HEIGHT = 0.875f;
	
	private static final Vector2 BUTTON_SIZE = new Vector2(2.0f, .2f);
	private static final Vector2 FIRST_BUTTON_POSITION = new Vector2(0, -0.05f);
	private static final float BUTTON_MARGIN = 0.05f;
	private static final float BUTTON_FONT_SCALE = .85f;

	private void newMainMenyScene()
	{
		createNewScene();
		currentScene.setBackgroundColor(MAIN_MENU_BACKGROUND_COLOR);
		
		//adding logo
		BufferedImage logoImage = ImageFactory.getImageFromPath(LOGO_PATH);
		Image logo = new Image(currentScene);
		logo.setImage(logoImage);
		logo.setLocalSize(LOGO_HEIGHT * logoImage.getWidth() / logoImage.getHeight(), LOGO_HEIGHT);
		logo.setLocalPosition(LOGO_POSITION);
		
		//adding buttons
		Button easyButton = new Button(currentScene.CENTER, currentScene);
		easyButton.setLocalSize(BUTTON_SIZE);
		easyButton.setLocalPosition(FIRST_BUTTON_POSITION);
		easyButton.setText("Easy");
		easyButton.setFontScale(BUTTON_FONT_SCALE);
		
		Button mediumButton = new Button(currentScene.CENTER, currentScene);
		mediumButton.setLocalSize(BUTTON_SIZE);
		mediumButton.setLocalPosition(new Vector2(easyButton.getLocalPosition().x, easyButton.getLocalPosition().y - easyButton.getLocalSize().y - BUTTON_MARGIN));
		mediumButton.setText("Medium");
		mediumButton.setFontScale(BUTTON_FONT_SCALE);
		
		Button hardButton = new Button(currentScene.CENTER, currentScene);
		hardButton.setLocalSize(BUTTON_SIZE);
		hardButton.setLocalPosition(new Vector2(mediumButton.getLocalPosition().x, mediumButton.getLocalPosition().y - mediumButton.getLocalSize().y - BUTTON_MARGIN));
		hardButton.setText("Hard");
		hardButton.setFontScale(BUTTON_FONT_SCALE);
		
		Button twoPlayerButton = new Button(currentScene.CENTER, currentScene);
		twoPlayerButton.setLocalSize(BUTTON_SIZE);
		twoPlayerButton.setLocalPosition(new Vector2(hardButton.getLocalPosition().x, hardButton.getLocalPosition().y - hardButton.getLocalSize().y - BUTTON_MARGIN));
		twoPlayerButton.setText("Two Player");
		twoPlayerButton.setFontScale(BUTTON_FONT_SCALE);
		twoPlayerButton.setMouseClickAction(() ->
		{
			gameMode = TWO_PLAYER;
			newGameScene();
		});
		
		currentScene.initialize(this);
	}
	
	public static final int ROWS = 6;
	public static final int COLUMNS = 7;
	public static final char RED = 'R';
	public static final char YELLOW = 'Y';
	public static final char EMPTY = ' ';
	public static final int WIN_AMOUNT = 4; // How many tokens in a row to win? (Connect 4 or Connect 20?)
	
	private static final Color GAME_BACKGROUND_COLOR = new Color(119, 165, 191);
	private static final Vector2 CONNECT_FOUR_POSITION = new Vector2(0, -.05f);
	private static final float CONNECT_FOUR_HEIGHT = 1.5f;
	private static final Vector2 CONNECT_FOUR_SIZE = new Vector2(CONNECT_FOUR_HEIGHT * COLUMNS / ROWS, CONNECT_FOUR_HEIGHT);

	private char[] connectFour; // MAIN BOARD
	private char turn;
	private char winner;
	private DataSetHandler dataSetHandler;
	private Button[] inputButtons;

	//game modes
	private int gameMode;
	private static final int EASY = 0;
	private static final int MEDIUM = 1;
	private static final int HARD = 2;
	private static final int TWO_PLAYER = 3;
	private void newGameScene()
	{
		//creating a new scene
		createNewScene();
		currentScene.setBackgroundColor(GAME_BACKGROUND_COLOR);		
		
		//creating board
		connectFour = new char[ROWS * COLUMNS];
		Arrays.fill(connectFour, EMPTY);
		turn = RED;
		winner = EMPTY;
		dataSetHandler = new DataSetHandler();

		//creating board graphics
		ConnectFourBoard connectFourBoard = new ConnectFourBoard(currentScene);
		connectFourBoard.setLocalPosition(CONNECT_FOUR_POSITION);
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
				if (canInsert(column) && winner == EMPTY)
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
				if (canInsert(column) && winner == EMPTY)
					hoverToken.setVisibility(false);
			});
			
			//setting button click action
			button.setMouseClickAction(() -> 
			{
				//if row is valid, create a token and send it to the correct position
				if (canInsert(column) && winner == EMPTY)
				{
					//if gameMode is two player and it is Yellow's turn, log current board and move
					if (gameMode == TWO_PLAYER && turn == RED)
							dataSetHandler.addData(Arrays.copyOf(connectFour, connectFour.length), column);

					//insert token
					int index = insert(column);
					int row = index / COLUMNS;

					//checks for winner
					winner = getWinner(index);
					if (winner != EMPTY || boardFull())
					{
						System.out.println("Winner: " + (winner == EMPTY ? "Tie" : winner));

						//if yellow is the winner or it is a tie game, then save the logged data
						if (winner == RED || boardFull())
							dataSetHandler.savaData();
					}


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
		resetButton.setText("Reset");
		resetButton.setFontScale(.75f);
		resetButton.setLocalSize(.5f, .2f);
		resetButton.setLocalPosition(.30f, -.4f);
		
		//setting resetButton command
		resetButton.setMouseClickAction(() -> { newGameScene(); });
		
		//creating backButton
		Button backButton = new Button(currentScene.NORTH_WEST, currentScene);
		backButton.setText("Back");
		backButton.setFontScale(.75f);
		backButton.setLocalSize(.5f, .2f);
		backButton.setLocalPosition(.30f, -.15f);
		backButton.setMouseClickAction(() -> { newMainMenyScene(); } );
				
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

		//returns the index that the token is inserted into
		return index;
	}
	
	private void nextTurn()
	{
		if (turn == RED)
			turn = YELLOW;
		else
			turn = RED;
	}
	
	private char getWinner(int index)
	{
		char winner = relHorizontalCheck(index);
		if (winner != EMPTY)
			return winner;

		winner = relDiagonalCheck(index);
		if (winner != EMPTY)
			return winner;

		return relVerticalCheck(index);
	}

	private boolean boardFull()
	{
		for (char move: connectFour)
			if (move == EMPTY)
				return false;
		return true;
	}

	private void createNewScene()
	{
		if (currentScene != null)
		{
			currentScene.setEnabled(false);
			currentScene.setActive(false);
			remove(currentScene);
		}

		currentScene = new Scene();
		add(currentScene);
		currentScene.setLayout(null);
	}

	/** relDiagonalCheck method
	 * 	Checks the diagnols to the place you just inserted for wins
	 * @param index
	 * @return
	 */
	private char relDiagonalCheck(int index) {

		int currentIndex = index;
		// This while loop goes until the index is on the edge of the board
		// This will specifically check the diagonal that looks like this:  \
		// This means until either of these:
		// index < COLUMNS   (TOP EDGE)
		// index % COLUMNS == 0  (LEFT EDGE)
		// index + COLUMNS >= COLUMNS * ROWS (BOTTOM EDGE)
		// index % COLUMNS == COLUMNS - 1 (RIGHT EDGE)
		while( !(currentIndex < COLUMNS) && currentIndex % COLUMNS != 0) {
			currentIndex = currentIndex - COLUMNS - 1;
		}

		int inARow = 0;
		char currentColor = EMPTY;
		// This while loop goes until the index is on the other side of the board diagonally
		while (!(currentIndex + COLUMNS >= COLUMNS * ROWS) && currentIndex % COLUMNS != COLUMNS - 1) {
			if (connectFour[currentIndex] == currentColor) {
				inARow++;
			}
			else {
				inARow = 1;
			}

			if (currentColor != EMPTY && inARow >= WIN_AMOUNT) {
				return currentColor;
			}

			currentColor = connectFour[currentIndex];
			currentIndex = currentIndex + COLUMNS + 1;
		}

		if (connectFour[currentIndex] == currentColor) {
			inARow++;
		}
		else {
			inARow = 1;
		}

		if (currentColor != EMPTY && inARow >= WIN_AMOUNT) {
			return currentColor;
		}

		// We are now done checking this diagonal:   \
		// Now we must check this one:   /
		// index < COLUMNS   (TOP EDGE)
		// index % COLUMNS == 0  (LEFT EDGE)
		// index + COLUMNS >= COLUMNS * ROWS (BOTTOM EDGE)
		// index % COLUMNS == COLUMNS - 1 (RIGHT EDGE)
		inARow = 0;
		currentIndex = index;

		// This while loop will go until currentIndex is on the edge
		while( currentIndex % COLUMNS != COLUMNS - 1 && !(currentIndex < COLUMNS)) {
			currentIndex = currentIndex - COLUMNS + 1;
		}


		while(currentIndex % COLUMNS != 0 && !(currentIndex + COLUMNS >= COLUMNS * ROWS)) {
			if (connectFour[currentIndex] == currentColor) {
				inARow++;
			}
			else {
				inARow = 1;
			}

			if (currentColor != EMPTY && inARow >= WIN_AMOUNT) {
				return currentColor;
			}

			currentColor = connectFour[currentIndex];
			currentIndex = currentIndex + COLUMNS - 1;
		}

		if (connectFour[currentIndex] == currentColor) {
			inARow++;
		}
		else {
			inARow = 1;
		}

		if (currentColor != EMPTY && inARow >= WIN_AMOUNT) {
			return currentColor;
		}

		return EMPTY;
	}

	/** relVerticalCheck method
	 * Less brute force (still not optimal)
	 * Checks the entire column in which you just inserted for wins
	 * @param index: The place you last inserted a token
	 * @return The Winning team (Empty if none)
	 */
	private char relVerticalCheck(int index) {
		// This while loop brings the index to the start of the column
		while (index > COLUMNS - 1) {
			index-= COLUMNS;
		}

		int inARow = 0;
		char currentColor = EMPTY;

		// This for loop iterates through the entire column
		for (int i = 0; i < ROWS; i++) {
			if (connectFour[index] == currentColor) {
				inARow++;
			}
			else {
				inARow = 1;
				currentColor = connectFour[index];
			}

			if (currentColor != EMPTY && inARow >= WIN_AMOUNT) {
				return currentColor;
			}

			index+=COLUMNS;
		}
		return EMPTY;
	}
    /** relHorizontalCheck method
	 * Checks the entire row in which you just inserted for wins
     * @param index: The place you last inserted a token
     * @return The Winning team (Empty if none)
     */
	private char relHorizontalCheck(int index) {
		// This while loop brings the index to the start of the row
        while (index % COLUMNS != 0) {
			index--;
		}

		int inARow = 0;
        char currentColor = EMPTY;

        // This for loop iterates through the entire row
        for (int i = 0; i < COLUMNS; i++) {
			if (connectFour[index] == currentColor) {
				inARow++;
			}
			else {
				inARow = 1;
				currentColor = connectFour[index];
			}

			if (currentColor != EMPTY && inARow >= WIN_AMOUNT) {
				return currentColor;
			}
			index++;
		}
        return EMPTY;
    }
	
	//main method
	public static void main(String[] args)
	{
		ConnectFour connectFour = new ConnectFour("Connect Four");
	}
}
