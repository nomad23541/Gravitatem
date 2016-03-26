package com.chrisreading.gravitatem.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.chrisreading.gravitatem.handlers.BoundedCamera;
import com.chrisreading.gravitatem.handlers.ContactHandler;
import com.chrisreading.gravitatem.handlers.GameStateManager;
import com.chrisreading.gravitatem.handlers.Input;
import com.chrisreading.gravitatem.handlers.MapLoader;
import com.chrisreading.gravitatem.handlers.ParallaxBackground;
import com.chrisreading.gravitatem.handlers.Timer;
import com.chrisreading.gravitatem.handlers.Vars;

import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * Use this class for all playable levels.
 */
public class LevelState extends GameState {
	
	protected World world;
	protected RayHandler ray;
	protected Box2DDebugRenderer b2dr;
	protected BoundedCamera b2dCam;
	protected ContactHandler cm;
	
	protected Timer timer;
	protected boolean gravitySwitch = false;
	
	protected MapLoader map;
	
	protected ParallaxBackground pbg;

	public LevelState(GameStateManager gsm) {
		super(gsm);
		
		// create box2d world to handle physics
		world = new World(new Vector2(0, -Vars.GRAVITY), false);
		cm = new ContactHandler();
		world.setContactListener(cm);
		b2dr = new Box2DDebugRenderer();
		
		// create lighting
		ray = new RayHandler(world);
		RayHandler.useDiffuseLight(true);
		//ray.setAmbientLight(0.2f, 0.2f, 0.2f, 0.1f);
	}
	
	/**
	 * Find all light points and create
	 */
	protected void createLights() {
		for(Vector2 point : map.getLights((TiledMapTileLayer) map.getTiledMap().getLayers().get("Light"))) {
			new PointLight(ray, 100, Color.WHITE, 110, point.x, point.y);
		}
	}
	
	protected void handleGravity() {
		if(gravitySwitch) {
			world.setGravity(new Vector2(0, Vars.GRAVITY));
		} else {
			world.setGravity(new Vector2(0, -Vars.GRAVITY));
		}
	}
	
	/**
	 * Handle peripheral input
	 */
	public void handleInput() {
		// debug mode
		if(Input.isPressed(Input.DEBUG))
			Vars.DEBUG = !Vars.DEBUG;
		
		// exit program
		if(Input.isPressed(Input.ESCAPE))
			Gdx.app.exit();
		
		// switch gravity
		if(Input.isPressed(Input.ENTER))
			gravitySwitch = !gravitySwitch;
		
		Input.update();
	}
	
	public void update(float delta) { 
		handleGravity();
	}
	
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clear screen
		
		sb.setProjectionMatrix(cam.combined);
	}

	/**
	 * Manage memory
	 */
	public void dispose() {
		ray.dispose();
		map.dispose();
		world.dispose();
	}
}
