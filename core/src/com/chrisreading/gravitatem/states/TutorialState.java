package com.chrisreading.gravitatem.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.entities.Player;
import com.chrisreading.gravitatem.handlers.BoundedCamera;
import com.chrisreading.gravitatem.handlers.ContactHandler;
import com.chrisreading.gravitatem.handlers.GameStateManager;
import com.chrisreading.gravitatem.handlers.Input;
import com.chrisreading.gravitatem.handlers.MapLoader;
import com.chrisreading.gravitatem.handlers.ParallaxBackground;
import com.chrisreading.gravitatem.handlers.ParallaxLayer;
import com.chrisreading.gravitatem.handlers.Vars;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class TutorialState extends GameState {
	
	protected World world; // box2d world
	protected RayHandler ray; // lighting
	protected Box2DDebugRenderer b2dr;
	protected BoundedCamera b2dCam;
	protected ContactHandler cm;
	
	protected MapLoader map; // map to load from
	
	private Player player;
	
	private ParallaxBackground pbg;

	public TutorialState(GameStateManager gsm) {
		super(gsm);
		
		// create box2d world to handle physics
		world = new World(new Vector2(0, -Vars.GRAVITY), false);
		cm = new ContactHandler();
		world.setContactListener(cm);
		b2dr = new Box2DDebugRenderer();
		
		// create lighting
		ray = new RayHandler(world);
		ray.setShadows(true);
		RayHandler.useDiffuseLight(true);
		ray.setAmbientLight(0.2f, 0.2f, 0.2f, 0.1f);
		
		// create map and load layers
		map = new MapLoader(world, "levels/tutorial.tmx");
		map.loadLayer((TiledMapTileLayer) map.getTiledMap().getLayers().get("Foreground"), Vars.BIT_GROUND, "ground");
		
		// set camera
		cam.setBounds(0, map.getMapWidth() * map.getTileSize(), 0, map.getMapHeight() * map.getTileSize());
		cam.zoom = 1.0f;
		
		// create debug camera
		b2dCam = new BoundedCamera();
		b2dCam.setToOrtho(false, GravitatemGame.V_WIDTH / Vars.PPM, GravitatemGame.V_HEIGHT / Vars.PPM);
		b2dCam.setBounds(0, (map.getMapWidth() * map.getTileSize()) / Vars.PPM, 0, (map.getMapHeight() * map.getTileSize()) / Vars.PPM);
		b2dCam.zoom = cam.zoom;
		
		pbg = new ParallaxBackground(new ParallaxLayer[] {
				new ParallaxLayer(GravitatemGame.content.getTexture("wallPanel"), new Vector2(0.5f, 0.3f), new Vector2(0, 0))
		}, GravitatemGame.V_WIDTH, GravitatemGame.V_HEIGHT, new Vector2(0, 0), cam);
		
		player = new Player(world, map.getPlayerSpawn());
		
		createLights();
	}
	
	private void createLights() {
		for(Vector2 point : map.getLights((TiledMapTileLayer) map.getTiledMap().getLayers().get("Light"))) {
			new PointLight(ray, 100, Color.WHITE, 100, point.x, point.y);
		}
	}
	
	public void handleInput() {
		// debug mode
		if(Input.isPressed(Input.DEBUG))
			Vars.DEBUG = !Vars.DEBUG;
		
		Input.update();
	}
	
	public void update(float delta) {
		handleInput();
		
		// update box2d world
		world.step(Gdx.graphics.getDeltaTime(), 4, 2);
		player.update(delta);
		
		// set camera to follow player
		cam.setPosition(player.getBody().getPosition().x * Vars.PPM + GravitatemGame.V_WIDTH / Vars.PPM, GravitatemGame.V_HEIGHT / 2);
		cam.update();
		
		// update debug camera
		if(Vars.DEBUG) {
			b2dCam.setPosition(player.getBody().getPosition().x + GravitatemGame.V_WIDTH / Vars.PPM / Vars.PPM, GravitatemGame.V_HEIGHT / 2 / Vars.PPM);
			b2dCam.update();
		}
	}
	
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clear screen
		
		sb.setProjectionMatrix(cam.combined);
		
		// draw background
		pbg.render(Gdx.graphics.getDeltaTime());
		
		// draw map
		map.render(cam);
		
		sb.begin();
		player.render(sb); // draw player
		sb.end();
		
		// draw lighting
		ray.setCombinedMatrix(cam);
		ray.updateAndRender();
		
		if(Vars.DEBUG)
			b2dr.render(world, b2dCam.combined);
	}

	public void dispose() {
		ray.dispose();
		map.dispose();
		world.dispose();
	}
	
}
