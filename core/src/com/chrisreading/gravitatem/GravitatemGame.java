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
	public static final int V_WIDTH = 640;
	public static final int V_HEIGHT = 360;
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
		
		content = new ContentPipeline();
		content.loadTexture("texture/background/WallPanel.png", "wallPanel");
		content.loadTexture("texture/sprites/player/Player.png", "player");
		
		// music
		content.loadMusic("audio/music/Lightless Dawn.mp3", "bgMusic");
		
		content.loadSound("audio/sfx/LightsOn.wav", "lightsOn");
		
		// tutorial dialogue
		content.loadMusic("audio/sfx/tutorial/Dialogue1.wav", "d1");
		content.loadMusic("audio/sfx/tutorial/Dialogue2.wav", "d2");
		content.loadMusic("audio/sfx/tutorial/Dialogue3.wav", "d3");
		content.loadMusic("audio/sfx/tutorial/Dialogue4.wav", "d4");
		
		
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
