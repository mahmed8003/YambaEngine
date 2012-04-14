package com.truelove.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.engine.GameScreen;
import com.engine.ScreenManager;
import com.engine.Settings;
import com.truelove.Assets;
import com.truelove.TrueLoveGame;

public class MainMenu extends GameScreen {
	
	public final static String TAG = "MainMenu"; 
	
	private Sprite mbackgroundSprite;
	private Sprite mchapterSelectionBtnSprite;
	private Sprite mcreditsBtnSprite;
	private Sprite mexitBtnSprite;
	private Sprite mfacebookBtnSprite;
	private Sprite mgameTitleSprite;
	private Sprite mmoreGamesBtnSprite;
	private Sprite mmuteBtnSprite;
	private Sprite mstartBtnSprite;
	private Sprite munMuteBtnSprite;
	
	private Music mBackgroundMusic;
	private SpriteBatch spriteBatch;
	private OrthographicCamera guiCam;
	private Vector3 touchPoint;
	
	Assets assets;
	
	public MainMenu(ScreenManager manager) {
		super(manager);
		touchPoint = new Vector3();
		assets = Assets.getInstance();
		//guiCam = CameraHelper.createCamera2(ViewportMode.STRETCH_TO_SCREEN, assets.VIRTUAL_WIDTH, assets.VIRTUAL_HEIGHT, 1.5f);
		spriteBatch = new SpriteBatch();
		//spriteBatch.setProjectionMatrix(guiCam.combined);
	}
	
	@Override
	public void show() {
		assets.loadAudioResources();
		assets.playBackgroundMusic();
		
		assets.loadMenuResources();
		
		guiCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		guiCam.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		mbackgroundSprite = new Sprite(assets.background);
		mchapterSelectionBtnSprite = new Sprite(assets.chapterSelectionBtn);
		mcreditsBtnSprite = new Sprite(assets.creditsBtn);
		mexitBtnSprite = new Sprite(assets.exitBtn);
		mfacebookBtnSprite = new Sprite(assets.facebookBtn);
		mgameTitleSprite = new Sprite(assets.gameTitle);
		mmoreGamesBtnSprite = new Sprite(assets.moreGamesBtn);
		mmuteBtnSprite = new Sprite(assets.muteBtn);
		mstartBtnSprite = new Sprite(assets.startBtn);
		munMuteBtnSprite = new Sprite(assets.unMuteBtn);
		
		
		mchapterSelectionBtnSprite.setPosition(148, 320 - 200);
		mcreditsBtnSprite.setPosition(195, 320 - 240);
		mexitBtnSprite.setPosition(5, 320 - 318);
		mfacebookBtnSprite.setPosition(390, 320 - 318);
		mgameTitleSprite.setPosition(98, 320 - 106);
		mmoreGamesBtnSprite.setPosition(165, 320 - 292);
		mmuteBtnSprite.setPosition(1000, 1000);
		mstartBtnSprite.setPosition(205, 320 - 146);
		munMuteBtnSprite.setPosition(44, 320 - 318);
	}
	
	@Override
	public void render(float delta) {
		guiCam.update();
		spriteBatch.begin();
		
		mbackgroundSprite.draw(spriteBatch);
		mchapterSelectionBtnSprite.draw(spriteBatch);
		mcreditsBtnSprite.draw(spriteBatch);
		mexitBtnSprite.draw(spriteBatch);
		mfacebookBtnSprite.draw(spriteBatch);
		mgameTitleSprite.draw(spriteBatch);
		mmoreGamesBtnSprite.draw(spriteBatch);
		mmuteBtnSprite.draw(spriteBatch);
		mstartBtnSprite.draw(spriteBatch);
		munMuteBtnSprite.draw(spriteBatch);
		
		spriteBatch.end();
		
		if(Gdx.input.justTouched()) {
			touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			guiCam.project(touchPoint);
			
			if(mstartBtnSprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
				assets.playClickSound();
			}
			else if(mchapterSelectionBtnSprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
				assets.playClickSound();
			}
			else if(mcreditsBtnSprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
				assets.playClickSound();
			}
			else if(mmoreGamesBtnSprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
				assets.playClickSound();
			}
			else if(mfacebookBtnSprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
				assets.playClickSound();
			}
			else if(mexitBtnSprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
				assets.playClickSound();
			}
			else if(mmuteBtnSprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y) || munMuteBtnSprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
				assets.playClickSound();
				
				boolean temp = Settings.getInstance().isSoundEnabled();
				Settings.getInstance().setSoundEnabled(!temp);
				assets.playBackgroundMusic();
				if(temp) {
					munMuteBtnSprite.setPosition(1000, 1000);
					mmuteBtnSprite.setPosition(44, 320 - 318);
				}else{
					munMuteBtnSprite.setPosition(44, 320 - 318);
					mmuteBtnSprite.setPosition(1000, 1000);
				}
				
			}
		}
	}
	
	@Override
	public void dispose() {
	}

}
