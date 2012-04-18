package com.engine;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class GameScreen implements Screen, InputProcessor {
	
	private final ScreenManager mScreenMgr;
	private final ScreenAssets mAssets;
	private World mWorld;
	private boolean mPaused;
	private boolean mUsePhysics;
	private int mVelocityIterations;
	private int mPositionIterations;
	

	public GameScreen(ScreenManager manager) {
		mScreenMgr = manager;
		mAssets = new ScreenAssets();
		mWorld = null;
		mPaused = false;
		mUsePhysics = false;
	}
	
	/**
	 * initialize the physics world
	 * @param gravity
	 * @param doSleep
	 * @param velocityIterations
	 * @param positionIterations
	 */
	public void initPhysicsWorld(final Vector2 gravity, boolean doSleep, int velocityIterations, int positionIterations) {
		if(mWorld == null) {
			mWorld = new World(gravity, doSleep);
			mVelocityIterations = velocityIterations;
			mPositionIterations = positionIterations;
			mUsePhysics = true;
		}
	}

	/**
	 * called when this game screen is going to destroy
	 * dispose physics world
	 * dispose screen assets
	 */
	@Override
	public void dispose() {
		if(mWorld != null) {
			mWorld.dispose();
		}
		mAssets.dispose();
	}

	@Override
	public final void hide() {
	}

	/**
	 * called when this screen of the game is going to pause state
	 */
	@Override
	public void pause() {
		mPaused = true;
	}

	/**
	 * called on every frame
	 * update the physics world (if uses)
	 */
	@Override
	public void render(float delta) {
		if(mWorld != null) {
			mWorld.step(delta, mVelocityIterations, mPositionIterations);
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	/**
	 * called when screen is resumed
	 */
	@Override
	public void resume() {
		mPaused = false;
	}

	/**
	 * called when screen 
	 */
	@Override
	public void show() {
	}
	
	
	@Override
	public boolean keyDown(int keyCode) {
		return false;
	}

	@Override
	public boolean keyTyped(char keyCode) {
		return false;
	}

	@Override
	public boolean keyUp(int keyCode) {
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}


	/**
	 * Get Screen Manager
	 * @return
	 */
	public final ScreenManager getScreenMgr() { 
		return mScreenMgr;	
	}
	
	/**
	 * Get Physics World
	 * @return
	 */
	public final World getWorld() {
		return mWorld;
	}
	
	/**
	 * Get Assets related to current screen
	 * @return
	 */
	public final ScreenAssets getAssets() {
		return mAssets;
	}
	
	public boolean isPaused() { 
		return mPaused; 
	}

	public boolean isUsesPhysics() {
		return mUsePhysics;
	}

}
