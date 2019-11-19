package connectfour;

import java.awt.*;
import java.util.Arrays;

import engine.*;
import engine.Button;
import engine.Image;
import neuralnetwork.NeuralNetwork;

public class ConnectFour extends Drawable
{
	public static final int ROWS = 6;
	public static final int COLUMNS = 7;
	public static final char RED = 'R';
	public static final char YELLOW = 'Y';
	public static final char EMPTY = ' ';
	public static final int WIN_AMOUNT = 4; // How many tokens in a row to win? (Connect 4 or Connect 20?)

	private char[] connectFour; // MAIN BOARD
	private char turn;
	private char winner;
	private DataSetHandler dataSetHandler;
	private Button[] inputButtons;

	//connect four ai (neural network)
	private NeuralNetwork connectFourAi;
	private boolean aiCanPlayMove;
	private final float AI_PLAY_DELAY = .75f;
	private static final String NEURAL_NETWORK_PATH = "res/files/NeuralNetwork.dat";

	//connectfour modes
	private int gameMode;
	public static final int EASY = 0;
	public static final int MEDIUM = 1;
	public static final int HARD = 2;
	public static final int TWO_PLAYER = 3;

	private static final String BOARD_TILE_PATH = "res/images/BoardTile.png";
	
	public ConnectFour(Scene scene)
	{
		super(scene);
	}
	
	public ConnectFour(Drawable parent, Scene scene)
	{
		super(parent, scene);
	}
	
	public void startNewGame(int gameMode)
	{
		this.gameMode = gameMode;

		connectFour = new char[ROWS * COLUMNS];
		Arrays.fill(connectFour, EMPTY);
		turn = RED;
		winner = EMPTY;
		dataSetHandler = new DataSetHandler();

		//calculating the width based on height, rows, and columns
		float width = getLocalSize().y * COLUMNS / ROWS;
		
		final int NUM_OF_TILES = ROWS * COLUMNS;
		//setting boardTiles position and size off of this ConnectFour's position and size
		for (int i = 0; i < NUM_OF_TILES; i++)
		{
			int row = i / COLUMNS;
			int column = i % COLUMNS;
					
			//creating new 
			Image tile = new Image(this, scene);
			tile.setImage(BOARD_TILE_PATH);
			tile.setLocalSize(width / COLUMNS, width / COLUMNS);
			tile.setLocalPosition(getLocalPosition().x - width / 2 + tile.getLocalSize().x * (column + 0.5f), getLocalPosition().y + getLocalSize().y / 2 - tile.getLocalSize().y * (row + 0.5f));
			//make tiles slightly bigger so that there are no gap between tiles
			tile.setLocalSize(1.01f * tile.getLocalSize().x, 1.01f * tile.getLocalSize().y);
		}

		//creating connect four ai
		connectFourAi = new NeuralNetwork(NEURAL_NETWORK_PATH);
		aiCanPlayMove = gameMode != TWO_PLAYER;

		//creating input buttons
		inputButtons = new Button[COLUMNS];
		Vector2 buttonSize = new Vector2(getLocalSize().x / inputButtons.length, getLocalSize().y);
		Vector2 tokenSize = new Vector2(buttonSize.x, buttonSize.x);
		for (int i = 0; i < inputButtons.length; i++) {
			int column = i;
			inputButtons[column] = new Button(this, scene);

			Button button = inputButtons[column];
			button.setLocalSize(buttonSize);
			button.setLocalPosition(getLocalPosition().x - getLocalSize().x / 2 + buttonSize.x * (i + 0.5f), getLocalPosition().y);
			button.setVisibility(false);

			//making hover token
			Token hoverToken = new Token(this, scene);
			hoverToken.setLocalSize(tokenSize);
			hoverToken.setLocalPosition(button.getLocalPosition().x, getLocalPosition().y + getLocalSize().y / 2 + tokenSize.y / 2);
			hoverToken.setVisibility(false);
			hoverToken.setLayer(-1);

			//setting button enter action
			button.setMouseEnterAction(() ->
			{
				//only do an action if two player or is yellow's turn
				if (gameMode == TWO_PLAYER || (turn == YELLOW && aiCanPlayMove)) {
					//if it is possible to play a token in column then set it to color of turn and make it visible
					if (canInsert(column) && winner == EMPTY) {
						if (turn == RED)
							hoverToken.setRed();
						else
							hoverToken.setYellow();

						hoverToken.setVisibility(true);
					}
					//otherwise destroy hoverToken
					else
						hoverToken.destroy();
				}
			});

			//setting mouse exit action
			button.setMouseExitAction(() ->
			{
				//only do an action if two player or is yellow's turn
				if (gameMode == TWO_PLAYER || (turn == YELLOW && aiCanPlayMove)) {
					//make hoverToken invisible
					if (canInsert(column) && winner == EMPTY)
						hoverToken.setVisibility(false);
				}
			});

			//setting button click action
			button.setMouseClickAction(() ->
			{
				//only do an action if two player or is yellow's turn
				if (gameMode == TWO_PLAYER || (turn == YELLOW && aiCanPlayMove)) {
					//if row is valid, create a token and send it to the correct position
					if (canInsert(column) && winner == EMPTY) {
						//if gameMode is two player and it is Yellow's turn, log current board and move
						if (gameMode == TWO_PLAYER && turn == RED)
							dataSetHandler.addData(Arrays.copyOf(connectFour, connectFour.length), column);

						//insert token
						int index = insert(column);
						int row = index / COLUMNS;

						//checks for winner
						//winner = getWinner(index);
						winner = getWinner();
						if (winner != EMPTY || boardFull()) {
							System.out.println("Winner: " + (winner == EMPTY ? "Tie" : winner));

							//if yellow is the winner or it is a tie connectfour, then save the logged data
							if (winner == RED || boardFull())
								dataSetHandler.savaData();
						}

						//create a new token
						Token token = new Token(this, scene);
						token.setLocalSize(tokenSize);
						token.setLocalPosition(button.getLocalPosition().x, getLocalPosition().y + getLocalSize().y / 2 + token.getLocalSize().y / 2);

						//setting token to correct color
						if (turn == YELLOW)
							token.setYellow();

						//drop this token and make the hover token invisible
						token.drop(getLocalPosition().y + getLocalSize().y / 2 - token.getLocalSize().y * (row + 0.5f));

						//go to next turn
						nextTurn();
						if (canInsert(column))
							hoverToken.setVisibility(false);
						else
							hoverToken.destroy();
					}
				}
			});
		}
	}

