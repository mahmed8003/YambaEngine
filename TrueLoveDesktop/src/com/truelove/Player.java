package com.truelove;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.engine.PhysicsFactory;
import com.engine.Settings;
import com.engine.Utility;
import com.engine.entity.AnimatedSprite;
import com.engine.entity.AnimationEventListener;

public class Player extends Actor implements AnimationEventListener {
	
	private final float MAX_VELOCITY = 14f;		
	boolean jump = false;
	World world;
	Body player;
	Fixture playerPhysicsFixture;
	Fixture playerSensorFixture;
	
	AnimatedSprite boyPlayer;
	AnimatedSprite girlPlayer;
	AnimatedSprite currentSelection;
	Body boyBody;
	Body girlBody;
	
	String prevAnimName;
	private int moveDirectionX = 0;
	private float speed = 0.1f;
	private boolean isBoyCanJump;
	Assets mAssets;
	
	public Player(World world) {
		mAssets = Assets.getInstance();
		this.world = world;
		boyPlayer = new AnimatedSprite(mAssets.boyRegion, 4, 4);
		girlPlayer = new AnimatedSprite(mAssets.girlRegion, 4, 4);
		
		boyPlayer.addNewAnimation("walk_right", new int[]{8, 9, 10, 11});
		boyPlayer.addNewAnimation("walk_left", new int[]{4, 5, 6, 7});
		boyPlayer.addNewAnimation("jump", new int[]{12, 13, 14, 15});
		boyPlayer.stopAnimation();
		boyPlayer.setFrameTime(0.1f);
		boyPlayer.setPosition(150, 500);
		boyPlayer.registerListener(this);
		
		girlPlayer.addNewAnimation("walk_right", new int[]{8, 9, 10, 11});
		girlPlayer.addNewAnimation("walk_left", new int[]{4, 5, 6, 7});
		girlPlayer.addNewAnimation("jump", new int[]{12, 13, 14, 15});
		girlPlayer.stopAnimation();
		girlPlayer.setFrameTime(0.1f);
		girlPlayer.registerListener(this);
		
		FixtureDef def = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		boyBody = PhysicsFactory.createBoxBody(world, boyPlayer, BodyType.DynamicBody, def, Settings.PIXEL_TO_METER_RATIO);
		girlBody = PhysicsFactory.createBoxBody(world, girlPlayer, BodyType.DynamicBody, def, Settings.PIXEL_TO_METER_RATIO);
		
		//boyBody.setUserData(userData);
		currentSelection = boyPlayer;
	}
	
	public void swapSelection() {
		if(currentSelection == boyPlayer) {
			currentSelection = girlPlayer;
		}else{
			currentSelection = boyPlayer;
		}
	}
	
	public void walkRight() {
		moveDirectionX = 1;
		prevAnimName = "walk_right";
		currentSelection.setAnimation(prevAnimName);
	}
	
	public void walkLeft() {
		moveDirectionX = -1;
		prevAnimName = "walk_left";
		currentSelection.setAnimation(prevAnimName);
	}
	
	public void stopWalk() {
		moveDirectionX = 0;
		currentSelection.stopAnimation();
	}
	
	public void jump() {
		if(!isBoyCanJump && boyPlayer == currentSelection)
			return;
		currentSelection.setAnimation("jump");
	}
	
	public void walkSpeed(float speed) {
		this.speed = speed;
	}

	public void update(float delta) {
		currentSelection.act(delta);
		Utility.updateSprite(boyPlayer, boyBody, true, true);
		Utility.updateSprite(girlPlayer, girlBody, true, true);
		
		if(currentSelection == boyPlayer) {
			boyBody.setLinearVelocity(speed * moveDirectionX, 0);
		}else{
			girlBody.setLinearVelocity(speed * moveDirectionX, 0);
		}
		
		//Gdx.app.log("Player : ", boyBody.getPosition().toString());
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		boyPlayer.draw(batch, parentAlpha);
		girlPlayer.draw(batch, parentAlpha);
	}

	@Override
	public Actor hit(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void touchDragged(float arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void touchUp(float arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnded(AnimatedSprite animatedSprite) {
		
		
		String animName = animatedSprite.getCurrentAnimationName();
		if(animName.equals("jump")) {
			animatedSprite.setAnimation(prevAnimName);
		}
		
	}
	
	private Body createPlayer() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		Body box = world.createBody(def);
 
		PolygonShape poly = new PolygonShape();		
		poly.setAsBox(0.45f, 1.4f);
		playerPhysicsFixture = box.createFixture(poly, 1);
		poly.dispose();			
 
		CircleShape circle = new CircleShape();		
		circle.setRadius(0.45f);
		circle.setPosition(new Vector2(0, -1.4f));
		playerSensorFixture = box.createFixture(circle, 0);		
		circle.dispose();		
 
		box.setBullet(true);
 
		return box;
	}

}
