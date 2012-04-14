package com.engine;

import java.util.Stack;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

public abstract class ScreenManager implements ApplicationListener {

	private final Stack<GameScreen> mScreens;
	private GameScreen mCurrentScreen;

	public ScreenManager() {
		mScreens = new Stack<GameScreen>();
	}


	@Override
	public void dispose() {
		clearScreens();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		if (mCurrentScreen != null)
			mCurrentScreen.render(Gdx.graphics.getDeltaTime());

	}

	@Override
	public void resize(int arg0, int arg1) {

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
	
	public GameScreen getCurrentScreen() {
		return mCurrentScreen;
	}

	public void changeScreen(GameScreen screen) {
		mCurrentScreen = screen;

		// destroy the current state
		if (!mScreens.empty())
			mScreens.pop().dispose();

		// store and init the new state
		mScreens.push(screen);
		mScreens.peek().show();

	}

	public void pushScreen(GameScreen screen) {
		mCurrentScreen = screen;

		// pause current state
		if (!mScreens.empty())
			mScreens.peek().pause();

		// store and init the new state
		mScreens.push(screen);
		mScreens.peek().show();
	}

	public void popScreen() {
		// cleanup the current state
		if (!mScreens.empty())
			mScreens.pop().dispose();

		// resume previous state
		if (!mScreens.empty()) {
			mCurrentScreen = mScreens.peek();
			mScreens.peek().resume();
		}
	}
	
	/**
	 * destroy all the screens in a game
	 */
	public final void clearScreens()
	{
		while (!mScreens.empty())
			popScreen();

		mCurrentScreen = null;
	}

}
