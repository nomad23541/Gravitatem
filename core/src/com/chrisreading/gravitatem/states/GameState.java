package com.chrisreading.gravitatem.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.handlers.GameStateManager;
import com.chrisreading.gravitatem.handlers.camera.BoundedCamera;

/*
 * Extendable, use for all gamestates.
 */
public abstract class GameState {
	
	protected GameStateManager gsm;
	protected GravitatemGame game;
	
	protected SpriteBatch sb;
	protected BoundedCamera cam;
	protected OrthographicCamera hudCam;
	
	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		this.game = gsm.game();
		sb = game.getSpriteBatch();
		cam = game.getCamera();
		hudCam = game.getHUDCamera();
	}
	
	public abstract void handleInput();
	public abstract void update(float delta);
	public abstract void render();
	public abstract void dispose();

}
