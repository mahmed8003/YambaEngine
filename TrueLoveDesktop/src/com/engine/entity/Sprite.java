package com.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Sprite extends BasicEntity {
	
	TextureRegion region;
	
	public Sprite(String name, TextureRegion region) {
		this(name, 0, 0, region);
	}
	
	public Sprite(String name, float x, float y, TextureRegion region) {
		super(name);
		setPosition(x, y);
		setWidth(region.getRegionWidth());
		setHeight(region.getRegionHeight());
		setScale(1, 1);
		setOrigin(width * 0.5f, height * 0.5f);
		this.region = region;
	}
	

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (visible) {
			batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
		}
	}

	@Override
	public void remove() {
		region = null;
		super.remove();
	}
	
	public TextureRegion getRegion() {
		return region;
	}
}
