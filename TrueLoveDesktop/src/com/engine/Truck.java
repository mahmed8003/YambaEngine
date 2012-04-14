package com.engine;

import javax.sound.sampled.Line;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.engine.entity.BasicEntity;
import com.engine.entity.Sprite;
import com.truelove.Assets;

public class Truck extends BasicEntity {

	private final Assets mAssets;
	private World world;
	private final float degreesToRadians = 0.0174532925f;
	private final float worldScale = Settings.PIXEL_TO_METER_RATIO;
	private Body car;
	private RevoluteJoint leftWheelRevoluteJoint;
	private RevoluteJoint rightWheelRevoluteJoint;
	private boolean left = false;
	private boolean right = false;
	private float motorSpeed = 0;
	private PrismaticJoint leftAxlePrismaticJoint;
	private PrismaticJoint rightAxlePrismaticJoint;
	private Body leftWheel;
	private Body rightWheel;
	//
	private float carPosX = 320;
	private float carPosY = 250;
	private float carWidth = 45;
	private float carHeight = 10;
	private float axleContainerDistance = 50;
	private float axleContainerWidth = 5;
	private float axleContainerHeight = 40;
	private float axleContainerDepth = 30;
	private float axleAngle = 30;
	private float wheelRadius = 25;
	//
	private Sprite carSprite;
	private Sprite leftWheelSprite;
	private Sprite rightWheelSprite;
	private Line leftLine;
	private Line rightLine;

	public Truck(World world) {
		this.world = world;
		mAssets = Assets.getInstance();
	}

