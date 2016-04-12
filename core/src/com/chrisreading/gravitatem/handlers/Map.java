package com.chrisreading.gravitatem.handlers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Loads and renders a tiled map, creates
 * box2d bodies from cells.
 */
public class Map {
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer renderer;
	private int mapWidth, mapHeight, tileSize;
	
	private World world;
	
	public TiledMap getTiledMap() { return tileMap; }
	public int getMapWidth () { return mapWidth; }
	public int getMapHeight() { return mapHeight; }
	public int getTileSize() { return tileSize; }
	
	// coins
	public int coins = 0;
	
	public Map(World world, String path) {
		this.world = world;
		
		// set rendering settings to avoid "bleeding"
		TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
		params.textureMagFilter = Texture.TextureFilter.Nearest;
		params.textureMinFilter = Texture.TextureFilter.Linear;	
		
		tileMap = new TmxMapLoader().load(path, params);
		
		mapWidth = (Integer) tileMap.getProperties().get("width");
		mapHeight = (Integer) tileMap.getProperties().get("height");
		tileSize = (Integer) tileMap.getProperties().get("tilewidth");
		
		renderer = new OrthogonalTiledMapRenderer(tileMap);
	}
	
	public int getCoinCount() {
		return 0; // only return 0 for testing
	}
	
	/**
	 * Get which level the player is on
	 */
	public int getLevel() {
		return 0;
	}
	
	public Vector2 getPlayerSpawn() {
		for(MapObject obj : tileMap.getLayers().get("Spawn Points").getObjects()) {
			if(obj.getName().equals("PlayerSpawn")) {
				return new Vector2(((RectangleMapObject) obj).getRectangle().x / Vars.PPM, 
						((RectangleMapObject) obj).getRectangle().y / Vars.PPM);
			}
		}
		
		return Vector2.Zero;
	}
	
	public void loadObjects(String layer, short bits, String data) {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		MapObjects objs = tileMap.getLayers().get(layer).getObjects();
		
		for(MapObject obj : objs) {
			if(obj instanceof RectangleMapObject) {
				Rectangle rec = ((RectangleMapObject) obj).getRectangle();
				Vector2 size = new Vector2((rec.x + rec.width * 0.5f) / Vars.PPM, (rec.y + rec.height * 0.5f) / Vars.PPM);
				// now create a body and fixture from cell
				bdef.type = BodyType.StaticBody;
				
				PolygonShape shape = new PolygonShape();
				shape.setAsBox(rec.width * 0.5f / Vars.PPM, rec.height * 0.5f / Vars.PPM, size, 0.0f);
				fdef.friction = 0;
				fdef.shape = shape;
				fdef.isSensor = true;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = Vars.BIT_PLAYER;
				world.createBody(bdef).createFixture(fdef).setUserData(data);		
			}
		}
	}
	
	public List<Vector2> getLights(TiledMapTileLayer layer) {
		List<Vector2> points = new ArrayList<Vector2>();
		
		for(int row = 0; row < mapHeight; row++) {
			for(int col = 0; col < mapWidth; col++) {
				Cell cell = layer.getCell(col, row);
				
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				points.add(new Vector2((col + 0.5f) * tileSize, (row + 0.5f) * tileSize));
			}
		}
		
		return points;
	}
	
	public void loadLayer(TiledMapTileLayer layer, short bits, String data) {	
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		for(int row = 0; row < mapHeight; row++) {
			for(int col = 0; col < mapWidth; col++) {
				Cell cell = layer.getCell(col, row); // get cell at coord
				
				// if cell exists
				if(cell == null) continue;
				if(cell.getTile() == null) continue;

				// now create a body and fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
					(col + 0.5f) * tileSize / Vars.PPM,
					(row + 0.5f) * tileSize / Vars.PPM
				);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = null;
				if(bits == Vars.BIT_LASER) {
					v = new Vector2[5];
					v[0] = new Vector2(-tileSize / 2 / Vars.PPM, -tileSize / 2 / Vars.PPM);
					v[1] = new Vector2(-tileSize / 2 / Vars.PPM, tileSize / 4f / Vars.PPM);
					v[2] = new Vector2(tileSize / 2 / Vars.PPM, tileSize / 4f / Vars.PPM);
					v[3] = new Vector2(tileSize / 2 / Vars.PPM, -tileSize / 4f / Vars.PPM);
					v[4] = new Vector2(-tileSize / 2 / Vars.PPM, -tileSize / 4f / Vars.PPM);
				} else {
					v = new Vector2[5];
					v[0] = new Vector2(-tileSize / 2 / Vars.PPM, -tileSize / 2 / Vars.PPM);
					v[1] = new Vector2(-tileSize / 2 / Vars.PPM, tileSize / 2 / Vars.PPM);
					v[2] = new Vector2(tileSize / 2 / Vars.PPM, tileSize / 2 / Vars.PPM);
					v[3] = new Vector2(tileSize / 2 / Vars.PPM, -tileSize / 2 / Vars.PPM);
					v[4] = new Vector2(-tileSize / 2 / Vars.PPM, -tileSize / 2 / Vars.PPM);
				}
				cs.createChain(v);
				fdef.shape = cs;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = Vars.BIT_PLAYER | Vars.BIT_DROID;
				fdef.isSensor = false;
				
				if(bits == Vars.BIT_LASER) 
					fdef.isSensor = true;
				
				world.createBody(bdef).createFixture(fdef).setUserData(data);
			}
		}
	}
	
	public void render(BoundedCamera cam) {
		renderer.setView(cam);
		renderer.render();
	}
	
	public void dispose() {
		tileMap.dispose();
		renderer.dispose();
	}

}
