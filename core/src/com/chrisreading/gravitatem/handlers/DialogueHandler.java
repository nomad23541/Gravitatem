package com.chrisreading.gravitatem.handlers;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;

/**
 * Handles printing text to the screen...
 */
public class DialogueHandler {
	
	private Queue<String> dialogue;
	private BitmapFont font;
	private float timer = 0f;
	private float delay = 3; // seconds between messages
	
	private Vector2 position; // location
	
	public DialogueHandler(Vector2 position, String file) {
		this.position = position;
		
		dialogue = new LinkedList<String>();
		
		// create font with settings
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 16;
		parameter.color = Color.YELLOW;
		parameter.shadowOffsetX = -1;
		parameter.shadowOffsetY = 1;
		parameter.shadowColor = Color.BLACK;
		
		font = generator.generateFont(parameter);
		generator.dispose();
	}
	
	public void addDialogue(String text) {
		dialogue.add(text);
	}
	
	public void render(SpriteBatch sb) {
		// handle switching text
		timer += Gdx.graphics.getDeltaTime();
		if(timer > delay && !dialogue.isEmpty()) {
			timer -= delay;
			dialogue.poll();
		}
		
		// draw bitmap font
		sb.begin();
		if(!dialogue.isEmpty())
			font.draw(sb, dialogue.peek(), position.x, position.y);
		sb.end();
	}
	
	public void dispose() {
		font.dispose();
	}

}
