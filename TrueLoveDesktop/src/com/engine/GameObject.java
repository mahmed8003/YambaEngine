package com.engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameObject {
	
	public abstract void init();
	public abstract void dispose();
	public abstract void update();


	public void beginContact(Body bodyA, Body bodyB, Vector2 normal,
			Vector2[] points) {
	}


	public void endContact(Body bodyA, Body bodyB, Vector2 normal,
			Vector2[] points) {
	}
	
	

}
