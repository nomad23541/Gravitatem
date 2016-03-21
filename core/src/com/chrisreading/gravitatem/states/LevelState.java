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
	


	public LevelState(GameStateManager gsm) {
		super(gsm);
		

	}
	
	/**
	 * Handle peripheral input
	 */
	public void handleInput() {

		
		Input.update();
	}
	
	public void update(float delta) {
		handleInput();


	}
	
	public void render() {

		

	}

	/**
	 * Manage memory
	 */
	public void dispose() {

	}
}
