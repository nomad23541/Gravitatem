package com.chrisreading.gravitatem.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.entities.Coin;
import com.chrisreading.gravitatem.entities.Player;
import com.chrisreading.gravitatem.entities.Portal;
import com.chrisreading.gravitatem.entities.Portal.PortalState;
import com.chrisreading.gravitatem.handlers.ContactHandler;
import com.chrisreading.gravitatem.handlers.GameStateManager;
import com.chrisreading.gravitatem.handlers.Input;
import com.chrisreading.gravitatem.handlers.Map;
import com.chrisreading.gravitatem.handlers.ParallaxBackground;
import com.chrisreading.gravitatem.handlers.Timer;
import com.chrisreading.gravitatem.handlers.Vars;
import com.chrisreading.gravitatem.handlers.camera.BoundedCamera;
import com.chrisreading.gravitatem.handlers.camera.CameraShake;

import box2dLight.ConeLight;
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
	
	protected Map map;
	protected Portal portal;
	protected Player player;
	
	protected CameraShake shake;
	
	protected ParallaxBackground pbg;
	
	protected Array<Coin> coins;

	public LevelState(GameStateManager gsm) {
		super(gsm);
		
		coins = new Array<Coin>();
			
		// create box2d world to handle physics
		world = new World(new Vector2(0, -Vars.GRAVITY), false);
		cm = new ContactHandler();
		world.setContactListener(cm);
		b2dr = new Box2DDebugRenderer();
		
		// create lighting
		ray = new RayHandler(world);
		RayHandler.useDiffuseLight(true);
		ray.setAmbientLight(0.1f, 0.1f, 0.1f, 0.1f);
		
		// start background music
		Music music = GravitatemGame.content.getMusic("bgMusic");
		music.setLooping(true);
		music.play();
		
		shake = new CameraShake();
	}
	
	protected void createCoins() {
		for(Vector2 loc: map.getCoinSpawns()) {
			coins.add(new Coin(world, loc));
		}
	}
	
	/**
	 * Find all light points and create
	 */
	protected void createLights() {
		for(Vector2 point : map.getLights((TiledMapTileLayer) map.getTiledMap().getLayers().get("Top Lights"))) {
			new ConeLight(ray, 60, Color.WHITE, 250, point.x, point.y + 16, 270, 90);
		}
		
		for(Vector2 point : map.getLights((TiledMapTileLayer) map.getTiledMap().getLayers().get("Bottom Lights"))) {
			new ConeLight(ray, 60, Color.WHITE, 250, point.x, point.y - 16, 90, 90);
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
		
		if(coins.size == 0 && portal.getState() == PortalState.CLOSE) {
			portal.setState(PortalState.OPEN);
		}
		
		// check for collected crystals
		Array<Body> bodies = cm.getCoinsToRemove();
		for(int i = 0; i < bodies.size; i++) {
			Body b = bodies.get(i);
			coins.removeValue((Coin) b.getUserData(), true);
			world.destroyBody(b);
			player.setScore(player.getScore() + 3);
		}
		bodies.clear();
	}
	
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clear screen
		shake.update(Gdx.graphics.getDeltaTime(), cam, new Vector2(cam.position.x, cam.position.y));
		
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
