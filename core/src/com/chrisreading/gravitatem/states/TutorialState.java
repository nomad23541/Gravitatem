package com.chrisreading.gravitatem.states;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.entities.Player;
import com.chrisreading.gravitatem.handlers.GameStateManager;
import com.chrisreading.gravitatem.handlers.MapLoader;
import com.chrisreading.gravitatem.handlers.Vars;

public class TutorialState extends LevelState {
	
	private Player player;

	public TutorialState(GameStateManager gsm) {
		super(gsm);
		
		// create map and load layers
		map = new MapLoader(world, "assets/levels/tutorial.tmx");
		map.loadLayer((TiledMapTileLayer) map.getTiledMap().getLayers().get("Foreground"), Vars.BIT_GROUND, "ground");
	}
	
	public void handleInput() {
		super.handleInput();
	}
	
	public void update(float delta) {
		super.update(delta);
	}
	
	public void render() {
		super.render();
		
		// set camera to follow player
		cam.setPosition(player.getBody().getPosition().x * Vars.PPM + GravitatemGame.V_WIDTH / Vars.PPM, GravitatemGame.V_HEIGHT / 2);
		cam.update();
		
		sb.begin();
		player.render(sb); // draw player
		sb.end();
		
		if(Vars.DEBUG) {
			b2dCam.setPosition(player.getBody().getPosition().x + GravitatemGame.V_WIDTH / Vars.PPM / Vars.PPM, GravitatemGame.V_HEIGHT / 2 / Vars.PPM);
			b2dCam.update();
		}
	}
	
}
