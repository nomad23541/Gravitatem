package com.chrisreading.gravitatem.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

/**
 * Handles special contacts the player makes
 */
public class ContactHandler implements ContactListener {
	
	private static int numFootContacts;
	private boolean portalCollide;	
	private boolean dead = false;
	
	private Array<Body> crystalsToRemove;
	private Array<Body> enemiesToRemove;
	
	public boolean isPlayerDead() { return dead; }
	public void setPlayerDead(boolean dead) { this.dead = dead; }
	public static boolean isPlayerOnGround() { return numFootContacts > 0; }
	public boolean isPlayerInPortal() { return portalCollide; }
	
	public Array<Body> getCrystalsToRemove() { return crystalsToRemove; }
	public Array<Body> getEnemiesToRemove() { return enemiesToRemove; }
	
	public ContactHandler() {
		super();
		crystalsToRemove = new Array<Body>();
		enemiesToRemove = new Array<Body>();
	}
	
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		// is player on the ground
		if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
			if(!fb.getUserData().equals("crystal") && !fb.getUserData().equals("portal"))
				numFootContacts++;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
			if(!fa.getUserData().equals("crystal") && !fa.getUserData().equals("portal"))
				numFootContacts++;
		}
		
		// portal collision detection
		if(fa.getUserData() != null && fa.getUserData().equals("portal")) {
			portalCollide = true;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("portal")) {
			portalCollide = true;
		}
		
		// crystal collision detection
		if(fa.getUserData() != null && fa.getUserData().equals("crystal")) {
			crystalsToRemove.add(fa.getBody());
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("crystal")) {
			crystalsToRemove.add(fb.getBody());
		}
		
		// laser collision detection
		if(fa.getUserData() != null && fa.getUserData().equals("laser")) {
			if(fb.getUserData().equals("player"))
				dead = true;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("laser")) {
			if(fa.getUserData().equals("player"))
				dead = true;
		}
		
		// laser collision w/ droid
		if(fa.getUserData() != null && fa.getUserData().equals("laser")) {
			if(fb.getUserData().equals("droid"))
				enemiesToRemove.add(fb.getBody());
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("laser")) {
			if(fa.getUserData().equals("droid"))
				enemiesToRemove.add(fa.getBody());
		}
		
		// player w/ droid collision
		if(fa.getUserData() != null && fa.getUserData().equals("droid")) {
			if(fb.getUserData().equals("player"))
				dead = true;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("droid")) {
			if(fa.getUserData().equals("player"))
				dead = true;
		}
		
		// player kills droid collision
		if(fa.getUserData() != null && fa.getUserData().equals("droidTop")) {
			if(fb.getUserData().equals("foot"))
				enemiesToRemove.add(fa.getBody());
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("droidTop")) {
			if(fa.getUserData().equals("foot"))
				enemiesToRemove.add(fb.getBody());
		}
	}

	public void endContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		// is player on the ground
		if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
			if(!fb.getUserData().equals("crystal") && !fb.getUserData().equals("portal"))
				numFootContacts--;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
			if(!fa.getUserData().equals("crystal") && !fa.getUserData().equals("portal"))
				numFootContacts--;
		}
		
		// portal collision detection
		if(fa.getUserData() != null && fa.getUserData().equals("portal")) {
			portalCollide = false;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("portal")) {
			portalCollide = false;
		}
	}

	public void postSolve(Contact c, ContactImpulse ci) {
	
	}

	public void preSolve(Contact c, Manifold m) {

	}

}
