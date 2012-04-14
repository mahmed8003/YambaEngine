package com.engine.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Platform extends BasicEntity {
	
	Sprite sprite;
	Body body;
	
	public Platform(String name, float x, float y, TextureRegion region) {
		sprite = new Sprite(name, x, y, region);
		
	}

}
