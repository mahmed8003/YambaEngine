package com.engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.engine.entity.BasicEntity;

public class PhysicsFactory {

	public static Body createCircleBody(final World phyWorld, final BasicEntity entity, final BodyType bodyType, final FixtureDef fixtureDef, final float pixelToMeterRatio) {
		final BodyDef circleBodyDef = new BodyDef();
		circleBodyDef.type = bodyType;

		final float halfWidth = entity.getWidthScaled() * 0.5f / pixelToMeterRatio;
		final float halfHeight = entity.getHeightScaled() * 0.5f / pixelToMeterRatio;
		
		circleBodyDef.position.x = entity.x / pixelToMeterRatio  + halfWidth;
		circleBodyDef.position.y = entity.y / pixelToMeterRatio  + halfHeight;

		circleBodyDef.angle = entity.rotation * MathUtils.degreesToRadians;

		final Body circleBody = phyWorld.createBody(circleBodyDef);

		final CircleShape circlePoly = new CircleShape();
		fixtureDef.shape = circlePoly;

		final float radius = halfWidth;
		circlePoly.setRadius(radius);

		circleBody.createFixture(fixtureDef);

		circlePoly.dispose();

		return circleBody;
	}
	
	public static Body createBoxBody(final World phyWorld, final BasicEntity entity, final BodyType bodyType, final FixtureDef fixtureDef, final float pixelToMeterRatio) {
		final BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;
		
		final float halfWidth = entity.getWidthScaled() * 0.5f / pixelToMeterRatio;
		final float halfHeight = entity.getHeightScaled() * 0.5f / pixelToMeterRatio;

		boxBodyDef.position.x = entity.x / pixelToMeterRatio + halfWidth;
		boxBodyDef.position.y = entity.y / pixelToMeterRatio + halfHeight;

		final Body boxBody = phyWorld.createBody(boxBodyDef);

		final PolygonShape boxPoly = new PolygonShape();

		

		boxPoly.setAsBox(halfWidth, halfHeight);
		fixtureDef.shape = boxPoly;

		boxBody.createFixture(fixtureDef);

		boxPoly.dispose();

		boxBody.setTransform(boxBody.getWorldCenter(), entity.rotation * MathUtils.degreesToRadians);

		return boxBody;
	}
	
	
	public static Body createBoxBody(final World phyWorld, final Rectangle entity, final BodyType bodyType, final FixtureDef fixtureDef, final float pixelToMeterRatio) {
		final BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;

		boxBodyDef.position.x = entity.x / pixelToMeterRatio;
		boxBodyDef.position.y = entity.y / pixelToMeterRatio;

		final Body boxBody = phyWorld.createBody(boxBodyDef);

		final PolygonShape boxPoly = new PolygonShape();

		final float halfWidth = entity.width * 0.5f / pixelToMeterRatio;
		final float halfHeight = entity.height * 0.5f / pixelToMeterRatio;

		boxPoly.setAsBox(halfWidth, halfHeight);
		fixtureDef.shape = boxPoly;

		boxBody.createFixture(fixtureDef);

		boxPoly.dispose();

		boxBody.setTransform(boxBody.getWorldCenter(), 0);

		return boxBody;
	}
	
	
	public static FixtureDef createFixtureDef(final float density, final float elasticity, final float friction) {
		return PhysicsFactory.createFixtureDef(density, elasticity, friction, false);
	}

	public static FixtureDef createFixtureDef(final float density, final float elasticity, final float friction, final boolean sensor) {
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.restitution = elasticity;
		fixtureDef.friction = friction;
		fixtureDef.isSensor = sensor;
		return fixtureDef;
	}

	public static FixtureDef createFixtureDef(final float density, final float elasticity, final float friction, final boolean sensor, final short categoryBits, final short maskBits, final short groupIndex) {
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.restitution = elasticity;
		fixtureDef.friction = friction;
		fixtureDef.isSensor = sensor;
		final Filter filter = fixtureDef.filter;
		filter.categoryBits = categoryBits;
		filter.maskBits = maskBits;
		filter.groupIndex = groupIndex;
		return fixtureDef;
	}
	
	public static void updateBody(Body body, BasicEntity entity, boolean posUpdate, boolean rotUpdate) {

		final float ptmRatio = Settings.PIXEL_TO_METER_RATIO;

		Vector2 pos = body.getPosition();
		float angle = body.getAngle();

		// update position
		if(posUpdate) {
			float x = entity.getX();
			float y = entity.getY();

			float ShapeHalfBaseWidth = entity.getWidth() * 0.5f;
			float ShapeHalfBaseHeight = entity.getHeight() * 0.5f;

			pos.x = x / ptmRatio + ShapeHalfBaseWidth;
			pos.y = y / ptmRatio + ShapeHalfBaseHeight;
		}

		// update rotation
		if(rotUpdate) {
			angle = MathUtils.degreesToRadians * entity.getRotation();
		}

		body.setTransform(pos, angle);
	}

	public static void updateEntity(BasicEntity entity, Body body, boolean posUpdate, boolean rotUpdate){

		final float ptmRatio = Settings.PIXEL_TO_METER_RATIO;

		// update position
		if(posUpdate) {
			final Vector2 pos = body.getPosition();
			float ShapeHalfBaseWidth = entity.getWidth() * 0.5f;
			float ShapeHalfBaseHeight = entity.getHeight() * 0.5f;
			entity.setPosition(pos.x * ptmRatio - ShapeHalfBaseWidth, 
					pos.y * ptmRatio - ShapeHalfBaseHeight);
		}

		// update rotation
		if(rotUpdate) {
			final float angle = body.getAngle();
			entity.setRotation(MathUtils.radiansToDegrees * angle);
		}
	}

}
