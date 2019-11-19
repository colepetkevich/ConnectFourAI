package connectfour;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import engine.Button;
import engine.Image;
import engine.ImageFactory;
import engine.Scene;
import engine.Vector2;

public class Game extends JFrame
{	
	//class variables
	private Scene currentScene;
	
	public Game(String title)
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
		easyButton.setMouseClickAction(() ->
		{
			gameMode = ConnectFour.EASY;
			newGameScene();
		});
		
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
			gameMode = ConnectFour.TWO_PLAYER;
			newGameScene();
		});
		
		currentScene.initialize(this);
	}

	private static final Color GAME_BACKGROUND_COLOR = new Color(119, 165, 191);

	private ConnectFour game;
	private int gameMode;
	private static final Vector2 CONNECT_FOUR_POSITION = new Vector2(0, 0);
	private static final float CONNECT_FOUR_HEIGHT = 1.5f;

	private void newGameScene()
	{
		//creating a new scene
		createNewScene();
		currentScene.setBackgroundColor(GAME_BACKGROUND_COLOR);

		//creating resetButton
		Button resetButton = new Button(currentScene.NORTH_WEST, currentScene);
		resetButton.setText("Reset");
		resetButton.setFontScale(.75f);
		resetButton.setLocalSize(.5f, .2f);
		resetButton.setLocalPosition(.30f, -.4f);

		//setting resetButton command
		resetButton.setMouseClickAction(() -> newGameScene());

		//creating backButton
		Button backButton = new Button(currentScene.NORTH_WEST, currentScene);
		backButton.setText("Back");
		backButton.setFontScale(.75f);
		backButton.setLocalSize(.5f, .2f);
		backButton.setLocalPosition(.30f, -.15f);
		backButton.setMouseClickAction(() -> newMainMenyScene());

		//creating connect four game
		game = new ConnectFour(currentScene);
		game.setLocalPosition(CONNECT_FOUR_POSITION);
		game.setLocalSize(CONNECT_FOUR_HEIGHT * ConnectFour.COLUMNS / ConnectFour.ROWS, CONNECT_FOUR_HEIGHT);
		game.startNewGame(gameMode);
				
		//initializing scene
		currentScene.initialize(this);
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
	
	//main method
	public static void main(String[] args)
	{
		Game connectFour = new Game("Connect Four");
	}
}
