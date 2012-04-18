package com.truelove.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.engine.GameScreen;
import com.engine.ScreenManager;
import com.engine.entity.PushButton;
import com.engine.entity.Sprite;
import com.truelove.Assets;

public class LevelSelectScreen extends GameScreen {

	public final static String TAG = "LevelSelectScreen";
	private Stage mMenuStage;
	private final Assets mAssets;
	
	public LevelSelectScreen(ScreenManager manager) {
		super(manager);
		mAssets = Assets.getInstance();
		mMenuStage = new Stage(getScreenMgr().getVirtualWidth(), getScreenMgr().getVirtualHeight(), true);
	}
	
	@Override
	public void show() {
		/* setting up background */
		Sprite sprite;
		sprite = new Sprite("background", mAssets.backgroundRegion);
		mMenuStage.addActor(sprite);
		
		/* creating screen contents */
		addButton("forward", 707, 258, "forward", "forward");
		addButton("backward", 26, 258, "backward", "backward");
		addButton("back_btn", 6, 40, "back_up", "back_down");
	}
	
	@Override
	public void render(float delta) {
		mMenuStage.act(delta);
		mMenuStage.draw();
	}
	
	@Override
	public void dispose() {
		mMenuStage.dispose();
		super.dispose();
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

}
