package com.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class BasicEntity extends Actor {

	private Rectangle bounds = new Rectangle();

	public BasicEntity() {
	}

	BasicEntity(String name) {
		super(name);
	}

	public String getName() {
		return name;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
	public void setScale(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	public float getWidthScaled() {
		return width * scaleX;
	}
	
	public float getHeightScaled() {
		return height * scaleY;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public float getOriginX() {
		return originX;
	}

	public void setOriginX(float originX) {
		this.originX = originX;
	}

	public float getOriginY() {
		return originY;
	}

	public void setOriginY(float originY) {
		this.originY = originY;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void translate(float xAmount, float yAmount) {
		this.x += xAmount;
		this.y += yAmount;
	}

	public void setOrigin(float originX, float originY) {
		this.originX = originX;
		this.originY = originY;
	}

	public Rectangle getBoundingRectangle() {
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		bounds.height = height;
		return bounds;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		return x > 0 && y > 0 && x < width && y < height;
	}

	@Override
	public void touchUp(float x, float y, int pointer) {
	}

	@Override
	public void touchDragged(float x, float y, int pointer) {
	}

	@Override
	public Actor hit(float x, float y) {
		if (x > 0 && x < width)
			if (y > 0 && y < height)
				return this;
		return null;
	}
	
}
