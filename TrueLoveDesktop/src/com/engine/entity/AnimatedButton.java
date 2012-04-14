package com.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedButton extends BasicEntity {
	
	private TextureRegion[] regions;
	private TextureRegion region;
	
	public AnimatedButton(String name, TextureRegion region) {
		super(name);
		int cols = 2, rows = 1;
		int tileWidth = region.getRegionWidth() / 2;
		int tileHeight = region.getRegionHeight() / 1;
		
		regions = new TextureRegion[cols * rows];
		
		width = tileWidth;
		height = tileHeight;
		setOrigin(0, 0);
		
		TextureRegion[][] textureRegions = region.split(tileWidth, tileHeight);
		regions[0] = textureRegions[0][0];
		regions[1] = textureRegions[0][1];
		this.region = regions[0];
	}
	
	
	@Override
	public boolean touchDown(float x, float y, int pointer) {
		boolean result = x > 0 && y > 0 && x < width && y < height;
		if(result) {
			region = regions[1];
		}
		return result;
	}

	@Override
	public void touchUp(float x, float y, int pointer) {
		region = regions[0];
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
	}

}
