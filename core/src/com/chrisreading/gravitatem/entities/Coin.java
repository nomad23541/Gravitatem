package com.chrisreading.gravitatem.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.entities.Portal.PortalState;
import com.chrisreading.gravitatem.handlers.Vars;
import com.chrisreading.gravitatem.handlers.camera.CameraShake;

public class Coin extends Entity {
	
	private TextureRegion[] sprites;

	public Coin(World world, Vector2 position) {
		super(world, position);
		
		// opened animation
		sprites = new TextureRegion[1];
		for(int i = 0; i < sprites.length; i++) {
			sprites[i] = new TextureRegion(GravitatemGame.content.getTexture("coin"), i * 8, 0, 8, 8);
		}
		
		setFrames(sprites, 0);
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.position.set(position);
		bdef.type = BodyType.StaticBody;
		body = world.createBody(bdef);

		shape.setAsBox((width / 2.4f) / Vars.PPM, (height / 2.4f) / Vars.PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = Vars.BIT_COIN;
		fdef.filter.maskBits = Vars.BIT_PLAYER;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("coin");
	}
	
	public void update(float delta) {
		super.update(delta);
	}

}
