package com.truelove;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.engine.Settings;

public class Assets {
	
	private static Assets mInstance = null;
	
	private HashMap<String, TextureRegion> mTextureRegions;
	private ArrayList<Texture> mTextures;
	
	private Sound mClickSound = null;
	private Music mBackgroundMusic = null;
	
	public static final float VIRTUAL_WIDTH = 480.0f;
	public static final float VIRTUAL_HEIGHT = 320.0f;

	public TextureRegion background;
	public TextureRegion chapterSelectionBtn;
	public TextureRegion creditsBtn;
	public TextureRegion exitBtn;
	public TextureRegion facebookBtn;
	public TextureRegion gameTitle;
	public TextureRegion moreGamesBtn;
	public TextureRegion muteBtn;
	public TextureRegion startBtn;
	public TextureRegion unMuteBtn;
	
	public TextureRegion boyRegion;
	public TextureRegion girlRegion;
	
	public TextureRegion animButton;
	
	public TextureRegion carBody;
	public TextureRegion carWheel;
	
	public TextureRegion box;
	public TextureRegion cir;
	
	/*
	 * HUD Texture and sprite
	 */
	private Texture hudTexture;
	public TextureRegion exit;
	public TextureRegion left;
	public TextureRegion reset;
	public TextureRegion right;
	public TextureRegion sound_off;
	public TextureRegion sound_on;
	public TextureRegion swap;
	public TextureRegion up;
	

	private Texture menuTexture;
	private Texture playerTexture;
	
	public void loadHudResources() {
		hudTexture = new Texture(Gdx.files.internal("data/gfx/menu/hud_texture.png"));
		hudTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		exit = new TextureRegion(hudTexture, 84, 149, 62, 62);
		left = new TextureRegion(hudTexture, 174, 0, 80, 83);
		reset = new TextureRegion(hudTexture, 148, 90, 60, 58);
		right = new TextureRegion(hudTexture, 87, 0, 86, 89);
		sound_off = new TextureRegion(hudTexture, 84, 91, 63, 57);
		sound_on = new TextureRegion(hudTexture, 0, 175, 73, 59);
		swap = new TextureRegion(hudTexture, 0, 0, 86, 90);
		up = new TextureRegion(hudTexture, 0, 91, 83, 83);
		
		playerTexture = new Texture(Gdx.files.internal("data/gfx/psheet.png"));
		boyRegion = new TextureRegion(playerTexture, 0, 0, 128, 192);//, 0, 0, 252, 130);
		girlRegion = new TextureRegion(playerTexture, 129, 0, 128, 198);
		
		Texture t = new Texture(Gdx.files.internal("data/gfx/btn.png"));
		animButton = new TextureRegion(t);
		
		//
		loadBoxyResources();
		
		//
		loadCarResources();
	}

	public void loadMenuResources() {
		menuTexture = new Texture(Gdx.files.internal("data/gfx/menu/menu_texture.png"));

		background = new TextureRegion(menuTexture, 0, 0, 480, 320);
		chapterSelectionBtn = new TextureRegion(menuTexture, 0, 426, 192, 59);
		creditsBtn = new TextureRegion(menuTexture, 193, 462, 80, 44);
		exitBtn = new TextureRegion(menuTexture, 370, 368, 40, 35);
		facebookBtn = new TextureRegion(menuTexture, 193, 426, 86, 35);
		gameTitle = new TextureRegion(menuTexture, 0, 321, 293, 104);
		moreGamesBtn = new TextureRegion(menuTexture, 294, 321, 151, 46);
		muteBtn = new TextureRegion(menuTexture, 274, 462, 53, 31);
		startBtn = new TextureRegion(menuTexture, 294, 368, 75, 44);
		unMuteBtn = new TextureRegion(menuTexture, 280, 426, 57, 31);
		
	}
	
	public void unloadMenuResources() {
		menuTexture.dispose();
	}
	
	private void loadCarResources() {
		Texture t = new Texture(Gdx.files.internal("data/gfx/car.png"));
		carBody = new TextureRegion(t, 0, 0, 150, 50);
		carWheel = new TextureRegion(t, 0, 51, 53, 53);
	}
	
	private void loadBoxyResources() {
		Texture t = new Texture(Gdx.files.internal("data/gfx/boxi.png"));
		box = new TextureRegion(t, 0, 0, 48, 48);
		cir = new TextureRegion(t, 49, 0, 48, 48);
	}
	
	public void playClickSound() {
		if(Settings.getInstance().isSoundEnabled()) {
			mClickSound.play();
		}else{
			//mClickSound.stop();
		}
	}
	
	public void playBackgroundMusic() {
		if(Settings.getInstance().isSoundEnabled()) {
			mBackgroundMusic.play();
			mBackgroundMusic.setLooping(true);
		}else{
			mBackgroundMusic.stop();
		}
	}
	
	public void loadAudioResources() {
		mBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/true_love_soundtrack.ogg"));
		mClickSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/click.ogg"));
	}
	
	public void loadTextureRegions(String textureFile, String regionFile) {
		Texture texture = new Texture(Gdx.files.internal(textureFile));
		mTextures.add(texture);
		
		FileHandle handle = Gdx.files.internal(regionFile);
		String regions = handle.readString();
		StringTokenizer tokLine = new StringTokenizer(regions, System.getProperty("line.separator"));
		while (tokLine.hasMoreTokens()) {
			String line = (String) tokLine.nextElement();
			String[] lns = line.split(" = ");
			String regionName = lns[0];
			
			String[] attribs = lns[1].split(" ");
			
			int x = Integer.parseInt(attribs[0]);
			int y = Integer.parseInt(attribs[1]);
			int width = Integer.parseInt(attribs[2]);
			int height = Integer.parseInt(attribs[3]);
			
			Gdx.app.log("Assets : ", regionName + x + y + width+height );
			
			TextureRegion region = new TextureRegion(texture, x, y, width, height);
			mTextureRegions.put(regionName, region);
		}
		
	}
	public TextureRegion getTextureRegion(String name) {
		return mTextureRegions.get(name);
	}
	
	public void addTextureRegion(String name, TextureRegion region) {
		mTextureRegions.put(name, region);
	}
	
	public void removeTextureRegion(String name) {
		mTextureRegions.remove(name);
	}
	
	public void clearTextureRegions() {
		for(Texture texture : mTextures) {
			texture.dispose();
		}
		mTextureRegions.clear();
		mTextures.clear();
	}
	
	public final static Assets getInstance() {
		if(mInstance == null) {
			mInstance = new Assets();
		}
		return mInstance;
	}
	
	private Assets() {
		mTextureRegions = new HashMap<String, TextureRegion>();
		mTextures = new ArrayList<Texture>();
	}
}