	public void update()
	{


		//only have ai play if not two player mode, turn is RED, and game is not over
		if (gameMode != TWO_PLAYER && turn == RED && aiCanPlayMove && winner == EMPTY && !boardFull())
		{
			aiCanPlayMove = false;

			//do ai's move at a delay
			new Delay(AI_PLAY_DELAY, () ->
			{
				aiPlays(connectFourAi);
			}, scene);
		}
	}

	public void fixedUpdate() {}
	public void resizeUpdate() {}

	public void draw(Graphics g) {}

	private void aiPlays(NeuralNetwork connectFourAi)
	{
		double[] prediction = connectFourAi.calculate(DataSetHandler.translateBoard(connectFour));

		//does forced move before ai prediction
		int move = checkForcedMove();

		//if there is not forced move then play ai's move
		if (move == -1)
		{
			move = NeuralNetwork.maxIndex(prediction);
			//System.out.println(Arrays.toString(prediction));
			//makes sure index is valid
			while (!canInsert(move))
			{
				prediction[move] = -Double.MAX_VALUE;
				move = NeuralNetwork.maxIndex(prediction);
			}
		}

		int row = insert(move) / COLUMNS;
		//saving winner
		//winner = getWinner(move);
		winner = getWinner();

		Vector2 tokenSize = new Vector2(getLocalSize().x / COLUMNS, getLocalSize().x / COLUMNS);

		//create a new token
		Token token = new Token(this, scene);
		token.setLocalSize(tokenSize);
		token.setLocalPosition(getLocalPosition().x - getLocalSize().x / 2 +  tokenSize.x * (move + 0.5f), getLocalPosition().y + getLocalSize().y / 2 + token.getLocalSize().y / 2);

		//drop this token and make the hover token invisible
		token.drop(getLocalPosition().y + getLocalSize().y / 2 - token.getLocalSize().y * (row + 0.5f));

		nextTurn();
		aiCanPlayMove = true;
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

	private char getWinner()
	{
		for (int i = 0; i < connectFour.length; i++)
		{
			int row = i / COLUMNS;
			int column = i % COLUMNS;
			//West
			if (column >= WIN_AMOUNT - 1)
			{
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i - j])
					{
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//North West
			if (row >= WIN_AMOUNT - 1 && column >= WIN_AMOUNT - 1)
			{
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i - j * (COLUMNS + 1)])
					{
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//North
			if (row >= WIN_AMOUNT - 1)
			{
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i - j * COLUMNS])
					{
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//North East
			if (row >= WIN_AMOUNT - 1 && COLUMNS - column >= WIN_AMOUNT)
			{
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i - j * (COLUMNS - 1)])
					{
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//East
			if (COLUMNS - column >= WIN_AMOUNT)
			{
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i + j])
					{
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//South East
			if (ROWS - row >= WIN_AMOUNT && COLUMNS - column >= WIN_AMOUNT)
			{
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i + j * (COLUMNS + 1)])
					{
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//South
			if (ROWS - row >= WIN_AMOUNT)
			{
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i + j * COLUMNS])
					{
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//South West
			if (ROWS - row >= WIN_AMOUNT && column >= WIN_AMOUNT - 1)
			{
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i + j * (COLUMNS - 1)])
					{
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
		}

		//no winner
		return EMPTY;
	}

	private boolean boardFull()
	{
		for (char move: connectFour)
			if (move == EMPTY)
				return false;
		return true;
	}

	private int checkForcedMove()
	{
		for (int i = 0; i < connectFour.length; i++)
		{
			int row = i / COLUMNS;
			int column = i % COLUMNS;
			//West
			if (column >= WIN_AMOUNT - 1)
			{
				int forcedMove = getForcedMove(i,-1);
				if (forcedMove != -1)
					return forcedMove;
			}
			//North West
			if (row >= WIN_AMOUNT - 1 && column >= WIN_AMOUNT - 1)
			{
				int forcedMove = getForcedMove(i, -(COLUMNS + 1));
				if (forcedMove != -1)
					return forcedMove;
			}
			//North
			if (row >= WIN_AMOUNT - 1)
			{
				int forcedMove = getForcedMove(i, -COLUMNS);
				if (forcedMove != -1)
					return forcedMove;
			}
			//North East
			if (row >= WIN_AMOUNT - 1 && COLUMNS - column >= WIN_AMOUNT)
			{
				int forcedMove = getForcedMove(i, -(COLUMNS - 1));
				if (forcedMove != -1)
					return forcedMove;
			}
			//East
			if (COLUMNS - column >= WIN_AMOUNT)
			{
				int forcedMove = getForcedMove(i, 1);
				if (forcedMove != -1)
					return forcedMove;
			}
			//South East
			if (ROWS - row >= WIN_AMOUNT && COLUMNS - column >= WIN_AMOUNT)
			{
				int forcedMove = getForcedMove(i, COLUMNS + 1);
				if (forcedMove != -1)
					return forcedMove;
			}
			//South
			if (ROWS - row >= WIN_AMOUNT)
			{
				int forcedMove = getForcedMove(i, COLUMNS);
				if (forcedMove != -1)
					return forcedMove;
			}
			//South West
			if (ROWS - row >= WIN_AMOUNT && column >= WIN_AMOUNT - 1)
			{
				int forcedMove = getForcedMove(i, COLUMNS - 1);
				if (forcedMove != -1)
					return forcedMove;
			}
		}

		//no forced move
		return -1;
	}

	private int getForcedMove(int i, int offset)
	{
		int emptySpot = -1;
		char pos = EMPTY;
		for (int j = 0; j < WIN_AMOUNT; j++)
		{
			int index = i + j * offset;

			if (emptySpot == -1 && connectFour[index] == EMPTY && (index / COLUMNS == ROWS - 1 || connectFour[index + COLUMNS] != EMPTY))
				emptySpot = index;
			else if (pos == EMPTY && connectFour[index] != EMPTY)
				pos = connectFour[index];
			else if (connectFour[index] != pos || connectFour[index] == EMPTY)
				return -1;
		}

		return emptySpot % COLUMNS;
	}
}