	public void init() {
		
		//
		carSprite = new Sprite("carbody", carPosX, carPosY, mAssets.carBody);
		leftWheelSprite = new Sprite("carWheel1", 0, 0, mAssets.carWheel);
		rightWheelSprite = new Sprite("carWheel2", 0, 0, mAssets.carWheel);
		
		/*leftLine = new Line(0, 0, 1, 1);
		leftLine.setColor(0, 0, 0);
		leftLine.setLineWidth(4.0f);
		
		rightLine = new Line(0, 0, 1, 1);
		rightLine.setColor(0, 0, 0);
		rightLine.setLineWidth(4.0f);*/
		
		carWidth = carSprite.getWidth() / 2.0f;
		carHeight = carSprite.getHeight() / 2.0f;
		wheelRadius = leftWheelSprite.getHeight() / 2.0f;
		
		MassData md = new MassData();
		md.I = 1;
		md.mass = 10;

		// ************************ THE CAR ************************ //
		PolygonShape carShape = new PolygonShape();
		carShape.setAsBox(carWidth / worldScale, carHeight/ worldScale);
		FixtureDef carFixture = new FixtureDef();
		carFixture.density = 1.0f;
		carFixture.friction = 1.0f;
		carFixture.restitution = 0.2f;
		carFixture.filter.groupIndex = -1;
		carFixture.shape = carShape;
		BodyDef carBodyDef = new BodyDef();
		carBodyDef.position.set(carPosX / worldScale, carPosY / worldScale);
		carBodyDef.type = BodyType.DynamicBody;

		// ******************* LEFT AXLE CONTAINER ********************* //
		PolygonShape leftAxleContainerShape = new PolygonShape();
		leftAxleContainerShape.setAsBox(axleContainerWidth / worldScale, 
										axleContainerHeight	/ worldScale, 
										new Vector2(-axleContainerDistance / worldScale, axleContainerDepth / worldScale), 
										axleAngle * degreesToRadians);
		
		//.setAsOrientedBox(				axleContainerWidth / worldScale, axleContainerHeight
						// worldScale, new b2Vec2(-axleContainerDistance/ worldScale, axleContainerDepth / worldScale), MathUtils.degToRad(axleAngle));
		FixtureDef leftAxleContainerFixture = new FixtureDef();
		leftAxleContainerFixture.density = 1;
		leftAxleContainerFixture.friction = 0.5f;
		leftAxleContainerFixture.restitution = 0.5f;
		leftAxleContainerFixture.filter.groupIndex = -1;
		leftAxleContainerFixture.shape = leftAxleContainerShape;

		// ****************** RIGHT AXLE CONTAINER ******************** //
		PolygonShape rightAxleContainerShape = new PolygonShape();
		rightAxleContainerShape.setAsBox(axleContainerWidth / worldScale, 
				axleContainerHeight	/ worldScale, 
				new Vector2(axleContainerDistance / worldScale, axleContainerDepth / worldScale), 
				-axleAngle * degreesToRadians);
		/*rightAxleContainerShape.SetAsOrientedBox(axleContainerWidth
				/ worldScale, axleContainerHeight / worldScale, new b2Vec2(
				axleContainerDistance / worldScale, axleContainerDepth
						/ worldScale), -axleAngle * degreesToRadians);*/
		FixtureDef rightAxleContainerFixture = new FixtureDef();
		rightAxleContainerFixture.density = 1;
		rightAxleContainerFixture.friction = 0.5f;
		rightAxleContainerFixture.restitution = 0.5f;
		rightAxleContainerFixture.filter.groupIndex = -1;
		rightAxleContainerFixture.shape = rightAxleContainerShape;
		
		// ************************ MERGING ALL TOGETHER ************************ //
		car = world.createBody(carBodyDef);
		car.createFixture(carFixture);
		car.createFixture(leftAxleContainerFixture);
		car.createFixture(rightAxleContainerFixture);
		car.setMassData(md);
		
		// ************************ THE AXLES ************************ //
					PolygonShape leftAxleShape = new PolygonShape();
					leftAxleShape.setAsBox(axleContainerWidth/worldScale/2,
											axleContainerHeight/worldScale,
											new Vector2(0,0),
											axleAngle*degreesToRadians);
					//leftAxleShape.SetAsOrientedBox(axleContainerWidth/worldScale/2,axleContainerHeight/worldScale,new b2Vec2(0,0),axleAngle*degreesToRadians);
					FixtureDef leftAxleFixture = new FixtureDef();
					leftAxleFixture.density=1.0f;
					leftAxleFixture.friction=0.5f;
					leftAxleFixture.restitution=0.5f;
					leftAxleFixture.shape=leftAxleShape;
					leftAxleFixture.filter.groupIndex=-1;
					BodyDef leftAxleBodyDef = new BodyDef();
					leftAxleBodyDef.type=BodyType.DynamicBody;
					Body leftAxle=world.createBody(leftAxleBodyDef);
					leftAxle.createFixture(leftAxleFixture);
					leftAxle.setTransform(new Vector2(carPosX/worldScale-axleContainerDistance/worldScale-axleContainerHeight/worldScale*(float)Math.cos((90-axleAngle)*degreesToRadians),carPosY/worldScale+axleContainerDepth/worldScale+axleContainerHeight/worldScale*(float)Math.sin((90-axleAngle)*degreesToRadians)), leftAxle.getAngle());
					//leftAxle.setPosition(new b2Vec2(carPosX/worldScale-axleContainerDistance/worldScale-axleContainerHeight/worldScale*Math.cos((90-axleAngle)*degreesToRadians),carPosY/worldScale+axleContainerDepth/worldScale+axleContainerHeight/worldScale*Math.sin((90-axleAngle)*degreesToRadians)));
					
					PolygonShape rightAxleShape = new PolygonShape();
					rightAxleShape.setAsBox(axleContainerWidth/worldScale/2,
											axleContainerHeight/worldScale,
											new Vector2(0,0),
											-axleAngle*degreesToRadians);
					FixtureDef rightAxleFixture = new FixtureDef();
					rightAxleFixture.density=1.0f;
					rightAxleFixture.friction=0.5f;
					rightAxleFixture.restitution=0.5f;
					rightAxleFixture.shape=rightAxleShape;
					rightAxleFixture.filter.groupIndex=-1;
					BodyDef rightAxleBodyDef = new BodyDef();
					rightAxleBodyDef.type=BodyType.DynamicBody;
					Body rightAxle=world.createBody(rightAxleBodyDef);
					rightAxle.createFixture(rightAxleFixture);
					rightAxle.setTransform(new Vector2(carPosX/worldScale+axleContainerDistance/worldScale+axleContainerHeight/worldScale*(float)Math.cos((90-axleAngle)*degreesToRadians),carPosY/worldScale+axleContainerDepth/worldScale+axleContainerHeight/worldScale*(float)Math.sin((90-axleAngle)*degreesToRadians)), rightAxle.getAngle());
					//rightAxle.setPosition(new b2Vec2(carPosX/worldScale+axleContainerDistance/worldScale+axleContainerHeight/worldScale*Math.cos((90-axleAngle)*degreesToRadians),carPosY/worldScale+axleContainerDepth/worldScale+axleContainerHeight/worldScale*Math.sin((90-axleAngle)*degreesToRadians)));


					// ************************ THE WHEELS ************************ //;
					CircleShape wheelShape = new CircleShape();
					wheelShape.setRadius(wheelRadius/worldScale);
					FixtureDef wheelFixture = new FixtureDef();
					wheelFixture.density=1.0f;
					wheelFixture.friction=1.0f;
					wheelFixture.restitution=0.2f;
					wheelFixture.filter.groupIndex=-1;
					wheelFixture.shape=wheelShape;
					BodyDef wheelBodyDef = new BodyDef();
					wheelBodyDef.type=BodyType.DynamicBody;
					wheelBodyDef.position.set(carPosX/worldScale-axleContainerDistance/worldScale-2*axleContainerHeight/worldScale*(float)Math.cos((90-axleAngle)*degreesToRadians),carPosY/worldScale+axleContainerDepth/worldScale+2*axleContainerHeight/worldScale*(float)Math.sin((90-axleAngle)*degreesToRadians));
					leftWheel=world.createBody(wheelBodyDef);
					leftWheel.createFixture(wheelFixture);
					leftWheel.setMassData(md);
					wheelBodyDef.position.set(carPosX/worldScale+axleContainerDistance/worldScale+2*axleContainerHeight/worldScale*(float)Math.cos((90-axleAngle)*degreesToRadians),carPosY/worldScale+axleContainerDepth/worldScale+2*axleContainerHeight/worldScale*(float)Math.sin((90-axleAngle)*degreesToRadians));
					rightWheel=world.createBody(wheelBodyDef);
					rightWheel.createFixture(wheelFixture);
					rightWheel.setMassData(md);
					
					// ************************ REVOLUTE JOINTS ************************ //
					RevoluteJointDef leftWheelRevoluteJointDef=new RevoluteJointDef();
					leftWheelRevoluteJointDef.initialize(leftWheel,leftAxle,leftWheel.getWorldCenter());
					leftWheelRevoluteJointDef.enableMotor=true;
					leftWheelRevoluteJoint=(RevoluteJoint) world.createJoint(leftWheelRevoluteJointDef);
					leftWheelRevoluteJoint.setMaxMotorTorque(200);
					
					RevoluteJointDef rightWheelRevoluteJointDef=new RevoluteJointDef();
					rightWheelRevoluteJointDef.initialize(rightWheel,rightAxle,rightWheel.getWorldCenter());
					rightWheelRevoluteJointDef.enableMotor=true;
					rightWheelRevoluteJoint=(RevoluteJoint) world.createJoint(rightWheelRevoluteJointDef);
					rightWheelRevoluteJoint.setMaxMotorTorque(200);

					
					// ************************ PRISMATIC JOINTS ************************ //
					PrismaticJointDef leftAxlePrismaticJointDef =new PrismaticJointDef();
					leftAxlePrismaticJointDef.lowerTranslation=-0.5f;
					leftAxlePrismaticJointDef.upperTranslation=axleContainerDepth/worldScale;
					leftAxlePrismaticJointDef.enableLimit=true;
					leftAxlePrismaticJointDef.enableMotor=true;
					leftAxlePrismaticJointDef.initialize(car,leftAxle,leftAxle.getWorldCenter(), new Vector2(-(float)Math.cos((90-axleAngle)*degreesToRadians),(float)Math.sin((90-axleAngle)*degreesToRadians)));
					leftAxlePrismaticJoint = (PrismaticJoint) world.createJoint(leftAxlePrismaticJointDef);
					leftAxlePrismaticJoint.setMaxMotorForce(20);                         
					leftAxlePrismaticJoint.setMotorSpeed(10);
					
					
					PrismaticJointDef rightAxlePrismaticJointDef =new PrismaticJointDef();
					rightAxlePrismaticJointDef.lowerTranslation=-0.5f;
					rightAxlePrismaticJointDef.upperTranslation=axleContainerDepth/worldScale;
					rightAxlePrismaticJointDef.enableLimit=true;
					rightAxlePrismaticJointDef.enableMotor=true;
					rightAxlePrismaticJointDef.initialize(car,rightAxle,rightAxle.getWorldCenter(), new Vector2((float)Math.cos((90-axleAngle)*degreesToRadians),(float)Math.sin((90-axleAngle)*degreesToRadians)));
					rightAxlePrismaticJoint=(PrismaticJoint) world.createJoint(rightAxlePrismaticJointDef);
					rightAxlePrismaticJoint.setMaxMotorForce(20);                         
					rightAxlePrismaticJoint.setMotorSpeed(10);    
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		carSprite.draw(batch, parentAlpha);
		leftWheelSprite.draw(batch, parentAlpha);
		rightWheelSprite.draw(batch, parentAlpha);
		super.draw(batch, parentAlpha);
	}

