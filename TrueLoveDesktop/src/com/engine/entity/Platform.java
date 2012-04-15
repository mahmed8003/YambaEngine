package com.engine.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.engine.PhysicsFactory;

public class Platform extends Sprite {
	
	Body body;

	public Platform(String name, float x, float y, TextureRegion region) {
		super(name, x, y, region);
	}
	
	
	@Override
	public void act(float delta) {
		PhysicsFactory.updateEntity(this, body, true, true);
		super.act(delta);
	}

}
