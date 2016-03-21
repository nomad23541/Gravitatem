package com.chrisreading.gravitatem.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/**
 * Handles what happens when a registered key is pressed
 */
public class InputProcessor extends InputAdapter {
	
	public static float zoomValue; 

	public boolean mouseMoved(int x, int y) {
		Input.x = x;
		Input.y = y;
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		Input.x = x;
		Input.y = y;
		Input.down = true;
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		Input.x = x;
		Input.y = y;
		Input.down = true;
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		Input.x = x;
		Input.y = y;
		Input.down = false;
		return true;
	}
	
	public boolean scrolled(int amount) {
		if(amount == 1) {
			zoomValue += 0.1f;
		} else if(amount == -1) {
			zoomValue -= 0.1f;
		}
		
		return false;
	}
	
	public boolean keyDown(int k) {
		if(k == Keys.A) Input.setKey(Input.LEFT, true);
		if(k == Keys.D) Input.setKey(Input.RIGHT, true);
		if(k == Keys.SPACE) Input.setKey(Input.SPACE, true);
		
		if(k == Keys.ENTER) Input.setKey(Input.ENTER, true);
		if(k == Keys.O) Input.setKey(Input.DEBUG, true);
		if(k == Keys.ESCAPE) Input.setKey(Input.ESCAPE, true);
		
		return true;
	}
	
	public boolean keyUp(int k) {
		if(k == Keys.A) Input.setKey(Input.LEFT, false);
		if(k == Keys.D) Input.setKey(Input.RIGHT, false);
		if(k == Keys.SPACE) Input.setKey(Input.SPACE, false);
		
		if(k == Keys.ENTER) Input.setKey(Input.ENTER, false);
		if(k == Keys.O) Input.setKey(Input.DEBUG, false);
		if(k == Keys.ESCAPE) Input.setKey(Input.ESCAPE, false);
		
		return true;
	}
	
}
