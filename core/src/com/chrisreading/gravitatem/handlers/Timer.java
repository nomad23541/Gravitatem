package com.chrisreading.gravitatem.handlers;

import com.badlogic.gdx.Gdx;

/**
 * Will countdown and perform action
 */
public class Timer {
	
	private float timer;
	private float count;
	
	private boolean stop = false;
	
	public Timer(float count) {
		this.count = count;
	}
	
	public void run() { }

	public void render() {
		if(!stop) {
			timer += Gdx.graphics.getDeltaTime();
			float minutes = (float) Math.floor(timer / 60.0f);
			float seconds = timer - minutes * 60.0f;
			
			if(seconds >= count && seconds <= count + 0.1f) {
				run();
				stop = true;
			}	
		}
	}
}
