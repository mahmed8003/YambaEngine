package com.truelove;

import com.engine.ScreenManager;
import com.truelove.screens.MainMenuScreen;

public class TrueLoveGame extends ScreenManager {

	@Override
	public void create() {
		super.create();
		Assets assets = Assets.getInstance();
		assets.initCommonResources();
		
		pushScreen(new MainMenuScreen(this));
		//pushScreen(new GamePlayScreen(this));
	}

}
