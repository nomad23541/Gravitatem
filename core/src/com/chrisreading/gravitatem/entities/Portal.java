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
import com.chrisreading.gravitatem.handlers.camera.CameraShake;

public class Portal extends Entity {
	
	public enum PortalState { CLOSE, OPEN }
	
	private TextureRegion[] closedSprites, openedSprites;
	
	private PortalState state;
	public PortalState getState() { return state; }
	public void setState(PortalState state) { this.state = state; }
	
	private CameraShake shake;

	public Portal(World world, Vector2 position, CameraShake shake) {
		super(world, position);
		this.shake = shake;
		
		state = PortalState.CLOSE;
		
		// closed animation
		closedSprites = new TextureRegion[1];
		for(int i = 0; i < closedSprites.length; i++) {
			closedSprites[i] = new TextureRegion(GravitatemGame.content.getTexture("portalClosed"), i * 20, 0, 20, 20);
		}
		
		// opened animation
		openedSprites = new TextureRegion[1];
		for(int i = 0; i < openedSprites.length; i++) {
			openedSprites[i] = new TextureRegion(GravitatemGame.content.getTexture("portal"), i * 20, 0, 20, 20);
		}
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.position.set(position);
		bdef.type = BodyType.StaticBody;
		body = world.createBody(bdef);

		shape.setAsBox((width / 2.4f) / Vars.PPM, (height / 2.4f) / Vars.PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = Vars.BIT_PORTAL;
		fdef.filter.maskBits = Vars.BIT_PLAYER;
		fdef.density = 0.0f;
		body.createFixture(fdef).setUserData("portal");
		
		shake = new CameraShake();
	}
	
	boolean shaked = false;
	private void handleState() {
		if(state == PortalState.OPEN) {
			// play sound, flicker lights
			if(!shaked) {
				shake.shake(1.0f);
				shaked = true;	
			}
		} else if(state == PortalState.CLOSE) {
			shaked = false;
		}
	}
	
	public void update(float delta) {
		super.update(delta);
		handleState();
		
		// set frames
		if(state == PortalState.CLOSE) {
			setFrames(closedSprites, 1 / 25f);
		} else if(state == PortalState.OPEN) {
			setFrames(openedSprites, 1 / 1.0f);
		}
	}

}
