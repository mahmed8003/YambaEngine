package com.truelove.screens;

import com.badlogic.gdx.Gdx;
import com.engine.GameScreen;
import com.engine.ScreenManager;

public class MainMenuScreen extends GameScreen {

	public MainMenuScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	public void show() {
		Gdx.app.log("Log 1", "show called");
	}
	
	
	@Override
	public void dispose() {
		Gdx.app.log("Log 1", "dispose called");
	}
	
	@Override
	public void render(float delta) {
		//Gdx.app.log("Log 1", Float.toString(delta));
	}

}
