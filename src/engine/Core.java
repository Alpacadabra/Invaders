package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import screen.GameScreen;
import screen.HighScoreScreen;
import screen.ScoreScreen;
import screen.Screen;
import screen.TitleScreen;
import screen.DifficultyScreen;

/**
 * Implements core game logic.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class Core {

	/** Width of current screen. */
	private static final int WIDTH = 448;
	/** Height of current screen. */
	private static final int HEIGHT = 520;
	/** Max fps of current screen. */
	private static final int FPS = 60;

	/** Max lives. */
	private static final int MAX_LIVES = 3;
	/** Levels between extra life. */
	private static final int EXTRA_LIFE_FRECUENCY = 3;
	/** Total number of levels. */
	private static final int NUM_LEVELS = 7;
	
	/** Difficulty settings for level 1. */
	private static final GameSettings SETTINGS_LEVEL_1 =
			new GameSettings(5, 4, 60, 2000);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2 =
			new GameSettings(5, 5, 50, 2500);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3 =
			new GameSettings(6, 5, 40, 1500);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4 =
			new GameSettings(6, 6, 30, 1500);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5 =
			new GameSettings(7, 6, 20, 1000);
	/** Difficulty settings for level 6. */
	private static final GameSettings SETTINGS_LEVEL_6 =
			new GameSettings(7, 7, 10, 1000);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_7 =
			new GameSettings(8, 7, 5, 500);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_8 =
			new GameSettings(8, 7, 2, 500);
	//normal
	private static final GameSettings SETTINGS_LEVEL_1n =
			new GameSettings(6, 4, 30, 3000);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2n =
			new GameSettings(6, 5, 25, 3500);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3n =
			new GameSettings(6, 5, 20, 4000);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4n =
			new GameSettings(7, 6, 15, 4500);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5n =
			new GameSettings(7, 6, 5, 5000);
	/** Difficulty settings for level 6. */
	private static final GameSettings SETTINGS_LEVEL_6n =
			new GameSettings(7, 7, 2, 5500);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_7n =
			new GameSettings(9, 7, 1, 6000);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_8n =
			new GameSettings(9, 7, 100, 6500);
	//Hard
	private static final GameSettings SETTINGS_LEVEL_1h =
			new GameSettings(7,6,20,100);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2h =
			new GameSettings(7, 5,16, 200);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3h =
			new GameSettings(7, 5, 12, 1500);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4h =
			new GameSettings(8, 6, 8, 1500);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5h =
			new GameSettings(8, 6, 4, 1000);
	/** Difficulty settings for level 6. */
	private static final GameSettings SETTINGS_LEVEL_6h =
			new GameSettings(8, 7, 2, 1000);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_7h =
			new GameSettings(9, 7, 1, 500);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_8h =
			new GameSettings(9,10,1,1000);
	
	/** Frame to draw the screen on. */
	private static Frame frame;
	/** Screen currently shown. */
	private static Screen currentScreen;
	/** Difficulty settings list. */
	private static List<GameSettings> gameSettings;
	/** Difficulty settings list. */
	private static List<GameSettings> gameSettings_Normal;
	/** Difficulty settings list. */
	private static List<GameSettings> gameSettings_Hard;
	/** Application logger. */
	private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());
	/** Logger handler for printing to disk. */
	private static Handler fileHandler;
	/** Logger handler for printing to console. */
	private static ConsoleHandler consoleHandler;
	



	/**
	 * Test implementation.
	 * 
	 * @param args
	 *            Program args, ignored.
	 */
	public static void main(final String[] args) {
		try {
			LOGGER.setUseParentHandlers(false);

			fileHandler = new FileHandler("log");
			fileHandler.setFormatter(new MinimalFormatter());

			consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new MinimalFormatter());

			LOGGER.addHandler(fileHandler);
			LOGGER.addHandler(consoleHandler);
			LOGGER.setLevel(Level.ALL);

		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}

		frame = new Frame(WIDTH, HEIGHT);
		DrawManager.getInstance().setFrame(frame);
		int width = frame.getWidth();
		int height = frame.getHeight();

		gameSettings = new ArrayList<GameSettings>();
		gameSettings.add(SETTINGS_LEVEL_1);
		gameSettings.add(SETTINGS_LEVEL_2);
		gameSettings.add(SETTINGS_LEVEL_3);
		gameSettings.add(SETTINGS_LEVEL_4);
		gameSettings.add(SETTINGS_LEVEL_5);
		gameSettings.add(SETTINGS_LEVEL_6);
		gameSettings.add(SETTINGS_LEVEL_7);
		gameSettings_Normal = new ArrayList<GameSettings>();
		gameSettings_Normal.add(SETTINGS_LEVEL_1n);
		gameSettings_Normal.add(SETTINGS_LEVEL_2n);
		gameSettings_Normal.add(SETTINGS_LEVEL_3n);
		gameSettings_Normal.add(SETTINGS_LEVEL_4n);
		gameSettings_Normal.add(SETTINGS_LEVEL_5n);
		gameSettings_Normal.add(SETTINGS_LEVEL_6n);
		gameSettings_Normal.add(SETTINGS_LEVEL_7n);
		gameSettings_Normal.add(SETTINGS_LEVEL_8n);
		gameSettings_Hard = new ArrayList<GameSettings>();
		gameSettings_Hard.add(SETTINGS_LEVEL_1h);
		gameSettings_Hard.add(SETTINGS_LEVEL_2h);
		gameSettings_Hard.add(SETTINGS_LEVEL_3h);
		gameSettings_Hard.add(SETTINGS_LEVEL_4h);
		gameSettings_Hard.add(SETTINGS_LEVEL_5h);
		gameSettings_Hard.add(SETTINGS_LEVEL_6h);
		gameSettings_Hard.add(SETTINGS_LEVEL_7h);
		gameSettings_Hard.add(SETTINGS_LEVEL_8h);
		GameState gameState;

		int returnCode = 1;
		do {

			switch (returnCode) {
			case 1:
				// Main menu.
				currentScreen = new TitleScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " title screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing title screen.");
				break;
			case 2:
			case 3:
				// Game & score.
				gameState = new GameState(1, 0, MAX_LIVES, 0, 0, returnCode - 1);
				do {
					// One extra live every few levels.
					boolean bonusLife = gameState.getLevel()
							% EXTRA_LIFE_FRECUENCY == 0
							&& gameState.getLivesRemaining() < MAX_LIVES;
					
					
					currentScreen = new GameScreen(gameState,
							gameSettings.get(gameState.getLevel() - 1),
							bonusLife, width, height, FPS);
					
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " game screen at " + FPS + " fps.");
					frame.setScreen(currentScreen);
					LOGGER.info("Closing game screen.");

					gameState = ((GameScreen) currentScreen).getGameState();

					gameState = new GameState(gameState.getLevel() + 1,
							gameState.getScore(),
							gameState.getLivesRemaining(),
							gameState.getBulletsShot(),
							gameState.getShipsDestroyed(),
							gameState.getNumPlayer());

				} while (gameState.getLivesRemaining() > 0
						&& gameState.getLevel() <= NUM_LEVELS);

				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " score screen at " + FPS + " fps, with a score of "
						+ gameState.getScore() + ", "
						+ gameState.getLivesRemaining() + " lives remaining, "
						+ gameState.getBulletsShot() + " bullets shot and "
						+ gameState.getShipsDestroyed() + " ships destroyed.");
				currentScreen = new ScoreScreen(width, height, FPS, gameState);
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing score screen.");
				break;
			case 4:
				// High scores.
				currentScreen = new HighScoreScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " high score screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing high score screen.");
				break;
			case 5:
				currentScreen = new DifficultyScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " Difficulty screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				if(currentScreen.SelectDiff() == 2) {
					gameSettings = gameSettings_Hard;
					LOGGER.info("Difficulty");
				}
				if(currentScreen.SelectDiff() == 1) {
					gameSettings = gameSettings_Normal;
					LOGGER.info("Normal");
				}
				
				LOGGER.info("Closing Difficulty screen.");
				
				
				
			default:
				break;
			}

		} while (returnCode != 0);

		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}

	/**
	 * Constructor, not called.
	 */
	private Core() {

	}

	/**
	 * Controls access to the logger.
	 * 
	 * @return Application logger.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Controls access to the drawing manager.
	 * 
	 * @return Application draw manager.
	 */
	public static DrawManager getDrawManager() {
		return DrawManager.getInstance();
	}

	/**
	 * Controls access to the input manager.
	 * 
	 * @return Application input manager.
	 */
	public static InputManager getInputManager() {
		return InputManager.getInstance();
	}

	/**
	 * Controls access to the file manager.
	 * 
	 * @return Application file manager.
	 */
	public static FileManager getFileManager() {
		return FileManager.getInstance();
	}

	/**
	 * Controls creation of new cooldowns.
	 * 
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @return A new cooldown.
	 */
	public static Cooldown getCooldown(final int milliseconds) {
		return new Cooldown(milliseconds);
	}

	/**
	 * Controls creation of new cooldowns with variance.
	 * 
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @param variance
	 *            Variation in the cooldown duration.
	 * @return A new cooldown with variance.
	 */
	public static Cooldown getVariableCooldown(final int milliseconds,
			final int variance) {
		return new Cooldown(milliseconds, variance);
	}
}