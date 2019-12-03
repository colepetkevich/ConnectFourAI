package connectfour;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

import engine.*;
import neuralnetwork.NNRepresentation;

public class Game extends JFrame implements KeyListener
{	
	//class variables
	private Scene scene;
	private NNRepresentation nn;
	
	public Game(String title)
	{
		super();
		setTitle(title);
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		addKeyListener(this);

		//newGameScene();
		newMainMenuScene();
	}
	
	private static final Color MAIN_MENU_BACKGROUND_COLOR = new Color(246, 234, 123);
	
	private static final String LOGO_PATH = "res/images/Logo00.png";
	private static final Vector2 LOGO_POSITION = new Vector2(0, 0.55f);
	private static final float LOGO_HEIGHT = 0.875f;
	
	private static final Vector2 BUTTON_SIZE = new Vector2(2.0f, .2f);
	private static final Vector2 FIRST_BUTTON_POSITION = new Vector2(0, -0.05f);
	private static final float BUTTON_MARGIN = 0.05f;
	private static final float BUTTON_FONT_SCALE = .85f;

	private void newMainMenuScene()
	{
		createNewScene();
		scene.setBackgroundColor(MAIN_MENU_BACKGROUND_COLOR);

		//adding logo
		BufferedImage logoImage = ImageFactory.getImageFromPath(LOGO_PATH);
		Image logo = new Image(scene);
		logo.setImage(logoImage);
		logo.setLocalSize(LOGO_HEIGHT * logoImage.getWidth() / logoImage.getHeight(), LOGO_HEIGHT);
		logo.setLocalPosition(LOGO_POSITION);
		
		//adding buttons
		Button easyButton = new Button(scene.CENTER, scene);
		easyButton.setLocalSize(BUTTON_SIZE);
		easyButton.setLocalPosition(FIRST_BUTTON_POSITION);
		easyButton.setText("Easy");
		easyButton.setLocalFontScale(BUTTON_FONT_SCALE);
		easyButton.setMouseClickAction(() -> newGameScene(ConnectFour.EASY));
		
		Button mediumButton = new Button(scene.CENTER, scene);
		mediumButton.setLocalSize(BUTTON_SIZE);
		mediumButton.setLocalPosition(new Vector2(easyButton.getLocalPosition().x, easyButton.getLocalPosition().y - easyButton.getLocalSize().y - BUTTON_MARGIN));
		mediumButton.setText("Medium");
		mediumButton.setLocalFontScale(BUTTON_FONT_SCALE);
		mediumButton.setMouseClickAction(() -> newGameScene(ConnectFour.MEDIUM));
		
		Button hardButton = new Button(scene.CENTER, scene);
		hardButton.setLocalSize(BUTTON_SIZE);
		hardButton.setLocalPosition(new Vector2(mediumButton.getLocalPosition().x, mediumButton.getLocalPosition().y - mediumButton.getLocalSize().y - BUTTON_MARGIN));
		hardButton.setText("Hard");
		hardButton.setLocalFontScale(BUTTON_FONT_SCALE);
		hardButton.setMouseClickAction(() -> newGameScene(ConnectFour.HARD));

		Button twoPlayerButton = new Button(scene.CENTER, scene);
		twoPlayerButton.setLocalSize(BUTTON_SIZE);
		twoPlayerButton.setLocalPosition(new Vector2(hardButton.getLocalPosition().x, hardButton.getLocalPosition().y - hardButton.getLocalSize().y - BUTTON_MARGIN));
		twoPlayerButton.setText("Very Hard");
		twoPlayerButton.setLocalFontScale(BUTTON_FONT_SCALE);
		twoPlayerButton.setMouseClickAction(() -> newGameScene(ConnectFour.VERY_HARD));
		
//		Button twoPlayerButton = new Button(scene.CENTER, scene);
//		twoPlayerButton.setLocalSize(BUTTON_SIZE);
//		twoPlayerButton.setLocalPosition(new Vector2(hardButton.getLocalPosition().x, hardButton.getLocalPosition().y - hardButton.getLocalSize().y - BUTTON_MARGIN));
//		twoPlayerButton.setText("Two Player");
//		twoPlayerButton.setLocalFontScale(BUTTON_FONT_SCALE);
//		twoPlayerButton.setMouseClickAction(() -> newGameScene(ConnectFour.TWO_PLAYER));

		scene.initialize(this);
	}

	private static final Color GAME_BACKGROUND_COLOR = new Color(119, 165, 191);

	private ConnectFour game;
	private static final Vector2 CONNECT_FOUR_POSITION = new Vector2(0, 0);
	private static final float CONNECT_FOUR_HEIGHT = 1.5f;

	private void newGameScene(int gameMode)
	{
		//creating a new scene
		createNewScene();
		scene.setBackgroundColor(GAME_BACKGROUND_COLOR);

		//creating resetButton
		Button resetButton = new Button(scene.NORTH_WEST, scene);
		resetButton.setText("Reset");
		resetButton.setLocalFontScale(.75f);
		resetButton.setLocalSize(.5f, .2f);
		resetButton.setLocalPosition(.30f, -.4f);

		//setting resetButton command
		resetButton.setMouseClickAction(() -> newGameScene(gameMode));

		//creating backButton
		Button backButton = new Button(scene.NORTH_WEST, scene);
		backButton.setText("Back");
		backButton.setLocalFontScale(.75f);
		backButton.setLocalSize(.5f, .2f);
		backButton.setLocalPosition(.30f, -.15f);
		backButton.setMouseClickAction(() -> newMainMenuScene());

		//creating connect four game
		game = new ConnectFour(scene);
		game.setLocalPosition(CONNECT_FOUR_POSITION);
		game.setLocalSize(CONNECT_FOUR_HEIGHT * ConnectFour.COLUMNS / ConnectFour.ROWS, CONNECT_FOUR_HEIGHT);
		game.startNewGame(gameMode);

		//initializing scene
		scene.initialize(this);
	}

	private void createNewScene()
	{
		if (scene != null)
		{
			scene.setEnabled(false);
			scene.setActive(false);
			remove(scene);
		}

		scene = new Scene(1000, 20);
		add(scene);
		scene.setLayout(null);
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyChar() == 'n')
		{
			nn = new NNRepresentation();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	//main method
	public static void main(String[] args)
	{
		Game connectFour = new Game("Connect Four");
	}
}
