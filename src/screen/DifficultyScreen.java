package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import engine.Cooldown;
import engine.Core;
import engine.GameState;
import engine.Score;

public class DifficultyScreen extends Screen {

	private static final int SELECTION_TIME = 200;
	
	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;
	
	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public DifficultyScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		// Defaults to play.
		this.returnCode = 5;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();
		this.returnDiff = 0;
		this.Diff = 0;
	}
	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		draw();
		if (this.selectionCooldown.checkFinished()
				&& this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_UP)
					|| inputManager.isKeyDown(KeyEvent.VK_W)) {
				previousMenuItem();
				this.selectionCooldown.reset();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
					|| inputManager.isKeyDown(KeyEvent.VK_S)) {
				nextMenuItem();
				this.selectionCooldown.reset();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
					&& this.inputDelay.checkFinished()) {
				this.isRunning = false;
			}
		}
		
	}

	/**
	 * Shifts the focus to the next menu item.
	 */
	private void nextMenuItem() {
		
		if (this.returnDiff == 2) 
			this.returnDiff = 0;
		else 
			this.returnDiff++;
		this.returnCode = 1;
	}
	
		/**
	 * Shifts the focus to the previous menu item.
	 */

	private void previousMenuItem() {
		
		if (this.returnDiff == 0) 
			this.returnDiff = 2;
		else 
			this.returnDiff--;
		
		this.returnCode = 1;
	}
	

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawD(this);
		drawManager.drawDifficulty(this, this.returnDiff);

		drawManager.completeDrawing(this);
	}
	
	@Override
	public int SelectDiff()  {
		
		return this.returnDiff;
		
	}
	
	
	
	
	
	
	
	
	
}
