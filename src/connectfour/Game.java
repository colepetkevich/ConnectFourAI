package connectfour;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyl
 */

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
	private NNRepresentation nnRep;

	private static final float MAX_UPDATES_PER_SECOND = 1000;
	private static final float FIXED_UPDATES_PER_SECOND = 20;
	
	public Game(String title)
	{
		super();
		setTitle(title);
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		addKeyListener(this);

		//start main menu
		newMainMenuScene();

		//make JFrame visible
		setVisible(true);
	}
	
	private static final Color MAIN_MENU_BACKGROUND_COLOR = new Color(246, 234, 123);
	
	private static final String LOGO_PATH = "res/images/Logo00.png";
	private static final Vector2 LOGO_POSITION = new Vector2(0, 0.55f);
	private static final float LOGO_HEIGHT = 0.875f;
	
	private static final Vector2 BUTTON_SIZE = new Vector2(2.0f, .2f);
	private static final Vector2 FIRST_BUTTON_POSITION = new Vector2(0, -0.05f);
	private static final float BUTTON_MARGIN = 0.05f;
	private static final float BUTTON_FONT_SCALE = .85f;

	/**
	 * newMainMenuScene()
	 * @param
	 * @return void
	 * Creates the main menu screen, which allows people to choose an Easy,Medium,Hard, or Very Hard AI
	 * game to play.
	 */
	private void newMainMenuScene()
	{
		Scene mainMenuScene = new Scene(MAX_UPDATES_PER_SECOND, FIXED_UPDATES_PER_SECOND);
		mainMenuScene.setBackgroundColor(MAIN_MENU_BACKGROUND_COLOR);

		//adding logo
		BufferedImage logoImage = ImageFactory.getImageFromPath(LOGO_PATH);
		Image logo = new Image(mainMenuScene);
		logo.setImage(logoImage);
		logo.setLocalSize(LOGO_HEIGHT * logoImage.getWidth() / logoImage.getHeight(), LOGO_HEIGHT);
		logo.setLocalPosition(LOGO_POSITION);
		
		//adding buttons
		Button easyButton = new Button(mainMenuScene.CENTER, mainMenuScene);
		easyButton.setLocalSize(BUTTON_SIZE);
		easyButton.setLocalPosition(FIRST_BUTTON_POSITION);
		easyButton.setText("Easy");
		easyButton.setLocalFontScale(BUTTON_FONT_SCALE);
		easyButton.setMouseClickAction(() -> newGameScene(ConnectFour.EASY));
		
		Button mediumButton = new Button(mainMenuScene.CENTER, mainMenuScene);
		mediumButton.setLocalSize(BUTTON_SIZE);
		mediumButton.setLocalPosition(new Vector2(easyButton.getLocalPosition().x, easyButton.getLocalPosition().y - easyButton.getLocalSize().y - BUTTON_MARGIN));
		mediumButton.setText("Medium");
		mediumButton.setLocalFontScale(BUTTON_FONT_SCALE);
		mediumButton.setMouseClickAction(() -> newGameScene(ConnectFour.MEDIUM));
		
		Button hardButton = new Button(mainMenuScene.CENTER, mainMenuScene);
		hardButton.setLocalSize(BUTTON_SIZE);
		hardButton.setLocalPosition(new Vector2(mediumButton.getLocalPosition().x, mediumButton.getLocalPosition().y - mediumButton.getLocalSize().y - BUTTON_MARGIN));
		hardButton.setText("Hard");
		hardButton.setLocalFontScale(BUTTON_FONT_SCALE);
		hardButton.setMouseClickAction(() -> newGameScene(ConnectFour.HARD));

		Button verHardButton = new Button(mainMenuScene.CENTER, mainMenuScene);
		verHardButton.setLocalSize(BUTTON_SIZE);
		verHardButton.setLocalPosition(new Vector2(hardButton.getLocalPosition().x, hardButton.getLocalPosition().y - hardButton.getLocalSize().y - BUTTON_MARGIN));
		verHardButton.setText("Very Hard");
		verHardButton.setLocalFontScale(BUTTON_FONT_SCALE);
		verHardButton.setMouseClickAction(() -> newGameScene(ConnectFour.VERY_HARD));
		
//		Button twoPlayerButton = new Button(mainMenuScene.CENTER, mainMenuScene);
//		twoPlayerButton.setLocalSize(BUTTON_SIZE);
//		twoPlayerButton.setLocalPosition(new Vector2(hardButton.getLocalPosition().x, hardButton.getLocalPosition().y - hardButton.getLocalSize().y - BUTTON_MARGIN));
//		twoPlayerButton.setText("Two Player");
//		twoPlayerButton.setLocalFontScale(BUTTON_FONT_SCALE);
//		twoPlayerButton.setMouseClickAction(() -> newGameScene(ConnectFour.TWO_PLAYER));

		//initializing new scene then changing to it
		mainMenuScene.initialize();
		changeScenes(mainMenuScene);
	}

	private static final Color GAME_BACKGROUND_COLOR = new Color(119, 165, 191);

	private ConnectFour game;
	private static final Vector2 CONNECT_FOUR_POSITION = new Vector2(0, 0);
	private static final float CONNECT_FOUR_HEIGHT = 1.5f;

	/**
	 * newGameScene
	 * @param gameMode
	 * @return void
	 * int gameMode represents the difficulty level to create the AI at
	 * This method builds the actual connect four gameplay scene. Creates a ConnectFour object and adds in
	 * a reset and back button to the Game JFrame.
	 */
	private void newGameScene(int gameMode)
	{
		//creating a new game Scene
		Scene gameScene = new Scene(MAX_UPDATES_PER_SECOND, FIXED_UPDATES_PER_SECOND);
		gameScene.setBackgroundColor(GAME_BACKGROUND_COLOR);

		//creating resetButton
		Button resetButton = new Button(gameScene.NORTH_WEST, gameScene);
		resetButton.setText("Reset");
		resetButton.setLocalFontScale(.75f);
		resetButton.setLocalSize(.5f, .2f);
		resetButton.setLocalPosition(.30f, -.4f);

		//setting resetButton command
		resetButton.setMouseClickAction(() ->
		{
			newGameScene(gameMode);

			if (nnRep != null)
			{
				nnRep.setVisible(false);
				nnRep.dispose();
				nnRep = null;
			}
		});

		//creating backButton
		Button backButton = new Button(gameScene.NORTH_WEST, gameScene);
		backButton.setText("Back");
		backButton.setLocalFontScale(.75f);
		backButton.setLocalSize(.5f, .2f);
		backButton.setLocalPosition(.30f, -.15f);
		backButton.setMouseClickAction(() -> {
			newMainMenuScene();

			if (nnRep != null)
			{
				nnRep.setVisible(false);
				nnRep.dispose();
				nnRep = null;
			}
			game = null;
		});

		//creating connect four game
		game = new ConnectFour(gameScene);
		game.setLocalPosition(CONNECT_FOUR_POSITION);
		game.setLocalSize(CONNECT_FOUR_HEIGHT * ConnectFour.COLUMNS / ConnectFour.ROWS, CONNECT_FOUR_HEIGHT);
		game.startNewGame(gameMode);

		//initializing new scene then changing to it
		gameScene.initialize();
		changeScenes(gameScene);
	}

	/**
	 * changeScene()
	 * @param newScene
	 * @return void
	 * This method sets the Game Object's scene to the given scene.
	 */
	private void changeScenes(Scene newScene)
	{
		//remove previous scene
		if (scene != null)
		{
			scene.setEnabled(false);
			scene.setActive(false);
			remove(scene);
		}

		scene = newScene;
		add(scene);
		scene.setLayout(null);

		//fixes any possible text scaling issues on startup
		new Delay(.25f, () -> scene.resizeUpdate(), scene);
	}

	/**
	 *
	 * @param e
	 * KeyEvent e is a KeyEvent object, representing info to tell which key was typed
	 * @return void
	 * The method keyTyped occurs whenever a key is typed
	 */
	@Override
	public void keyTyped(KeyEvent e) {}

	/**
	 *
	 * @param e
	 * KeyEvent e is a KeyEvent object, representing info to tell which key was typed
	 * @return void
	 * The method keyPressed occurs whenever a key is pressed
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyChar() == 'n')
		{
			if (nnRep == null && game != null)
				nnRep = new NNRepresentation(game.getNeuralNetwork(), false);
		}
		else if (e.getKeyChar() == 'r') {
			if (nnRep != null && game != null)
				nnRep.changeColors(game.getNeuralNetwork());
		}
	}

	/**
	 *
	 * @param e
	 * KeyEvent e is a KeyEvent object, representing info to tell which key was typed
	 * @return void
	 * The method keyReleased occurs whenever a key is released
	 */
	@Override
	public void keyReleased(KeyEvent e) {}
	
	//main method
	public static void main(String[] args)
	{
		Game connectFour = new Game("Connect Four");
	}
}
