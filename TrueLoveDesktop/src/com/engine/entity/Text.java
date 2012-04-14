package com.engine.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Text extends BasicEntity {
	
	private BitmapFont font;
	private String text;
	
	public Text() {
		 
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		//font.draw(batch, text, x, y);
	}

}
