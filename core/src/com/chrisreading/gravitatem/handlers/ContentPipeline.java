package com.chrisreading.gravitatem.handlers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Will load textures, sfx, and audio
 * @author 9505180
 *
 */
public class ContentPipeline {
	
	private HashMap<String, Texture> textures;
	
	public ContentPipeline() {
		textures = new HashMap<String, Texture>();
	}
	
	public void loadTexture(String path) {
		int slashIndex = path.lastIndexOf('/');
		String key;

		if (slashIndex == -1) {
			key = path.substring(0, path.lastIndexOf('.'));
		} else {
			key = path.substring(slashIndex + 1, path.lastIndexOf('.'));
		}
		loadTexture(path, key);
	}

	public void loadTexture(String path, String key) {
		Texture tex = new Texture(Gdx.files.internal(path));
		textures.put(key, tex);
	}

	public Texture getTexture(String key) {
		return textures.get(key);
	}

	public void removeTexture(String key) {
		Texture tex = textures.get(key);
		if (tex != null) {
			textures.remove(key);
			tex.dispose();
		}
	}

}
