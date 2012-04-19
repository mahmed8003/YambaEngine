package com.truelove.screens;

import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.engine.GameScreen;
import com.engine.LevelLoader;
import com.engine.ScreenManager;
import com.engine.entity.Boxi;
import com.engine.entity.Platform;
import com.engine.entity.PushButton;
import com.engine.entity.Sprite;
import com.truelove.Assets;

public class GamePlayScreen extends GameScreen {
	
	public final static String TAG = "CreditsScreen";
	private Stage mMenuStage;
	private Stage mStage;
	private final Assets mAssets;
	public final ArrayList<Platform> mPlatforms;
	public final ArrayList<Boxi> mBoxis;
	
	//
	Box2DDebugRenderer debugRenderer;

	public GamePlayScreen(ScreenManager manager) {
		super(manager);
		initPhysicsWorld(new Vector2(0, -30), true, 4, 4);
		mAssets = Assets.getInstance();
		mMenuStage = new Stage(getScreenMgr().getVirtualWidth(), getScreenMgr().getVirtualHeight(), true);
		mStage = new Stage(getScreenMgr().getVirtualWidth(), getScreenMgr().getVirtualHeight(), true);
		mPlatforms = new ArrayList<Platform>();
		mBoxis = new ArrayList<Boxi>();
		debugRenderer = new Box2DDebugRenderer(true, false, false);
	}
	
	@Override
	public void show() {
		/* setting up background */
		Sprite sprite;
		sprite = new Sprite("background", mAssets.backgroundRegion);
		mMenuStage.addActor(sprite);
		
		/* loading sprite */
		final Sprite loadingSprite = new Sprite("loading_screen", mAssets.loadingRegion);
		mMenuStage.addActor(loadingSprite);
		
		/* perform some heavy task */
		LevelLoader loader = new LevelLoader(this);
		loader.loadLevel("data/levels/level_01/level.xml");
		loader.dispose();
		initLevel();
		
		
		/* creating screen contents */
		addButton("back_btn", 6, 40, "back_up", "back_down");
		
		/*removing loading screen */
		mMenuStage.removeActor(loadingSprite);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		mMenuStage.act(delta);
		mMenuStage.draw();
		
		/* */
		mStage.act(delta);
		mStage.draw();
		
		//debugRenderer.render(getWorld(), mStage.getCamera().combined);
	}
	
	@Override
	public void dispose() {
		mMenuStage.dispose();
		mStage.dispose();
		super.dispose();
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		mStage.touchDown(x, y, pointer, button);
		boolean touched = mMenuStage.touchDown(x, y, pointer, button);
		return touched;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		mStage.touchUp(x, y, pointer, button);
		boolean touched = mMenuStage.touchUp(x, y, pointer, button);
		if(touched) {
			Actor hitActor = mMenuStage.getLastTouchedChild();
			if(hitActor == null) {
				return touched;
			}
			
			if(hitActor.name.startsWith("back_btn")) {
				getScreenMgr().popScreen();
			}
		}
		return touched;
	}
	
	private void addButton(String name, float x, float y, String upRegion, String downRegion) {
		float dy = getScreenMgr().getVirtualHeight() - y;
		PushButton btn = new PushButton(name, x, dy, mAssets.assets.getTextureRegion(upRegion), mAssets.assets.getTextureRegion(downRegion));
		btn.setOnClickSound(mAssets.mClickSound);
		mMenuStage.addActor(btn);
	}
	
	private void initLevel() {
		for(Platform platform : mPlatforms) {
			mStage.addActor(platform);
		}
		
		for(Boxi boxi : mBoxis) {
			mStage.addActor(boxi);
		}
	}


}
