package com.chrisreading.gravitatem.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.handlers.Vars;

public class Player extends Entity {
	
	public enum PlayerState { MOVING, JUMPING, IDLE }
	
	private PlayerState state;
	private int crystals; 
	
	public int getCrystals() { return crystals; }
	public void addCrystal() { crystals++; }
	
	public void setState(PlayerState state) { this.state = state; }
	public PlayerState getState() { return state; }
	
	public TextureRegion[] idleSprites, movingSprites, jumpingSprites; // animations
	
	// player values
	public static final float MOVE_SPEED = 0.8f;
	public static float MOVE_JUMP = 2.5f;
	
	public Player(World world, Vector2 position) {
		super(world, position);
		
		state = PlayerState.IDLE; // set initial state
		
		// idle animation
		idleSprites = new TextureRegion[5];
		for(int i = 0; i < idleSprites.length; i++) {
			idleSprites[i] = new TextureRegion(GravitatemGame.content.getTexture("playerIdle"), i * 14, 0, 13, 18);
		}
		
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
		body.createFixture(fdef).setUserData("player");
		
		shape.setAsBox(width / 4 / Vars.PPM, 2 / Vars.PPM, new Vector2(0, -height / 2 / Vars.PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = Vars.BIT_PLAYER;
		fdef.filter.maskBits = Vars.BIT_PORTAL | Vars.BIT_GROUND | Vars.BIT_CRYSTAL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
	}
	
	public void update(float delta) {
		super.update(delta);
		
		// set frames
		if(state == PlayerState.MOVING) {
			setFrames(movingSprites, 1 / 8f);
		} else if(state == PlayerState.JUMPING) {
			setFrames(jumpingSprites, 1 / 1.0f);
		} else if(state == PlayerState.IDLE) {
			setFrames(idleSprites, 1 / 1.2f);
		}
	} 

}
