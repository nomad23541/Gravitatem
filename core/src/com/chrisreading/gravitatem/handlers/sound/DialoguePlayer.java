package com.chrisreading.gravitatem.handlers.sound;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;

/**
 * Will play multiple sound files in order,
 * uses the Music class for more control over 
 * the file. Also feeds subtitles over the screen
 * as the dialogue plays on.
 */
public class DialoguePlayer {
	
	private Queue<Music> dialogue;
	private Queue<String> subtitles;
	
	private BitmapFont font;
	private Vector2 position;
	
	public DialoguePlayer(Vector2 position, String file) {
		this.position = position;
		
		dialogue = new LinkedList<Music>();
		subtitles = new LinkedList<String>();
		
		// create font with settings
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 10;
		parameter.color = Color.WHITE;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 1;
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(file));
		font = generator.generateFont(parameter);
		generator.dispose();
	}
	
	public void add(Music sound, String subtitle) {
		dialogue.add(sound);
		subtitles.add(subtitle);
	}
	
	public void start() {
		dialogue.poll().play();
	}
	
	public void render(SpriteBatch sb) {
		if(!dialogue.isEmpty()) {
			dialogue.peek().setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(Music music) {
					if(!subtitles.isEmpty())
						subtitles.poll();
					
					if(!dialogue.isEmpty()) {
						dialogue.poll().play();
					} else {
						dispose(); // remove resources when complete
					}
				}
			});	
		}
		
		sb.begin();
		if(!subtitles.isEmpty())
			font.draw(sb, subtitles.peek(), position.x, position.y);
		sb.end();
	}
	
	public void dispose() {
		for(Music sound : dialogue) {
			sound.dispose();
		}
	}

}
