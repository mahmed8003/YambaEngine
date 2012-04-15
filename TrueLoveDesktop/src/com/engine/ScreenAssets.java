package com.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class ScreenAssets {
	
	private final HashMap<String, TextureRegion> mTextureRegions;
	private final ArrayList<Texture> mTextures;
	private final HashMap<String, Body> mBodies;
	
	public ScreenAssets() {
		mTextureRegions = new HashMap<String, TextureRegion>();
		mTextures = new ArrayList<Texture>();
		mBodies = new HashMap<String, Body>();
	}
	
	public void addTextureRegion(String id, TextureRegion region) {
		mTextureRegions.put(id, region);
	}
	
	public TextureRegion getTextureRegion(String id) {
		return mTextureRegions.get(id);
	}
	
	public HashMap<String, TextureRegion> getTextureRegions() {
		return mTextureRegions;
	}
	
	public void addTexture(Texture texture) {
		mTextures.add(texture);
	}
	
	public void loadTextureRegions(String textureFile, String regionFile) {
		Texture texture = new Texture(Gdx.files.internal(textureFile));
		mTextures.add(texture);
		
		FileHandle handle = Gdx.files.internal(regionFile);
		String regions = handle.readString();
		StringTokenizer tokLine = new StringTokenizer(regions, System.getProperty("line.separator"));
		while (tokLine.hasMoreTokens()) {
			String line = (String) tokLine.nextElement();
			String[] lns = line.split(" = ");
			String regionName = lns[0];
			
			String[] attribs = lns[1].split(" ");
			
			int x = Integer.parseInt(attribs[0]);
			int y = Integer.parseInt(attribs[1]);
			int width = Integer.parseInt(attribs[2]);
			int height = Integer.parseInt(attribs[3]);
			
			Gdx.app.log("Assets : ", regionName + x + y + width+height );
			
			TextureRegion region = new TextureRegion(texture, x, y, width, height);
			mTextureRegions.put(regionName, region);
		}
		
	}
	
	public void addBody(String id, Body body) {
		mBodies.put(id, body);
	}
	
	public Body getBody(String id) {
		return mBodies.get(id);
	}
	
	public void dispose() {
		for(Texture texture : mTextures) {
			texture.dispose();
		}
		mTextureRegions.clear();
		mTextures.clear();
		mBodies.clear();
	}
}
