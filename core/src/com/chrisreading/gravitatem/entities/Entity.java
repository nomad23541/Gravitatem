package com.chrisreading.gravitatem.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.chrisreading.gravitatem.handlers.Vars;

/**
 * Extendable, use for all moving entities
 */
public abstract class Entity {

	protected Body body;
	protected Vector2 position;
	protected Animation animation;
	protected float elapsedTime = 0;
	protected boolean facingRight;
	protected TextureRegion currentFrame;
	protected float width, height;
	
	public void setFacingRight(boolean facingRight) { this.facingRight = facingRight; }
	public boolean isFacingRight() { return facingRight; }
	
	public Body getBody() { return body; }
	
	public Entity(World world, Vector2 position) {
		this.position = position;
	}
	
	public void setFrames(TextureRegion[] reg, float delay) {
		animation = new Animation(delay, reg);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void update(float delta) {
	}
	
	public void render(SpriteBatch sb) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		currentFrame = animation.getKeyFrame(elapsedTime, true);
		
		// flip the texture
		if(!facingRight && !currentFrame.isFlipX()) {
			currentFrame.flip(true, false);
		} else if(facingRight && currentFrame.isFlipX()) {
			currentFrame.flip(true, false);
		}
		
		sb.draw(currentFrame, body.getPosition().x * Vars.PPM - width / 2, body.getPosition().y * Vars.PPM - height / 2, (width / 2), (height / 2), width, height, 1f, 1f, MathUtils.radiansToDegrees * body.getAngle());
	}
	
	public void dispose() {}
	
}
