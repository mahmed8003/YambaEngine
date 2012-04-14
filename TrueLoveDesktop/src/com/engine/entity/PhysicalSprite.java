package com.engine.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class PhysicalSprite extends Sprite {
	
	/**
	 * The Body associated with this Sprite, for Box2D
	 */
	public Body body;
	/**
	 * The BodyDef associated with this Sprite, for Box2D
	 */
	public BodyDef bodyDef;
	protected boolean usePhysics;
	
	public PhysicalSprite(String name, float x, float y, TextureRegion region) {
		super(name, x, y, region);
	}

	public PhysicalSprite(String name, TextureRegion region) {
		super(name, region);
	}

	@Override
	public void act(float delta) {
		
		if(usePhysics) {
			setX(body.getPosition().x - width / 2);
			setY(body.getPosition().y - height / 2);
			rotation = body.getAngle() * MathUtils.radiansToDegrees;
		}
		super.act(delta);
	}
	
	/**
	 * Flags the PhysicalObject to use the Box2D physics engine
	 * If the Body object is not set, this will raise an exception on next redraw
	 * Stops any kinematics from Sprite
	 */
	public void usePhysics() {
		usePhysics = true;
	}
	
	/**
	 * Flags the PhysicalObject to ignore the physics engine for now
	 */
	public void noPhysics() {
		usePhysics = false;
	}
	
	
	@Override
	public void remove() {
		if(body != null) {
			body.getWorld().destroyBody(body);
			body = null;
		}
		super.remove();
	}
}
