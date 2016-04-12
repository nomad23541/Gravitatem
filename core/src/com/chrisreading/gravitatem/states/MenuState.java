package com.chrisreading.gravitatem.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.chrisreading.gravitatem.handlers.GameStateManager;
import com.chrisreading.gravitatem.handlers.Input;
import com.chrisreading.gravitatem.handlers.Map;

import box2dLight.RayHandler;

public class MenuState extends GameState {
	
	private Map map;
	private RayHandler handler; // lighting
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
		
		// lighting
		handler = new RayHandler(null);
		handler.setCombinedMatrix(cam);
		RayHandler.useDiffuseLight(true);
		
		map = new Map(null, "res/levels/menu.tmx");
		
		// set camera
		//cam.setBounds(0, map.getMapWidth() * map.getTileSize(), 0, map.getMapHeight() * map.getTileSize());			
	}

	public void handleInput() {
		if(Input.isPressed(Input.ENTER))
			gsm.setState(GameStateManager.TUTORIAL);
	}

	public void update(float delta) {
		handleInput();
	}

	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(201/255f, 201/255f, 201/255f, 1f); // clear screen
		
		sb.setProjectionMatrix(cam.combined);
		
		// draw map
		map.render(cam);
		
		// render lights
		handler.updateAndRender();
	
		// draw textures
		sb.begin();
		sb.end();
	}

	public void dispose() {
		
	}

}