	@Override
	public void act(float delta) {
		Utility.updateSprite(carSprite, car, true, true);
		Utility.updateSprite(leftWheelSprite, leftWheel, true, true);
		Utility.updateSprite(rightWheelSprite, rightWheel, true, true);
		
		if (left) {
			motorSpeed+=0.5;
		}
		if (right) {
			motorSpeed-=0.5;
		}
		motorSpeed*=0.99;
		if (motorSpeed>10) {
			motorSpeed=10;
		}
		leftWheelRevoluteJoint.setMotorSpeed(motorSpeed);
		rightWheelRevoluteJoint.setMotorSpeed(motorSpeed);
		
		super.act(delta);
	}

	public void update(float elapsedTime) {
		
		Utility.updateSprite(carSprite, car, true, true);
		Utility.updateSprite(leftWheelSprite, leftWheel, true, true);
		Utility.updateSprite(rightWheelSprite, rightWheel, true, true);
		
		/*
		float[] cord = carSprite.convertLocalToSceneCoordinates(32, 36);
		float x1 = cord[0];
		float y1 = cord[1];
		float x2 = (leftWheelSprite.getX() + (leftWheelSprite.getWidth() * 0.5f));
		float y2 = (leftWheelSprite.getY() + (leftWheelSprite.getHeight() * 0.5f));
		leftLine.setPosition(x1, y1, x2, y2);
		
		cord = carSprite.convertLocalToSceneCoordinates(120, 36);
		x1 = cord[0];
		y1 = cord[1];
		x2 = (rightWheelSprite.getX() + (rightWheelSprite.getWidth() * 0.5f));
		y2 = (rightWheelSprite.getY() + (rightWheelSprite.getHeight() * 0.5f));
		rightLine.setPosition(x1, y1, x2, y2);
		*/
		
		
		if (left) {
			motorSpeed+=0.5;
		}
		if (right) {
			motorSpeed-=0.5;
		}
		motorSpeed*=0.99;
		if (motorSpeed>10) {
			motorSpeed=10;
		}
		leftWheelRevoluteJoint.setMotorSpeed(motorSpeed);
		rightWheelRevoluteJoint.setMotorSpeed(motorSpeed);
		
		
		/*
		if (right) {
			leftWheelRevoluteJoint.setMotorSpeed(-20);
			leftWheelRevoluteJoint.setMaxMotorTorque(1000);

			rightWheelRevoluteJoint.setMotorSpeed(-20);
			rightWheelRevoluteJoint.setMaxMotorTorque(1000);
		}
		else if (left) {
			leftWheelRevoluteJoint.setMotorSpeed(0);
			leftWheelRevoluteJoint.setMaxMotorTorque(2000);

			rightWheelRevoluteJoint.setMotorSpeed(0);
			rightWheelRevoluteJoint.setMaxMotorTorque(2000);
		} else {
			leftWheelRevoluteJoint.setMotorSpeed(0);
			leftWheelRevoluteJoint.setMaxMotorTorque(0);
			rightWheelRevoluteJoint.setMotorSpeed(0);
			rightWheelRevoluteJoint.setMaxMotorTorque(0);
		}
		*/
	}

	@Override
	public void remove() {
		world.destroyBody(car);
		world.destroyBody(leftWheel);
		world.destroyBody(rightWheel);
		
		world.destroyJoint(leftWheelRevoluteJoint);
		world.destroyJoint(rightWheelRevoluteJoint);
		world.destroyJoint(leftAxlePrismaticJoint);
		world.destroyJoint(rightAxlePrismaticJoint);
	}

	@Override
	public float getX() {
		return carSprite.getX();
	}

	@Override
	public float getY() {
		return carSprite.getY();
	}

	@Override
	public float getRotation() {
		return carSprite.getRotation();
	}
	
	public void initCarPosition(float x, float y) {
		carPosX = x;
		carPosY = y;
	}
	
	public void setLeftPressed(boolean pressed) {
		this.left = pressed;
	}

	public void setRightPressed(boolean pressed) {
		this.right = pressed;
	}

}
