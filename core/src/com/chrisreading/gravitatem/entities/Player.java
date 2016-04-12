package com.chrisreading.gravitatem.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.handlers.ContactHandler;
import com.chrisreading.gravitatem.handlers.Input;
import com.chrisreading.gravitatem.handlers.Vars;

public class Player extends Entity {
	
	public enum PlayerState { MOVING, JUMPING, IDLE }
	
	private PlayerState state;	
	private int score = 0;
	
	public void setState(PlayerState state) { this.state = state; }
	public PlayerState getState() { return state; }
	
	public void setScore(int score) { this.score = score; }
	public int getScore() { return this.score; }
	
	public TextureRegion[] idleSprites, movingSprites, jumpingSprites; // animations
	
	// player values
	public static final float MOVE_SPEED = 0.9f;
	public static float MOVE_JUMP = 2.8f;
	
	public Player(World world, Vector2 position) {
		super(world, position);
		
		state = PlayerState.IDLE; // set initial state
		
		// idle animation
		idleSprites = new TextureRegion[1];
		for(int i = 0; i < idleSprites.length; i++) {
			idleSprites[i] = new TextureRegion(GravitatemGame.content.getTexture("player"), i * 16, 0, 16, 16);
		}
		
		setFrames(idleSprites, 0);
		width = 16; height = 16;
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.position.set(position);
		bdef.type = BodyType.DynamicBody;
		bdef.fixedRotation = true;
		body = world.createBody(bdef);

		shape.setAsBox((width / 2.4f) / Vars.PPM, (height / 2.4f) / Vars.PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = Vars.BIT_PLAYER;
		fdef.filter.maskBits = Vars.BIT_PORTAL | Vars.BIT_GROUND | Vars.BIT_CRYSTAL;
		fdef.density = 0.0f;
		body.createFixture(fdef).setUserData("player");
		
		body.setLinearDamping(1.0f);
		
		shape.setAsBox(width / 4 / Vars.PPM, 2 / Vars.PPM, new Vector2(0, -height / 2 / Vars.PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = Vars.BIT_PLAYER;
		fdef.filter.maskBits = Vars.BIT_PORTAL | Vars.BIT_GROUND | Vars.BIT_CRYSTAL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
	}
	
	private void handleInput() {
		if(Input.isDown(Input.LEFT) && body.getLinearVelocity().x > -Player.MOVE_SPEED && !Input.isDown(Input.RIGHT)) {
			setFacingRight(false);
			body.applyLinearImpulse(new Vector2(-Player.MOVE_SPEED / 2, 0.0f), body.getPosition(), true);
		} else if(Input.isDown(Input.RIGHT) && body.getLinearVelocity().x < Player.MOVE_SPEED && !Input.isDown(Input.LEFT)) {
			setFacingRight(true);
			body.applyLinearImpulse(new Vector2(Player.MOVE_SPEED / 2, 0.0f), body.getPosition(), true);
		}
		
		if(Input.isDown(Input.SPACE)) {
			if(ContactHandler.isPlayerOnGround()) {
				body.setLinearVelocity(body.getLinearVelocity().x, 0.0f);
				body.applyLinearImpulse(new Vector2(0, Player.MOVE_JUMP), body.getWorldCenter(), true);
			}
		}
	}
	
	public void update(float delta) {
		super.update(delta);
		handleInput();
		
		/*
		// set frames
		if(state == PlayerState.MOVING) {
			setFrames(movingSprites, 1 / 8f);
		} else if(state == PlayerState.JUMPING) {
			setFrames(jumpingSprites, 1 / 1.0f);
		} else if(state == PlayerState.IDLE) {
			setFrames(idleSprites, 1 / 1.2f);
		}
		*/
	} 

}
