package com.truelove.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.engine.GameScreen;
import com.engine.ScreenManager;
import com.engine.entity.PushButton;
import com.engine.entity.Sprite;
import com.engine.entity.ToggleButton;
import com.truelove.Assets;

public class MainMenuScreen extends GameScreen {
	
	public final static String TAG = "MainMenuScreen";
	private Stage mMenuStage;
	private ArrayList<PushButton> mButtons;
	private final Assets mAssets;

	public MainMenuScreen(ScreenManager manager) {
		super(manager);
		mMenuStage = new Stage(getScreenMgr().getVirtualWidth(), getScreenMgr().getVirtualHeight(), true);
		mButtons = new ArrayList<PushButton>();
		mAssets = Assets.getInstance();
	}
	
	@Override
	public void show() {
		/* setting up background */
		Sprite sprite;
		sprite = new Sprite("background", mAssets.backgroundRegion);
		mMenuStage.addActor(sprite);
		
		/* loading screen specific resources */
		getAssets().loadTextureRegions("data/gfx/title_texture.png", "data/gfx/title_texture.txt");
		
		/* creating screen contents */
		sprite = new Sprite("title", 79, getScreenMgr().getVirtualHeight() - 114, getAssets().getTextureRegion("game_title"));
		mMenuStage.addActor(sprite);
		
		
		addButton("start_btn", 277, 226, "start_up", "start_down", false);
		addButton("level_btn", 277, 302, "level_up", "level_down", false);
		addButton("credits_btn", 277, 378, "credits_up", "credits_down", false);
		addButton("exit_btn", 277, 452, "exit_up", "exit_down", false);
		addButton("mute_btn", 100, 452, "unmute_icon", "mute_icon", true);
		addButton("twitter_icon", 593, 452, "twitter_icon", "twitter_icon", false);
		addButton("facebook_icon", 657, 452, "facebook_icon", "facebook_icon", false);
		
		
	}
	
	
	@Override
	public void dispose() {
		mMenuStage.clear();
		super.dispose();
	}
	
	@Override
	public void render(float delta) {
		mMenuStage.act(delta);
		mMenuStage.draw();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		boolean touched = mMenuStage.touchDown(x, y, pointer, button);
		return touched;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		boolean touched = mMenuStage.touchUp(x, y, pointer, button);
		if(touched) {
			Actor hitActor = mMenuStage.getLastTouchedChild();
			if(hitActor == null) {
				return touched;
			}
			
			if(hitActor.name.startsWith("start")) {
				getScreenMgr().pushScreen(new GamePlayScreen(getScreenMgr()));
			}
			else if(hitActor.name.startsWith("level")){
				getScreenMgr().pushScreen(new LevelSelectScreen(getScreenMgr()));
			}
			else if(hitActor.name.startsWith("credits")){
				getScreenMgr().pushScreen(new CreditsScreen(getScreenMgr()));
			}
			else if(hitActor.name.startsWith("exit")){
				Gdx.app.exit();
			}
		}
		return touched;
	}

	
	private void addButton(String name, float x, float y, String upRegion, String downRegion, boolean toggle) {
		float dy = getScreenMgr().getVirtualHeight() - y;
		if(toggle) {
			ToggleButton btn = new ToggleButton(name, x, dy, mAssets.assets.getTextureRegion(upRegion), mAssets.assets.getTextureRegion(downRegion));
			btn.setOnClickSound(mAssets.mClickSound);
			mMenuStage.addActor(btn);
		}else{
			PushButton btn = new PushButton(name, x, dy, mAssets.assets.getTextureRegion(upRegion), mAssets.assets.getTextureRegion(downRegion));
			btn.setOnClickSound(mAssets.mClickSound);
			mMenuStage.addActor(btn);
		}
		
		//mButtons.add(btn);
	}
}
