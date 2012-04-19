package com.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.engine.PhysicsFactory;

public class Platform extends BasicEntity {

	Sprite sprite;
	Body body;

	public Platform(String name, Sprite sprite, Body body) {
		this.sprite = sprite;
		this.body = body;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		sprite.draw(batch, parentAlpha);
	}
	
	@Override
	public void act(float delta) {
		PhysicsFactory.updateEntity(sprite, body, true, true);
		super.act(delta);
	}
	
	@Override
	public void remove() {
		sprite.remove();
		body.getWorld().destroyBody(body);
		super.remove();
	}
}
