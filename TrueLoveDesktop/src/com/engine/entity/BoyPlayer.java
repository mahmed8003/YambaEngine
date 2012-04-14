package com.engine.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.engine.PhysicsUtils;
import com.engine.Settings;
import com.engine.Utility;

public class BoyPlayer extends AnimatedSprite implements AnimationEventListener {

	private Body body;
	private final Vector2 impulse;
	private float speed;
	private float jumpImpulse;
	private boolean grounded = true;
	private boolean moving;
	private boolean jump;

	public BoyPlayer(World world, TextureRegion tRegion, int cols, int rows) {
		super(tRegion, cols, rows);
		this.registerListener(this);
		impulse = new Vector2();
		speed = 2.0f;
		jumpImpulse = 0.5f;
		
		//
		this.addNewAnimation("walk_right", new int[]{8, 9, 10, 11});
		this.addNewAnimation("walk_left", new int[]{4, 5, 6, 7});
		this.addNewAnimation("jump", new int[]{12, 13, 14, 15});
		this.stopAnimation();
		this.setFrameTime(0.9f);
		this.setPosition(200, 500);
		this.registerListener(this);
		
		FixtureDef def = PhysicsUtils.createFixtureDef(10, 0.5f, 0.5f);
		body = PhysicsUtils.createBoxBody(world, this, BodyType.DynamicBody, def, Settings.PIXEL_TO_METER_RATIO);
	}

	@Override
	public void act(float delta) {
		
		if(moving) {
			body.setLinearVelocity(impulse);
		}
		
		
		//setX(body.getPosition().x - width / 2);
		//setY(body.getPosition().y - height / 2);
		//rotation = body.getAngle() * MathUtils.radiansToDegrees;
		Utility.updateSprite(this, body, true, true);
		super.act(delta);
	}

	@Override
	public void remove() {
		if (body != null) {
			body.getWorld().destroyBody(body);
			body = null;
		}
		super.remove();
	}
	
	public void setPlayerPosition(float x, float y) {
		body.setTransform(x, y, body.getAngle());
	}
	
	public void setMovingSpeed(float speed) {
		this.speed = speed;
	}
	
	public void moveToRight() {
		moving = true;
		impulse.x = speed;
	}
	
	public void moveToLeft() {
		moving = true;
		impulse.x = -speed;
	}
	
	public void stopToMove() {
		moving = false;
		body.setLinearVelocity(0, 0);
	}
	
	public void jump() {
		if(grounded) {
			grounded = false;
			impulse.y = jumpImpulse;
			body.applyLinearImpulse(impulse, body.getWorldCenter());
		}
	}
	
	@Override
	public void onAnimationEnded(AnimatedSprite animatedSprite) {
		// TODO Auto-generated method stub
		
	}
	
	public void beginContact(Body bodyA, Body bodyB, Vector2 normal,
			Vector2[] points) {
	}


	public void endContact(Body bodyA, Body bodyB, Vector2 normal,
			Vector2[] points) {
	}

	

}
