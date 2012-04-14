package com.engine;

import java.io.Serializable;

public class Settings implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Settings mInstance = null;
	private boolean soundEnabled = true;
	public static final float PIXEL_TO_METER_RATIO = 1.0f;
	
	private Settings() {
	}
	
	public static Settings getInstance(){
		if(mInstance == null){
			mInstance = new Settings();
		}
		return mInstance;
	}

	public boolean isSoundEnabled() {
		return soundEnabled;
	}

	public void setSoundEnabled(boolean soundEnabled) {
		this.soundEnabled = soundEnabled;
	}
}
