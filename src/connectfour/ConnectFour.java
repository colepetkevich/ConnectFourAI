package connectfour;

import java.awt.*;
import java.util.Arrays;

import engine.*;
import engine.Button;
import engine.Image;
import engine.Label;
import graph.Edge;
import neuralnetwork.NeuralNetwork;
import neuralnetwork.WeightNetwork;

public class ConnectFour extends Drawable
{
	public static final char RED = 'R';
	public static final char YELLOW = 'Y';
	public static final char EMPTY = ' ';

	public static final int ROWS = 6;
	public static final int COLUMNS = 7;
	public static final int WIN_AMOUNT = 4; // How many tokens in a row to win? (Connect 4 or Connect 20?)

	private char[] connectFour; // MAIN BOARD
	private char turn;
	private char winner;
	private DataSetHandler dataSetHandler;
	private Button[] inputButtons;

	//connect four ai (neural network)
	private NeuralNetwork connectFourAI;
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
		connectFourAI = new NeuralNetwork(NEURAL_NETWORK_PATH);
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
					if (canInsert(column, connectFour) && winner == EMPTY) {
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
					if (canInsert(column, connectFour) && winner == EMPTY)
						hoverToken.setVisibility(false);
				}
			});

			//setting button click action
			button.setMouseClickAction(() ->
			{
				//only do an action if two player or is yellow's turn
				if (gameMode == TWO_PLAYER || (turn == YELLOW && aiCanPlayMove)) {
					//if row is valid, create a token and send it to the correct position
					if (canInsert(column, connectFour) && winner == EMPTY) {
						//if gameMode is two player and it is Yellow's turn, log current board and move
						if (gameMode == TWO_PLAYER && turn == RED)
							dataSetHandler.addData(Arrays.copyOf(connectFour, connectFour.length), column);

						//insert token
						int index = insert(column, connectFour, turn);
						int row = index / COLUMNS;

						//checks for winner
						winner = getWinner(connectFour);
						if (winner != EMPTY || boardFull(connectFour))
						{
							System.out.println(boardFull(connectFour) ? "Its a tie!" : (winner == RED ? "Red" : "Yellow") + " wins!");

							//if yellow is the winner or it is a tie connectfour, then save the logged data
							if (gameMode == TWO_PLAYER && (turn == RED || boardFull(connectFour)))
								dataSetHandler.savaData();
							else
								dataSetHandler.clearData();
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
						if (canInsert(column, connectFour))
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
		if (gameMode != TWO_PLAYER && turn == RED && aiCanPlayMove && winner == EMPTY && !boardFull(connectFour))
		{
			aiCanPlayMove = false;

			//do ai's move at a delay
			new Delay(AI_PLAY_DELAY, () ->
			{
				aiPlays(connectFourAI);
			}, scene);
		}

		// This is where I will build the win screen pop up
		// Also a temporary LAG MACHINE
		char winner = getWinner(connectFour);
		if (winner != EMPTY) {

			Label l = new Label(parent, scene);
			l.setLocalFontScale(0.3f);

			if (winner == RED) {
				l.setColor(Color.RED);
				l.setText("<html><center>RED<br/>WINS</center></html>");
				l.setLocalSize(0.5f,0.4f);
			}
			else {
				l.setColor(Color.YELLOW);
				l.setText("<html><center>YELLOW<br/>WINS</center></html>");
				l.setLocalSize(0.6f,0.4f);
			}
		}



	}

	public void fixedUpdate() {}
	public void resizeUpdate() {}

	public void draw(Graphics g) {}

	private void aiPlays(NeuralNetwork connectFourAi)
	{
		double[] prediction = connectFourAi.calculate(DataSetHandler.translateBoard(connectFour));

		double[] emptyPrediction = new double[prediction.length];
		Arrays.fill(emptyPrediction, -Double.MAX_VALUE);

		int move = -1;

		//does forced move before ai prediction if on medium or hard game mode
		if (gameMode == MEDIUM || gameMode == HARD)
			move = checkForcedMove(connectFour, turn);

		//if there is not forced move then play ai's move
		if (move < 0)
		{
			move = NeuralNetwork.maxIndex(prediction);

			//making sure move does not result in a loss
			char[] connectFourCopy = Arrays.copyOf(connectFour, connectFour.length);
			insert(move, connectFourCopy, RED);
			int yellowForcedMove = checkForcedMove(connectFourCopy, YELLOW);

			//makes sure index is valid
			//if on hard gameMode make sure current move will not results in a loss
			while (!Arrays.equals(prediction, emptyPrediction) && (!canInsert(move, connectFour) ||
					(gameMode == HARD && yellowForcedMove >= 0 && yellowForcedMove < COLUMNS)))
			{
				prediction[move] = -Double.MAX_VALUE;
				move = NeuralNetwork.maxIndex(prediction);

				//making sure move does not result in a loss
				connectFourCopy = Arrays.copyOf(connectFour, connectFour.length);
				insert(move, connectFourCopy, RED);
				yellowForcedMove = checkForcedMove(connectFourCopy, YELLOW);
			}

			//if all predictions used, play only available space
			if (Arrays.equals(prediction, emptyPrediction))
				for (int i = 0; i < COLUMNS; i++)
					if (canInsert(i, connectFour))
					{
						move = i;
						break;
					}
		}
		//if blccking then adjust the index of move
		else if (move >= COLUMNS)
			move -= COLUMNS;

		int row = insert(move, connectFour, turn) / COLUMNS;

		//checks for winner
		winner = getWinner(connectFour);
		if (winner != EMPTY || boardFull(connectFour))
		{
			System.out.println(boardFull(connectFour) ? "Its a tie!" : (winner == RED ? "Red" : "Yellow") + " wins!");

			//if yellow is the winner or it is a tie connectfour, then save the logged data
			if (gameMode == TWO_PLAYER && (turn == RED || boardFull(connectFour)))
				dataSetHandler.savaData();
			else
				dataSetHandler.clearData();
		}

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

	//HELPER METHODS
	private void nextTurn()
	{
		if (turn == RED)
			turn = YELLOW;
		else
			turn = RED;
	}

	//STATIC HELPER METHODS
	public static boolean canInsert(int column, char[] connectFour)
	{
		if (connectFour[column] != EMPTY)
			return false;

		return true;
	}

	public static int insert(int column, char[] connectFour, final char TURN)
	{
		if (connectFour[column] != EMPTY)
			return -1;

		int index = column;
		while(index + COLUMNS < connectFour.length && connectFour[index + COLUMNS] == EMPTY)
			index += COLUMNS;

		connectFour[index] = TURN;

		//returns the index that the token is inserted into
		return index;
	}

	//checks single index for winner
	public static char getWinner(char[] connectFour, int index)
	{
		int row = index / COLUMNS;
		int column = index % COLUMNS;
		final int MIN_INDEX = 0;
		final int MAX_INDEX = connectFour.length - 1;

		//horizontal
		for (int i = index; i < index + WIN_AMOUNT; i++) {
			//determining horizontal row elements
			char[] horiRow = new char[WIN_AMOUNT];
			boolean horiRowCreated = true;
			for (int j = 0; j < horiRow.length; j++) {
				int currIndex = i - j;

				//if currIndex and index are not on same row then exit
				if (currIndex < MIN_INDEX || currIndex > MAX_INDEX ||
						row != currIndex / COLUMNS || connectFour[currIndex] == EMPTY) {
					horiRowCreated = false;
					break;
				}
				horiRow[j] = connectFour[currIndex];
			}

			if (horiRowCreated) {
				//if all elements in horiRow are the same then currRow is a winning row so return the element values
				boolean allEqual = true;
				for (int j = 0; j < horiRow.length - 1; j++)
					if (horiRow[j] != horiRow[j + 1]) {
						allEqual = false;
						break;
					}
				if (allEqual)
					return horiRow[0];
			}
		}

		//vertical
		for (int i = index; i <= index + COLUMNS * WIN_AMOUNT; i += COLUMNS) {
			//determining vertical row elements
			char[] vertRow = new char[WIN_AMOUNT];
			boolean vertRowCreated = true;
			for (int j = 0; j < vertRow.length; j++) {
				int currIndex = i - j * COLUMNS;

				//if currIndex and index are not on same column then exit
				if (currIndex < MIN_INDEX || currIndex > MAX_INDEX ||
						column != currIndex % COLUMNS || connectFour[currIndex] == EMPTY) {
					vertRowCreated = false;
					break;
				}
				vertRow[j] = connectFour[currIndex];
			}

			if (vertRowCreated) {
				//if all elements in currRow are the same then currRow is a winning row so return the element values
				boolean allEqual = true;
				for (int j = 0; j < vertRow.length - 1; j++)
					if (vertRow[j] != vertRow[j + 1]) {
						allEqual = false;
						break;
					}
				if (allEqual)
					return vertRow[0];
			}
		}

		//increasing diagonal
		for (int i = index; i <= index + (COLUMNS - 1) * WIN_AMOUNT; i += COLUMNS - 1)
		{
			//determining diagonal row elements
			char[] diagRow = new char[WIN_AMOUNT];
			boolean diagRowCreated = true;
			for (int j = 0; j < diagRow.length; j++) {
				int currIndex = i - j * (COLUMNS - 1);

				//if currIndex and index are not on same diagonal then exit
				if (currIndex < MIN_INDEX || currIndex > MAX_INDEX ||
						row + column != currIndex / COLUMNS + currIndex % COLUMNS || connectFour[currIndex] == EMPTY) {
					diagRowCreated = false;
					break;
				}
				diagRow[j] = connectFour[currIndex];
			}

			if (diagRowCreated) {
				//if all elements in currRow are the same then currRow is a winning row so return the element values
				boolean allEqual = true;
				for (int j = 0; j < diagRow.length - 1; j++)
					if (diagRow[j] != diagRow[j + 1]) {
						allEqual = false;
						break;
					}
				if (allEqual)
					return diagRow[0];
			}
		}

		//decreasing diagonal
		for (int i = index; i <= index + (COLUMNS + 1) * WIN_AMOUNT; i += COLUMNS + 1)
		{
			//determining diagonal row elements
			char[] diagRow = new char[WIN_AMOUNT];
			boolean diagRowCreated = true;
			for (int j = 0; j < diagRow.length; j++) {
				int currIndex = i - j * (COLUMNS + 1);

				//if currIndex and index are not on same diagonal then exit or
				if (currIndex < MIN_INDEX || currIndex > MAX_INDEX ||
						row - column != currIndex / COLUMNS - currIndex % COLUMNS || connectFour[currIndex] == EMPTY) {
					diagRowCreated = false;
					break;
				}
				diagRow[j] = connectFour[currIndex];
			}

			if (diagRowCreated) {
				//if all elements in currRow are the same then currRow is a winning row so return the element values
				boolean allEqual = true;
				for (int j = 0; j < diagRow.length - 1; j++)
					if (diagRow[j] != diagRow[j + 1]) {
						allEqual = false;
						break;
					}
				if (allEqual)
					return diagRow[0];
			}
		}

		return EMPTY;
	}

	//checks entire board for winner
	private static char getWinner(char[] connectFour) {
		for (int i = 0; i < connectFour.length; i++) {
			int row = i / COLUMNS;
			int column = i % COLUMNS;
			//West
			if (column >= WIN_AMOUNT - 1) {
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i - j]) {
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//North West
			if (row >= WIN_AMOUNT - 1 && column >= WIN_AMOUNT - 1) {
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i - j * (COLUMNS + 1)]) {
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//North
			if (row >= WIN_AMOUNT - 1) {
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i - j * COLUMNS]) {
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//North East
			if (row >= WIN_AMOUNT - 1 && COLUMNS - column >= WIN_AMOUNT) {
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i - j * (COLUMNS - 1)]) {
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//East
			if (COLUMNS - column >= WIN_AMOUNT) {
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i + j]) {
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//South East
			if (ROWS - row >= WIN_AMOUNT && COLUMNS - column >= WIN_AMOUNT) {
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i + j * (COLUMNS + 1)]) {
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//South
			if (ROWS - row >= WIN_AMOUNT) {
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i + j * COLUMNS]) {
						isWinner = false;
						break;
					}

				if (isWinner)
					return connectFour[i];
			}
			//South West
			if (ROWS - row >= WIN_AMOUNT && column >= WIN_AMOUNT - 1) {
				boolean isWinner = true;
				for (int j = 1; j < WIN_AMOUNT; j++)
					if (connectFour[i] == EMPTY || connectFour[i] != connectFour[i + j * (COLUMNS - 1)]) {
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

	private static boolean boardFull(char[] connectFour)
	{
		for (char move: connectFour)
			if (move == EMPTY)
				return false;
		return true;
	}

	public static int checkForcedMove(char[] connectFour, final char TURN)
	{
		int blockingMove = -1;

		for (int i = 0; i < connectFour.length; i++)
		{
			int row = i / COLUMNS;
			int column = i % COLUMNS;
			//West
			if (column >= WIN_AMOUNT - 1)
			{
				int move = getForcedMove(i,-1, connectFour, TURN);

				//set blockingMove to move if move is a blocking move
				if (move >= COLUMNS)
					blockingMove = move;
				//return move if move is a winning move
				else if (move >= 0)
					return move;
			}
			//North West
			if (row >= WIN_AMOUNT - 1 && column >= WIN_AMOUNT - 1)
			{
				int move = getForcedMove(i, -(COLUMNS + 1), connectFour, TURN);

				//set blockingMove to move if move is a blocking move
				if (move >= COLUMNS)
					blockingMove = move;
					//return move if move is a winning move
				else if (move >= 0)
					return move;
			}
			//North
			if (row >= WIN_AMOUNT - 1)
			{
				int move = getForcedMove(i, -COLUMNS, connectFour, TURN);

				//set blockingMove to move if move is a blocking move
				if (move >= COLUMNS)
					blockingMove = move;
					//return move if move is a winning move
				else if (move >= 0)
					return move;
			}
			//North East
			if (row >= WIN_AMOUNT - 1 && COLUMNS - column >= WIN_AMOUNT)
			{
				int move = getForcedMove(i, -(COLUMNS - 1), connectFour, TURN);

				//set blockingMove to move if move is a blocking move
				if (move >= COLUMNS)
					blockingMove = move;
					//return move if move is a winning move
				else if (move >= 0)
					return move;
			}
			//East
			if (COLUMNS - column >= WIN_AMOUNT)
			{
				int move = getForcedMove(i, 1, connectFour, TURN);

				//set blockingMove to move if move is a blocking move
				if (move >= COLUMNS)
					blockingMove = move;
					//return move if move is a winning move
				else if (move >= 0)
					return move;
			}
			//South East
			if (ROWS - row >= WIN_AMOUNT && COLUMNS - column >= WIN_AMOUNT)
			{
				int move = getForcedMove(i, COLUMNS + 1, connectFour, TURN);

				//set blockingMove to move if move is a blocking move
				if (move >= COLUMNS)
					blockingMove = move;
					//return move if move is a winning move
				else if (move >= 0)
					return move;
			}
			//South
			if (ROWS - row >= WIN_AMOUNT)
			{
				int move = getForcedMove(i, COLUMNS, connectFour, TURN);

				//set blockingMove to move if move is a blocking move
				if (move >= COLUMNS)
					blockingMove = move;
					//return move if move is a winning move
				else if (move >= 0)
					return move;
			}
			//South West
			if (ROWS - row >= WIN_AMOUNT && column >= WIN_AMOUNT - 1)
			{
				int move = getForcedMove(i, COLUMNS - 1, connectFour, TURN);

				//set blockingMove to move if move is a blocking move
				if (move >= COLUMNS)
					blockingMove = move;
					//return move if move is a winning move
				else if (move >= 0)
					return move;
			}
		}

		//returns blocking move
		return blockingMove;
	}

	private static int getForcedMove(int i, int offset, char[] connectFour, final char TURN)
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

		//if winning then returns: [0, COLUMN)
		//if blocking then returns: [COLUMN, 2*COLUMN)
		return (connectFour[i] == TURN ? 0 : COLUMNS) + emptySpot % COLUMNS;
	}
}
