package com.chrisreading.gravitatem.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.chrisreading.gravitatem.GravitatemGame;
import com.chrisreading.gravitatem.entities.Player;

/**
 * Displays player score and map info onto the screen
 */
public class Hud {
	
	private Player player;
	private Map map;
	private BitmapFont font;
	
	public Hud(Player player, Map map) {
		this.player = player;
		this.map = map;
		
		// create font with settings
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 10;
		parameter.color = Color.WHITE;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 1;
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/visitor1.ttf"));
		font = generator.generateFont(parameter);
		generator.dispose();
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		font.draw(sb, "Score: " + player.getScore(), GravitatemGame.V_WIDTH / 2.6f, GravitatemGame.V_HEIGHT - 2);	// score
		font.draw(sb, "Enemies Left: " + map.getCoinCount(), GravitatemGame.V_WIDTH / 2.0f, GravitatemGame.V_HEIGHT - 2); // coins
		font.draw(sb, "Level: " + map.getLevel(), GravitatemGame.V_WIDTH / 1.5f, GravitatemGame.V_HEIGHT - 2); // level
		
		// DEBUG
		if(Vars.DEBUG) {
			font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 30);
			font.draw(sb, "PLAYERSTATE: " + player.getState(), 20, 20);	
		}
		sb.end();
	}

}
