package com.engine.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.engine.PhysicsFactory;
import com.engine.Settings;
import com.engine.Utility;
import com.truelove.Assets;

public class Boxi extends BasicEntity implements AnimationEventListener {
	
	/* states can be Box or circle */
	public static final int BOX = 1;
	public static final int CIRCLE = 2;
	
	/* types can be convertible nonconvertable */
	public static final int CONVERTIBLE = 1;
	public static final int NONCONVERTABLE = 2;
	
	
	
	private final World world;
	private Sprite boxSprite;
	private Sprite circleSprite;
	private Body boxBody;
	private Body circleBody;
	private Body body;
	private int state;	// current state (Box or circle)
	private final int type;
	
	/* Annotating box */
	private AnimatedSprite faceSprite;
	
	public Boxi(World world, String name, float x, float y, TextureRegion boxiRegion, int state, int type) {
		super(name);
		this.world = world;
		this.state = state;
		this.type = type;
		
		boxSprite = new Sprite("boxSprite", x, y, Assets.getInstance().box);
		circleSprite = new Sprite("circleSprite", x, y, Assets.getInstance().cir);
		
		initBoxiEntity(boxSprite);
		
		final FixtureDef def = PhysicsFactory.createFixtureDef(1.0f, 0.2f, 1.0f);
		boxBody = PhysicsFactory.createBoxBody(world, boxSprite, BodyType.DynamicBody, def, Settings.PIXEL_TO_METER_RATIO);
		circleBody = PhysicsFactory.createCircleBody(world, circleSprite, BodyType.DynamicBody, def, Settings.PIXEL_TO_METER_RATIO);
		
		if(state == BOX) {
			body = boxBody;
			circleBody.setActive(false);
			circleSprite.setVisible(false);
		}else{
			body = circleBody;
			boxBody.setActive(false);
			boxSprite.setVisible(false);
		}
		
		/*faceSprite = new AnimatedSprite(boxiRegion, 2, 2);
		faceSprite.addNewAnimation("normal", new int[]{1,2});
		faceSprite.addNewAnimation("aggressive", new int[]{1,2});
		faceSprite.addNewAnimation("sad", new int[]{1,2});
		faceSprite.addNewAnimation("sleep", new int[]{1,2});
		faceSprite.registerListener(this);
		faceSprite.setAnimation("normal");*/
	}
	
	@Override
	public void act(float delta) {
		
		if(state == BOX) {
			Utility.updateSprite(boxSprite, boxBody, true, true);
			updateBoxiEntity(boxSprite);
		}else{
			Utility.updateSprite(circleSprite, circleBody, true, true);
			updateBoxiEntity(circleSprite);
		}
		
		//Gdx.app.log("BOX ", Float.toString(boxBody.getLinearVelocity().len()));
		//Gdx.app.log("Cir ", Float.toString(circleBody.getLinearVelocity().len()));
		Gdx.app.log("Current ", Float.toString(body.getLinearVelocity().angle()));
		
		super.act(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		boxSprite.draw(batch, parentAlpha);
		circleSprite.draw(batch, parentAlpha);
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public void remove() {
		world.destroyBody(boxBody);
		world.destroyBody(circleBody);
		boxSprite.remove();
		circleSprite.remove();
		super.remove();
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer) {
		boolean result = x > 0 && y > 0 && x < width && y < height;
		if(result && type == CONVERTIBLE) {
			toggle();
		}
		return result;
	}
	
	@Override
	public void onAnimationEnded(AnimatedSprite animatedSprite) {
		boolean isAwake = body.isAwake();
		float speed = body.getLinearVelocity().len();
		float angle = body.getLinearVelocity().angle();
		
		if(!isAwake) {
			faceSprite.setAnimation("sleep");
		}else{
			if(speed < 2) {
				faceSprite.setAnimation("normal");
			}else{
				if(angle > 260 && angle < 280) {
					faceSprite.setAnimation("sad");
				}else{
					faceSprite.setAnimation("aggressive");
				}
				
			}
		}
	}
	
	
	
	/* =======================================================================================
	     private methods
	   ======================================================================================= */
	
	public void toggle() {
		if(state == BOX) {
			state = CIRCLE;
			swapParams(circleSprite, boxSprite);
			body = swapParams(circleBody, boxBody);
		}else{
			state = BOX;
			swapParams(boxSprite, circleSprite);
			body = swapParams(boxBody, circleBody);
		}
	}
	
	private void initBoxiEntity(Sprite sprite) {
		setPosition(sprite.getX(), sprite.getY());
		setOrigin(sprite.getOriginX(), sprite.getOriginY());
		setRotation(sprite.getRotation());
		setWidth(sprite.getWidth());
		setHeight(sprite.getHeight());
	}
	
	private void updateBoxiEntity(Sprite sprite) {
		setPosition(sprite.getX(), sprite.getY());
		setRotation(sprite.getRotation());
	}
	
	private Body swapParams(Body bodyA, Body bodyB) {
		bodyA.setTransform(bodyB.getPosition(), bodyB.getAngle());
		bodyA.setLinearVelocity(bodyB.getLinearVelocity());
		bodyA.setAngularVelocity(bodyB.getAngularVelocity());
		bodyB.setActive(false);
		bodyA.setActive(true);
		return bodyA;
	}
	
	private void swapParams(Sprite spriteA, Sprite spriteB) {
		spriteB.setVisible(false);
		spriteA.setVisible(true);
	}
	
	

}
