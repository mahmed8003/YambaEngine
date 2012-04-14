package com.truelove;

import com.engine.ScreenManager;
import com.truelove.screens.GamePlayScreen;

public class TrueLoveGame extends ScreenManager {


	@Override
	public void create() {
		//pushScreen(new MainMenuScreen(this));
		pushScreen(new GamePlayScreen(this));
	}

}
