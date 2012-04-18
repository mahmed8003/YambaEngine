package com.engine;

import java.util.Stack;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class ScreenManager implements ApplicationListener {

	private final int mVirtualWidth = 800;
    private final int mVirtualHeight = 480;
    private final float mAspectRatio;
    private Rectangle viewport;
    
    private float mScreenWidth;
    private float mScreenHeight;
    
	private final Stack<GameScreen> mScreens;
	private GameScreen mCurrentScreen;

	public ScreenManager() {
		mScreens = new Stack<GameScreen>();
		mAspectRatio = (float)mVirtualWidth/(float)mVirtualHeight;
	}
	
	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);
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
		// set viewport
        //Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
		Gdx.gl.glViewport(0, 0, (int)getScreenWidth(), (int)getScreenHeight());

        // clear previous frame
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        // render current screen
		if (mCurrentScreen != null)
			mCurrentScreen.render(Gdx.graphics.getDeltaTime());

	}

	@Override
	public void resize(int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
		
		// calculate new viewport
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        if(aspectRatio > mAspectRatio)
        {
            scale = (float)height/(float)mVirtualHeight;
            crop.x = (width - mVirtualWidth*scale)/2f;
        }
        else if(aspectRatio < mAspectRatio)
        {
            scale = (float)width/(float)mVirtualWidth;
            crop.y = (height - mVirtualHeight*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)mVirtualWidth;
        }

        float w = (float)mVirtualWidth*scale;
        float h = (float)mVirtualHeight*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
	}

	@Override
	public void resume() {
	}
	
	public float getVirtualWidth() {
		return mVirtualWidth;
	}
	
	public float getVirtualHeight() {
		return mVirtualHeight;
	}
	
	public float getScreenWidth() {
		return mScreenWidth;
	}
	
	public float getScreenHeight() {
		return mScreenHeight;
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

		/* setting up input processor class */
		Gdx.input.setInputProcessor(mCurrentScreen);
	}

	public void pushScreen(GameScreen screen) {
		mCurrentScreen = screen;

		// pause current state
		if (!mScreens.empty())
			mScreens.peek().pause();

		// store and init the new state
		mScreens.push(screen);
		mScreens.peek().show();
		
		/* setting up input processor class */
		Gdx.input.setInputProcessor(mCurrentScreen);
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
		
		/* setting up input processor class */
		Gdx.input.setInputProcessor(mCurrentScreen);
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
