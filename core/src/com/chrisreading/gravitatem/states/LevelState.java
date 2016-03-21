package com.chrisreading.gravitatem.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.entities.Player;
import com.chrisreading.gravitatem.entities.Player.PlayerState;
import com.chrisreading.gravitatem.handlers.BoundedCamera;
import com.chrisreading.gravitatem.handlers.ContactHandler;
import com.chrisreading.gravitatem.handlers.DialogueHandler;
import com.chrisreading.gravitatem.handlers.GameStateManager;
import com.chrisreading.gravitatem.handlers.Input;
import com.chrisreading.gravitatem.handlers.MapLoader;
import com.chrisreading.gravitatem.handlers.Vars;

import box2dLight.RayHandler;

/**
 * Use this class for all playable levels.
 */
public class LevelState extends GameState {
	
	protected World world; // box2d world
	protected RayHandler ray; // lighting
	protected Box2DDebugRenderer b2dr;
	protected BoundedCamera b2dCam;
	protected ContactHandler cm;
	
	protected MapLoader map; // map to load from

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
		ray.setAmbientLight(0.2f, 0.2f, 0.2f, 0.1f);

		// set camera
		cam.setBounds(0, map.getMapWidth() * map.getTileSize(), 0, map.getMapHeight() * map.getTileSize());
		cam.zoom = 1.0f;
		
		// create debug camera
		b2dCam = new BoundedCamera();
		b2dCam.setToOrtho(false, GravitatemGame.V_WIDTH / Vars.PPM, GravitatemGame.V_HEIGHT / Vars.PPM);
		b2dCam.setBounds(0, (map.getMapWidth() * map.getTileSize()) / Vars.PPM, 0, (map.getMapHeight() * map.getTileSize()) / Vars.PPM);
		b2dCam.zoom = cam.zoom;
	}
	
	/**
	 * Handle peripheral input
	 */
	public void handleInput() {
		// debug mode
		if(Input.isPressed(Input.DEBUG))
			Vars.DEBUG = !Vars.DEBUG;
		
		Input.update();
	}
	
	public void update(float delta) {
		handleInput();

		// update box2d world
		world.step(GravitatemGame.STEP, 4, 2);
	}
	
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clear screen
		
		sb.setProjectionMatrix(cam.combined);
		
		// draw map
		map.render(cam);
		
		// draw lighting
		ray.setCombinedMatrix(cam);
		ray.updateAndRender();
		
		// draw debug boxes
		if(Vars.DEBUG) {
			b2dr.render(world, b2dCam.combined);
		}
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
