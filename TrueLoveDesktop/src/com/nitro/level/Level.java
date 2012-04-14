package com.nitro.level;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


public class Level {

	public ArrayList<Layer> Layers = new ArrayList<Layer>();
	
	public static Level loadFromFile(String file) {
		FileHandle handle = Gdx.files.internal("data/level/test.json");
		String json = handle.readString();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Level level = mapper.readValue(json, Level.class);
			Gdx.app.log("JSON converted : ", mapper.writeValueAsString(level));
			return level;
		} catch (Exception e) {
			Gdx.app.log("Exception : ", e.getMessage());
		}
		return null;
	}
}
