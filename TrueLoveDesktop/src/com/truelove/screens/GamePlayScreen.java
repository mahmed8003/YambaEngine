package com.truelove.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.engine.GameScreen;
import com.engine.PhysicsFactory;
import com.engine.ScreenManager;
import com.engine.Settings;
import com.engine.Truck;
import com.engine.entity.AnimatedButton;
import com.engine.entity.BasicEntity;
import com.engine.entity.Boxi;
import com.engine.entity.BoyPlayer;
import com.engine.entity.Sprite;
import com.nitro.level.GameObject;
import com.nitro.level.Layer;
import com.nitro.level.Level;
import com.truelove.Assets;
import com.truelove.Player;

public class GamePlayScreen extends GameScreen implements
		InputProcessor {

	private Assets mAssets;

	private Stage mStage;
	private Stage mHud;

	private Player mPlayer;
	private BasicEntity mGameObject;
	private BoyPlayer mBoyPlayer;
	
	private AnimatedButton btn;
	private Truck mTruck;
	
	private Boxi mBoxi;
	
	private Box2DDebugRenderer box2dDebugRenderer;

	public GamePlayScreen(ScreenManager manager) {
		super(manager);

		mAssets = Assets.getInstance();
		mAssets.loadAudioResources();
		mAssets.playBackgroundMusic();
		mAssets.loadHudResources();

		mStage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false);
		mHud = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false);


		this.initPhysicsWorld(new Vector2(0, -20), true, 4, 4);

		FixtureDef def = PhysicsFactory.createFixtureDef(1, 0.2f, 1.0f);
		Rectangle r = new Rectangle(0, 0, 10000, 5);
		Body body = PhysicsFactory.createBoxBody(getWorld(), r, BodyType.StaticBody, def, Settings.PIXEL_TO_METER_RATIO);
		Body body2 = PhysicsFactory.createBoxBody(getWorld(), r, BodyType.StaticBody, def, Settings.PIXEL_TO_METER_RATIO);
		body2.setTransform(100, 10, 15 * MathUtils.degreesToRadians);

		mPlayer = new Player(getWorld());
		mBoyPlayer = new BoyPlayer(getWorld(), mAssets.boyRegion, 4, 4);
		

		//

		/*
		 * mHud.addActor(new Image("exit button", mAssets.exit));
		 * mHud.addActor(new Image("left button", mAssets.left));
		 * mHud.addActor(new Image("reset button", mAssets.reset));
		 * mHud.addActor(new Image("right button", mAssets.right));
		 * 
		 * mHud.addActor(new Image("swap button", mAssets.swap));
		 * mHud.addActor(new Image("up button", mAssets.up));
		 */
		setUpHud();

		/*
		 * my mechanici
		 */
		/*
		 * Level level = new Level(); Layer layer1 = new Layer();
		 * layer1.setName("my layer 1"); Layer layer2 = new Layer();
		 * layer2.setName("my layer 2"); layer2.addProperty("physics",
		 * "enable"); Sprite sprite1 = new Sprite("sprite 1", mAssets.exit);
		 * Sprite sprite2 = new Sprite("sprite 2", mAssets.exit); Sprite sprite3
		 * = new Sprite("sprite 3", mAssets.exit);
		 * 
		 * layer1.addGameObject(sprite1); layer1.addGameObject(sprite2);
		 * layer2.addGameObject(sprite3);
		 * 
		 * level.addLayer(layer1); level.addLayer(layer2);
		 */
		/*
		 * GameObject gameObject = new GameObject(); Layer layer = new Layer();
		 * Level level = new Level(); layer.GameObjects.add(gameObject);
		 * level.Layers.add(layer);
		 * 
		 * ObjectMapper mapper = new ObjectMapper(); try {
		 * Gdx.app.log("JSON converted : ", mapper.writeValueAsString(level)); }
		 * catch (Exception e) { Gdx.app.log("Exception : ", e.getMessage()); }
		 */
		getAssets().loadTextureRegions("data/gfx/my_art.png", "data/gfx/my_art.txt");
		getAssets().loadTextureRegions("data/gfx/texbg.png", "data/gfx/texbg.txt");
		
		
		/*
		Level level = Level.loadFromFile("data/level/test.json");
		
		for (Layer layer : level.Layers) {
			if (!layer.Name.startsWith("B"))
				continue;
			for (GameObject gameObject : layer.GameObjects) {
				Sprite sprite = new Sprite(gameObject.Name, getAssets().getTextureRegion(gameObject.TextureName));
				//sprite.setPosition(0, 0);
				//sprite.setOrigin(gameObject.OriginX, gameObject.OriginY);
				sprite.setScale(gameObject.ScaleX, gameObject.ScaleY);
				sprite.setRotation(gameObject.Rotation);
				float x = gameObject.X + gameObject.OriginX;
				float y = gameObject.Y + gameObject.OriginY;
				// (sprite.height);
				sprite.setPosition(gameObject.X, y);
				mStage.addActor(sprite);
				if (sprite.name.startsWith("tree_02_")) {
					mGameObject = sprite;
					// mGameObject.setOrigin(0, 0);
				}
			}

		}*/
		// mStage.getCamera().translate(100, 10, 0);

		/* setting up input processor class */
		Gdx.input.setInputProcessor(this);

	}

	@Override
	public void show() {
		mPlayer.walkSpeed(1);
		mStage.addActor(mPlayer);
		btn = new AnimatedButton("Test Button", mAssets.animButton);
		btn.setPosition(180, 180);
		mHud.addActor(btn);
		mStage.addActor(mBoyPlayer);
		// sprite.rotate(30);
		// sprite.setLoopMethod(AnimatedSprite.LOOP_REVERSE);
		//mTruck = new Truck(getWorld());
		//mTruck.initCarPosition(400, 200);
		//mTruck.init();
		//mStage.addActor(mTruck);
		mBoxi = new Boxi(getWorld(), "myBox", 700, 400, null, Boxi.BOX, Boxi.CONVERTIBLE);
		mStage.addActor(mBoxi);
		box2dDebugRenderer = new Box2DDebugRenderer(true, true, false); 
	}
	

	@Override
	public void render(float delta) {
		
		GL10 gl = Gdx.graphics.getGL10();
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		box2dDebugRenderer.render(getWorld(), mStage.getCamera().combined);

		mStage.act(delta);
		mStage.draw();
		mHud.draw();
		mPlayer.update(delta);
		super.render(delta);
	}

	@Override
	public void dispose() {
		mStage.dispose();
		mHud.dispose();
		super.dispose();
	}

	@Override
	public boolean keyDown(int keyCode) {
		if (keyCode == Input.Keys.A) {
			mStage.getCamera().translate(-5, 0, 0);

		}

		if (keyCode == Input.Keys.D) {
			mStage.getCamera().translate(5, 0, 0);
		}

		if (keyCode == Input.Keys.W) {
			mStage.getCamera().translate(0, 5, 0);

		}

		if (keyCode == Input.Keys.S) {
			mStage.getCamera().translate(0, -5, 0);
		}

		if (keyCode == Input.Keys.R) {
			mGameObject.setRotation(mGameObject.getRotation() + 10);
		}
		
		if (keyCode == Input.Keys.K) {
			//mTruck.setLeftPressed(true);
		}
		
		if (keyCode == Input.Keys.L) {
			//mTruck.setRightPressed(true);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char keyCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keyCode) {
		if (keyCode == Input.Keys.K) {
			//mTruck.setLeftPressed(false);
		}
		
		if (keyCode == Input.Keys.L) {
			//mTruck.setRightPressed(false);
		}
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		mStage.touchDown(x, y, pointer, button);
		boolean touched = mHud.touchDown(x, y, pointer, button);
		if (touched) {
			Actor hitActor = mHud.getLastTouchedChild();
			if (hitActor == null)
				return touched;

			if (hitActor.name.startsWith("right")) {
				mPlayer.walkRight();
				mBoyPlayer.moveToRight();
			}
			if (hitActor.name.startsWith("left")) {
				mPlayer.walkLeft();
				mBoyPlayer.moveToLeft();
			}
			if (hitActor.name.startsWith("up")) {
				mPlayer.jump();
				mBoyPlayer.jump();
			}
			if (hitActor.name.startsWith("swap")) {
				mPlayer.swapSelection();
			}
		}
		return touched;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		mStage.touchUp(x, y, pointer, button);
		boolean touched = mHud.touchUp(x, y, pointer, button);
		if (touched) {
			Actor hitActor = mHud.getLastTouchedChild();
			if (hitActor == null)
				return touched;

			if (hitActor.name.startsWith("exit")) {
				mAssets.playClickSound();
				Actor actor = getActorStartsWith(mHud, "sound_off");
				actor.visible = false;

			}

			if (hitActor.name.startsWith("right")) {
				mPlayer.stopWalk();
				mBoyPlayer.stopToMove();
			}
			if (hitActor.name.startsWith("left")) {
				mPlayer.stopWalk();
				mBoyPlayer.stopToMove();
			}
			if (hitActor.name.startsWith("up")) {
				mPlayer.stopWalk();
			}
		}
		return touched;
	}

	private void setUpHud() {
		addToHud("exit button", mAssets.exit, 0, 5);
		addToHud("reset button", mAssets.reset, 65, 5);
		addToHud("sound_off button", mAssets.sound_off, 130, 5);
		addToHud("sound_on button", mAssets.sound_on, 195, 5);

		addToHud("left button", mAssets.left, 10, 550);
		addToHud("right button", mAssets.right, 100, 544);
		addToHud("up button", mAssets.up, 800, 544);
		addToHud("swap button", mAssets.swap, 860, 460);
	}

	private void addToHud(String name, TextureRegion region, float x, float y) {
		Image image = new Image(name, region);
		image.x = x;
		image.y = Gdx.graphics.getHeight() - y - image.height;
		mHud.addActor(image);
	}

	private Actor getActorStartsWith(Stage stage, String prefix) {
		Actor actor = null;
		List<Actor> actors = stage.getActors();
		for (int i = 0; i < actors.size(); i++) {
			Actor t = actors.get(i);
			if (t.name.startsWith(prefix)) {
				actor = t;
				break;
			}
		}
		return actor;
	}
}
