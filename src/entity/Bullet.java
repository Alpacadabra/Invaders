package entity;

import java.awt.Color;

import engine.DrawManager.SpriteType;

/**
 * Implements a bullet that moves vertically up or down.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Bullet extends Entity {
	private Color color;
	/**
	 * Speed of the bullet, positive or negative depending on direction -
	 * positive is down.
	 */
	private int speed;

	/**
	 * Constructor, establishes the bullet's properties.
	 * 
	 * @param positionX
	 *            Initial position of the bullet in the X axis.
	 * @param positionY
	 *            Initial position of the bullet in the Y axis.
	 * @param speed
	 *            Speed of the bullet, positive or negative depending on
	 *            direction - positive is down.
	 */
	public Bullet(final int positionX, final int positionY, final int speed) {
		super(positionX, positionY, 3 * 2, 5 * 2, Color.WHITE);

		this.speed = speed;
		setSprite();
	}
	/**
	*  set color of entity
	*/
	public final void setColor(Color new_color) {
		this.color = new_color;
	}
	/**
	 * Sets correct sprite for the bullet, based on speed.
	 */
	
	public final void setSprite() {
		if (speed < 0)
			this.spriteType = SpriteType.Bullet;
		else {
			this.spriteType = SpriteType.EnemyBullet;
			this.setColor(Color.red);
		
		}
	}

	/**
	 * Updates the bullet's position.
	 */
	public final void update() {
		this.positionY += this.speed;
	}

	/**
	 * Setter of the speed of the bullet.
	 * 
	 * @param speed
	 *            New speed of the bullet.
	 */
	public final void setSpeed(final int speed) {
		this.speed = speed;
	}

	/**
	 * Getter for the speed of the bullet.
	 * 
	 * @return Speed of the bullet.
	 */
	public final int getSpeed() {
		return this.speed;
	}
}
