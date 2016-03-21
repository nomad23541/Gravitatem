package com.chrisreading.gravitatem.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chrisreading.gravitatem.GravitatemGame;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GravitatemGame.V_WIDTH * GravitatemGame.SCALE;
		config.height = GravitatemGame.V_HEIGHT * GravitatemGame.SCALE;
		config.resizable = false;
		new LwjglApplication(new GravitatemGame(), config);
	}
}
