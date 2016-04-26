package com.chrisreading.gravitatem.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.entities.Coin;
import com.chrisreading.gravitatem.entities.Player;
import com.chrisreading.gravitatem.entities.Portal;
import com.chrisreading.gravitatem.entities.Portal.PortalState;
import com.chrisreading.gravitatem.handlers.GameStateManager;
import com.chrisreading.gravitatem.handlers.Hud;
import com.chrisreading.gravitatem.handlers.Map;
import com.chrisreading.gravitatem.handlers.ParallaxBackground;
import com.chrisreading.gravitatem.handlers.ParallaxLayer;
import com.chrisreading.gravitatem.handlers.Timer;
import com.chrisreading.gravitatem.handlers.Vars;
import com.chrisreading.gravitatem.handlers.camera.BoundedCamera;
import com.chrisreading.gravitatem.handlers.sound.DialoguePlayer;

public class TutorialState extends LevelState {
	
	private DialoguePlayer dplayer;
	private Hud hud;

	public TutorialState(GameStateManager gsm) {
		super(gsm);
		
		// create map and load layers
		map = new Map(world, "levels/tutorial.tmx");
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
				new ParallaxLayer(GravitatemGame.content.getTexture("wallPanel"), new Vector2(0.5f, 0.3f), new Vector2(0, 0)),
		}, GravitatemGame.V_WIDTH, GravitatemGame.V_HEIGHT, new Vector2(0, 0), cam);
		
		player = new Player(world, map.getPlayerSpawn());
		portal = new Portal(world, map.getPortalSpawn(), shake);	
		
		hud = new Hud(player, map);
		
		dplayer = new DialoguePlayer(new Vector2(GravitatemGame.V_WIDTH / 3, 14), "font/visitor1.ttf");
		dplayer.add(GravitatemGame.content.getMusic("d1"), "Welcome Test Subject 9876");
		dplayer.add(GravitatemGame.content.getMusic("d2"), "You are apart of the Gravitatem Project,");
		dplayer.add(GravitatemGame.content.getMusic("d3"), "In which you will control gravity.");
		dplayer.add(GravitatemGame.content.getMusic("d4"), "Good luck.");
		
		timer = new Timer(1.1f) {
			public void run() {
				GravitatemGame.content.getSound("lightsOn").play();
				createLights();
				dplayer.start();
				shake.shake(0.3f);
			}
		};
		
		createCoins();
	}
	
	public void handleInput() {
		super.handleInput();
	}
	
	public void update(float delta) {
		super.update(delta);
		
		handleInput();
		
		// update box2d world
		world.step(Gdx.graphics.getDeltaTime(), 4, 2);
		player.update(delta);
		portal.update(delta);
		
		for(Coin coin : coins)
			coin.update(delta);
		
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
		super.render();
		
		timer.render();
		
		// draw background
		pbg.render(Gdx.graphics.getDeltaTime());
		// draw map
		map.render(cam);
		
		sb.begin();
		player.render(sb); // draw player
		portal.render(sb);
		for(Coin coin : coins)
			coin.render(sb);
		sb.end();
		
		// draw lighting
		if(!Vars.DEBUG) {
			ray.setCombinedMatrix(cam);
			ray.updateAndRender();	
		}
		
		if(Vars.DEBUG)
			b2dr.render(world, b2dCam.combined);
		
		sb.setProjectionMatrix(hudCam.combined);
		dplayer.render(sb);
		hud.render(sb);
	}
	
}
