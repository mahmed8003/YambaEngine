package com.engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.engine.entity.AnimatedSprite;
import com.engine.entity.BasicEntity;
import com.engine.entity.Sprite;

public final class Utility {

	public static class BodyDesc {
		float mDensity;
		float mElasticity;
		float mFriction;
		boolean mSensor;
		int mBodyType;
		int mBodyShape;
		Shape mEntityShape;

		public BodyDesc(float density, float elasticity, float friction, boolean sensor, int bodyType, int bodyShape, Shape entityShape) {
			set(density, elasticity, friction, sensor, bodyType, bodyShape, entityShape);
		}

		public void set(float density, float elasticity, float friction, boolean sensor, int bodyType, int bodyShape, Shape entityShape) {
			mDensity = density;
			mElasticity = elasticity;
			mFriction = friction;
			mSensor = sensor;
			mBodyType = bodyType;
			mBodyShape = bodyShape;
			mEntityShape = entityShape;
		}
	}

	/**
	 * method to convert physics world coordinates into screen coordinates
	 * @param point
	 * @return
	 */
	public static Vector2 worldtoSceneCoord(final Vector2 point) {
		final float ptmRatio = Settings.PIXEL_TO_METER_RATIO;
		Vector2 vec = new Vector2(point.x * ptmRatio, point.y * ptmRatio);
		return vec;
	}

	/**
	 * method to convert screen coordinates into physics world coordinates
	 * @param point
	 * @return
	 */
	public static Vector2 scenetoWorldCoord(final Vector2 point) {
		final float ptmRatio = Settings.PIXEL_TO_METER_RATIO;
		Vector2 vec = new Vector2(point.x / ptmRatio, point.y / ptmRatio);
		return vec;
	}

	public static void updateBody(Body body, AnimatedSprite sprite, boolean posUpdate, boolean rotUpdate) {

		final float ptmRatio = Settings.PIXEL_TO_METER_RATIO;

		Vector2 pos = body.getPosition();
		float angle = body.getAngle();

		// update position
		if(posUpdate) {
			float x = sprite.getX();
			float y = sprite.getY();

			float ShapeHalfBaseWidth = sprite.getWidth() * 0.5f;
			float ShapeHalfBaseHeight = sprite.getHeight() * 0.5f;

			pos.x = x / ptmRatio + ShapeHalfBaseWidth;
			pos.y = y / ptmRatio + ShapeHalfBaseHeight;
		}

		// update rotation
		if(rotUpdate) {
			angle = degToRad(sprite.getRotation());//MathUtils.degreesToRadians * sprite.getRotation();
		}

		body.setTransform(pos, angle);
	}

	public static void updateSprite(AnimatedSprite sprite, Body body, boolean posUpdate, boolean rotUpdate){

		final float ptmRatio = Settings.PIXEL_TO_METER_RATIO;

		// update position
		if(posUpdate) {
			final Vector2 pos = body.getPosition();
			float ShapeHalfBaseWidth = sprite.getWidth() * 0.5f;
			float ShapeHalfBaseHeight = sprite.getHeight() * 0.5f;
			sprite.setPosition(pos.x * ptmRatio - ShapeHalfBaseWidth, 
					pos.y * ptmRatio - ShapeHalfBaseHeight);
		}

		// update rotation
		if(rotUpdate) {
			final float angle = body.getAngle();
			sprite.setRotation(radToDeg(angle));
		}
	}
	
	public static void updateSprite(Sprite sprite, Body body, boolean posUpdate, boolean rotUpdate){

		final float ptmRatio = Settings.PIXEL_TO_METER_RATIO;

		// update position
		if(posUpdate) {
			final Vector2 pos = body.getPosition();
			float ShapeHalfBaseWidth = sprite.getWidth() * 0.5f;
			float ShapeHalfBaseHeight = sprite.getHeight() * 0.5f;
			sprite.setPosition(pos.x * ptmRatio - ShapeHalfBaseWidth, 
					pos.y * ptmRatio - ShapeHalfBaseHeight);
		}

		// update rotation
		if(rotUpdate) {
			final float angle = body.getAngle();
			sprite.setRotation(radToDeg(angle));
		}
	}
	
	public static Body createBody(BasicEntity gameObject) {
		Body body = null;
		return body;
	}

	
	public static final float radToDeg(final float pRad) {
		return MathUtils.radiansToDegrees * pRad;
	}

	public static final float degToRad(final float pDegree) {
		return MathUtils.degreesToRadians * pDegree;
	}
}
