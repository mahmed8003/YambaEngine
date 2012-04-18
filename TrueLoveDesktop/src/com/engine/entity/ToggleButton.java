package com.engine.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ToggleButton extends BasicEntity {
	
	private TextureRegion[] regions;
	private TextureRegion region;
	private boolean down = false;
	private Sound sound = null;
	
	
	public ToggleButton(String name, TextureRegion region) {
		super(name);
		int cols = 2, rows = 1;
		int tileWidth = region.getRegionWidth() / 2;
		int tileHeight = region.getRegionHeight() / 1;
		
		TextureRegion[][] textureRegions = region.split(tileWidth, tileHeight);
		regions = new TextureRegion[cols * rows];
		regions[0] = textureRegions[0][0];
		regions[1] = textureRegions[0][1];
		this.region = regions[0];
		
		setPosition(0, 0);
		setWidth(tileWidth);
		setHeight(tileHeight);
		setScale(1, 1);
		setOrigin(0, height);
	}
	
	public ToggleButton(String name, float x, float y, TextureRegion region_up, TextureRegion region_down) {
		super(name);
		regions = new TextureRegion[2];
		regions[0] = region_up;
		regions[1] = region_down;
		region = regions[0];
		setPosition(x, y);
		setWidth(region_up.getRegionWidth());
		setHeight(region_up.getRegionHeight());
		setScale(1, 1);
		setOrigin(0, height);
	}
	
	
	@Override
	public boolean touchDown(float x, float y, int pointer) {
		boolean result = x > 0 && y > 0 && x < width && y < height;
		if(result) {
			// if user set some sound for the button then play it
			if(sound != null) {
				sound.play();
			}
		}
		return result;
	}

	@Override
	public void touchUp(float x, float y, int pointer) {
		if(isDown()) {
			down = false;
			region = regions[0];
		}else{
			down = true;
			region = regions[1];
		}
		
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
	}
	
	public void setOnClickSound(Sound sound) {
		this.sound = sound;
	}

	public boolean isDown() {
		return down;
	}

}
