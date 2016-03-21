package com.chrisreading.gravitatem.handlers;

import java.util.Stack;

import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.states.GameState;
import com.chrisreading.gravitatem.states.MenuState;
import com.chrisreading.gravitatem.states.TutorialState;

/**
 * Manages the changing of a game state
 */
public class GameStateManager {
	
	private GravitatemGame game;
	
	private Stack<GameState> gameStates;
	
	public static final int MENU = 0;
	public static final int TUTORIAL = 1;
	public static final int LEVEL1 = 2;
	public static final int LEVEL2 = 3;
	public static final int LEVEL3 = 4;
	public static final int LEVEL4 = 5;
	public static final int LEVEL5 = 6;
	public static final int LEVEL6 = 7;
	public static final int END = 8;
	
	public GravitatemGame game() { return game; }
	
	public GameStateManager(GravitatemGame game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(TUTORIAL);
	}
	
	public void update(float delta) {
		gameStates.peek().update(delta);
	}
	
	public void render() {
		gameStates.peek().render();
	}
	
	private GameState getState(int state) {
		if(state == MENU) return new MenuState(this);
		if(state == TUTORIAL) return new TutorialState(this);
		return null;
	}
	
	public void setState(int state) {
		popState();
		pushState(state);
	}
	
	public void pushState(int state) {
		gameStates.push(getState(state));
	}
	
	public void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}

}
