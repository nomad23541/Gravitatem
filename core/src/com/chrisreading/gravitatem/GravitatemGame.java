package com.chrisreading.gravitatem;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chrisreading.gravitatem.handlers.BoundedCamera;
import com.chrisreading.gravitatem.handlers.ContentPipeline;
import com.chrisreading.gravitatem.handlers.GameStateManager;
import com.chrisreading.gravitatem.handlers.InputProcessor;

public class GravitatemGame extends ApplicationAdapter {
	
	// global variables
	public static final String TITLE = "Gravitatem";
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 600;
	public static final int SCALE = 2;
	public static final float STEP = 1 / 60f;
	
	private SpriteBatch sb;
	private BoundedCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	
	public static ContentPipeline content;
	
	public SpriteBatch getSpriteBatch() { return sb; }
	public BoundedCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }

	public void create() {
		Gdx.input.setInputProcessor(new InputProcessor());
		
		cam = new BoundedCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		sb = new SpriteBatch();
		gsm = new GameStateManager(this);
	}

	public void render() {
		Gdx.graphics.setTitle(TITLE + " - " + Gdx.graphics.getFramesPerSecond() + " FPS");
		gsm.update(STEP);
		gsm.render();
	}
	
	public void dispose() {}	
	public void pause() {}
	public void resize(int w, int h) {}
	public void resume() {}
	
}
