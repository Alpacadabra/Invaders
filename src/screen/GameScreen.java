package screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

import engine.Cooldown;
import engine.Core;
import engine.GameSettings;
import engine.GameState;
import entity.Bullet;
import entity.BulletPool;
import entity.EnemyShip;
import entity.EnemyShipFormation;
import entity.Entity;
import entity.Ship;

/**
 * Implements the game screen, where the action happens.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class GameScreen extends Screen {

	/** Milliseconds until the screen accepts user input. */
	private static final int INPUT_DELAY = 6000;
	/** Bonus score for each life remaining at the end of the level. */
	private static final int LIFE_SCORE = 100;
	/** Minimum time between bonus ship's appearances. */
	private static final int BONUS_SHIP_INTERVAL = 20000;
	/** Maximum variance in the time between bonus ship's appearances. */
	private static final int BONUS_SHIP_VARIANCE = 10000;
	/** Time until bonus ship explosion disappears. */
	private static final int BONUS_SHIP_EXPLOSION = 500;
	/** Time from finishing the level to screen change. */
	private static final int SCREEN_CHANGE_INTERVAL = 1500;
	/** Height of the interface separation line. */
	private static final int SEPARATION_LINE_HEIGHT = 40;

	/** Current game difficulty settings. */
	private GameSettings gameSettings;
	/** Current difficulty level number. */
	private int level;
	/** Formation of enemy ships. */
	private EnemyShipFormation enemyShipFormation;
	/** Player's ship. */
	private Ship shipA, shipB;
	/** Bonus enemy ship that appears sometimes. */
	private EnemyShip enemyShipSpecial;
	/** Minimum time between bonus ship appearances. */
	private Cooldown enemyShipSpecialCooldown;
	/** Time until bonus ship explosion disappears. */
	private Cooldown enemyShipSpecialExplosionCooldown;
	/** Time from finishing the level to screen change. */
	private Cooldown screenFinishedCooldown;
	/** Set of all bullets fired by on screen ships. */
	private Set<Bullet> bullets;
	/** Current score. */
	private int score;
	/** Player lives left. */
	private int lives;
	/** Total bullets shot by the player. */
	private int bulletsShot;
	/** Total ships destroyed by the player. */
	private int shipsDestroyed;
	/** Moment the game starts. */
	private long gameStartTime;
	/** Checks if the level is finished. */
	private boolean levelFinished;
	/** Checks if a bonus life is received. */
	private boolean bonusLife;
	/** Number of Player. */
	private int numPlayer;
	/** To randomize color. */
	private Random random;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param gameState
	 *            Current game state.
	 * @param gameSettings
	 *            Current game settings.
	 * @param bonnusLife
	 *            Checks if a bonus life is awarded this level.
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public GameScreen(final GameState gameState,
			final GameSettings gameSettings, final boolean bonusLife,
			final int width, final int height, final int fps) {
		super(width, height, fps);

		this.gameSettings = gameSettings;
		this.bonusLife = bonusLife;
		this.level = gameState.getLevel();
		this.score = gameState.getScore();
		this.lives = gameState.getLivesRemaining();
		this.numPlayer = gameState.getNumPlayer();
		if (this.bonusLife)
			this.lives++;
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public final void initialize() {
		super.initialize();

		enemyShipFormation = new EnemyShipFormation(this.gameSettings);
		enemyShipFormation.attach(this);
		
		// Generate ships
		this.random = new Random();
		int randomR = random.nextInt(256);
		int randomG = random.nextInt(256);
		int randomB = random.nextInt(256);
		if(this.numPlayer == 1)
			this.shipA = new Ship((this.width / 2), this.height - 30, new Color(randomR, randomG, randomB));
		else if(this.numPlayer == 2) {
			this.shipA = new Ship((this.width / 2) - 100, this.height - 30, new Color(randomR, randomG, randomB));
			this.shipB = new Ship((this.width / 2) + 100, this.height - 30, new Color(Math.abs(randomR - 255), Math.abs(randomG - 255), Math.abs(randomB - 255)));
		}
		
		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(
				BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core
				.getCooldown(BONUS_SHIP_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
		this.bullets = new HashSet<Bullet>();

		// Special input delay / countdown.
		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();
	}

	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		this.score += LIFE_SCORE * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.score);

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	
	private boolean esc [] = {false,false};
	private int i=0;
	private boolean esc_decision = true;

	protected final void update() {
		super.update();
		
		
		if(inputManager.isKeyDown(KeyEvent.VK_ESCAPE))
			esc[i] = true;
		else
			esc[i] = false;
		if(i==0) {
			if(esc[i]==true && esc[1]==false) 
				esc_decision = !esc_decision;
			i++;
			}
			
		else if(i==1) {
			if(esc[i]==true&&esc[0]==false) 
				esc_decision = !esc_decision;	
			i=0;
		}
		
		if(esc_decision) {		
			if (this.inputDelay.checkFinished() && !this.levelFinished) {				
				if (!this.shipA.isDestroyed()) {
					boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_D);
					boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_A);
	
					boolean isRightBorder = this.shipA.getPositionX()
							+ this.shipA.getWidth() + this.shipA.getSpeed() > this.width - 1;
					boolean isLeftBorder = this.shipA.getPositionX()
							- this.shipA.getSpeed() < 1;
	
					if (moveRight && !isRightBorder) {
						this.shipA.moveRight(); }
					if (moveLeft && !isLeftBorder) {
						this.shipA.moveLeft();
					}
					if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
						if (this.shipA.shoot(this.bullets))
							this.bulletsShot++;
				}
				
				if (numPlayer == 2) {
					if (!this.shipB.isDestroyed()) {
						boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
						boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT);
		
						boolean isRightBorder = this.shipB.getPositionX()
								+ this.shipB.getWidth() + this.shipB.getSpeed() > this.width - 1;
						boolean isLeftBorder = this.shipB.getPositionX()
								- this.shipB.getSpeed() < 1;
		
						if (moveRight && !isRightBorder) {
							this.shipB.moveRight(); }
						if (moveLeft && !isLeftBorder) {
							this.shipB.moveLeft();
						}
						if (inputManager.isKeyDown(KeyEvent.VK_SHIFT))
							if (this.shipB.shoot(this.bullets))
								this.bulletsShot++;
					}
				}
	
				if (this.enemyShipSpecial != null) {
					if (!this.enemyShipSpecial.isDestroyed())
						this.enemyShipSpecial.move(2, 0);
					else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
						this.enemyShipSpecial = null;
	
				}
				if (this.enemyShipSpecial == null
						&& this.enemyShipSpecialCooldown.checkFinished()) {
					this.enemyShipSpecial = new EnemyShip();
					this.enemyShipSpecialCooldown.reset();
					this.logger.info("A special ship appears");
				}
				if (this.enemyShipSpecial != null
						&& this.enemyShipSpecial.getPositionX() > this.width) {
					this.enemyShipSpecial = null;
					this.logger.info("The special ship has escaped");
				}
	
				this.shipA.update();
				this.enemyShipFormation.update();
				this.enemyShipFormation.shoot(this.bullets);
				
			}
	
			manageCollisions();
			cleanBullets();
			draw();
	
			if ((this.enemyShipFormation.isEmpty() || this.lives == 0)
					&& !this.levelFinished) {
				this.levelFinished = true;
				this.screenFinishedCooldown.reset();
			}
	
			if (this.levelFinished && this.screenFinishedCooldown.checkFinished())
				this.isRunning = false;
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawEntity(this.shipA, this.shipA.getPositionX(),
				this.shipA.getPositionY());
		if (numPlayer == 2) drawManager.drawEntity(this.shipB, this.shipB.getPositionX(),
				this.shipB.getPositionY());
		if (this.enemyShipSpecial != null)
			drawManager.drawEntity(this.enemyShipSpecial,
					this.enemyShipSpecial.getPositionX(),
					this.enemyShipSpecial.getPositionY());

		enemyShipFormation.draw();

		for (Bullet bullet : this.bullets)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());

		// Interface.
		drawManager.drawScore(this, this.score);
		drawManager.drawLives(this, this.lives);
		drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);

		// Countdown to game start.
		if (!this.inputDelay.checkFinished()) {
			int countdown = (int) ((INPUT_DELAY
					- (System.currentTimeMillis()
							- this.gameStartTime)) / 1000);
			drawManager.drawCountDown(this, this.level, countdown,
					this.bonusLife);
			drawManager.drawHorizontalLine(this, this.height / 2 - this.height
					/ 12);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height
					/ 12);
		}

		drawManager.completeDrawing(this);
	}

	/**
	 * Cleans bullets that go off screen.
	 */
	private void cleanBullets() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets) {
			bullet.update();
			if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height)
				recyclable.add(bullet);
		}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}

	/**
	 * Manages collisions between bullets and ships.
	 */
	private void manageCollisions() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets)
			if (bullet.getSpeed() > 0) {
				if (checkCollision(bullet, this.shipA) && !this.levelFinished) {
					recyclable.add(bullet);
					if (!this.shipA.isDestroyed()) {
						this.shipA.destroy();
						this.lives--;
						this.logger.info("Hit on player ship, " + this.lives
								+ " lives remaining.");
					}
				}
				if (numPlayer == 2) {
					if (checkCollision(bullet, this.shipB) && !this.levelFinished) {
						recyclable.add(bullet);
						if (!this.shipB.isDestroyed()) {
							this.shipB.destroy();
							this.lives--;
							this.logger.info("Hit on player ship, " + this.lives
									+ " lives remaining.");
						}
					}
				}
				
			} else {
				for (EnemyShip enemyShip : this.enemyShipFormation)
					if (!enemyShip.isDestroyed()
							&& checkCollision(bullet, enemyShip)) {
						this.score += enemyShip.getPointValue();
						this.shipsDestroyed++;
						this.enemyShipFormation.destroy(enemyShip);
						recyclable.add(bullet);
					}
				if (this.enemyShipSpecial != null
						&& !this.enemyShipSpecial.isDestroyed()
						&& checkCollision(bullet, this.enemyShipSpecial)) {
					this.score += this.enemyShipSpecial.getPointValue();
					this.shipsDestroyed++;
					this.enemyShipSpecial.destroy();
					this.enemyShipSpecialExplosionCooldown.reset();
					recyclable.add(bullet);
				}
			}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}

	/**
	 * Checks if two entities are colliding.
	 * 
	 * @param a
	 *            First entity, the bullet.
	 * @param b
	 *            Second entity, the ship.
	 * @return Result of the collision test.
	 */
	private boolean checkCollision(final Entity a, final Entity b) {
		// Calculate center point of the entities in both axis.
		int centerAX = a.getPositionX() + a.getWidth() / 2;
		int centerAY = a.getPositionY() + a.getHeight() / 2;
		int centerBX = b.getPositionX() + b.getWidth() / 2;
		int centerBY = b.getPositionY() + b.getHeight() / 2;
		// Calculate maximum distance without collision.
		int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
		int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
		// Calculates distance.
		int distanceX = Math.abs(centerAX - centerBX);
		int distanceY = Math.abs(centerAY - centerBY);

		return distanceX < maxDistanceX && distanceY < maxDistanceY;
	}

	/**
	 * Returns a GameState object representing the status of the game.
	 * 
	 * @return Current game state.
	 */
	public final GameState getGameState() {
		return new GameState(this.level, this.score, this.lives,
				this.bulletsShot, this.shipsDestroyed, this.numPlayer);
	}
}