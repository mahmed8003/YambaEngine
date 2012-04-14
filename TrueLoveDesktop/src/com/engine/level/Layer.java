package com.engine.level;

import java.util.ArrayList;

import com.engine.entity.BasicEntity;

public class Layer {

	/**
	 * The name of the layer.
	 */
	private String name;

	/**
	 * Should this layer be visible?
	 */
	private boolean visible;

	/**
	 * The list of the items in this layer.
	 */
	private ArrayList<BasicEntity> gameObjects;

	
	public Layer() {
		gameObjects = new ArrayList<BasicEntity>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		for(BasicEntity obj : gameObjects) {
			obj.setVisible(visible);
		}
	}
	
	public BasicEntity getGameObjectByName(String name) {
		for(BasicEntity gameObject : gameObjects) {
			if(gameObject.getName().compareTo(name) == 0) {
				return gameObject;
			}
		}
		return null;
	}

	public ArrayList<BasicEntity> getGameObjects() {
		return gameObjects;
	}

	public void setGameObjects(ArrayList<BasicEntity> gameObjects) {
		this.gameObjects = gameObjects;
	}
	
	public void addGameObject(BasicEntity gameObject) {
		this.gameObjects.add(gameObject);
	}

	
	public void dispose() {
		gameObjects.clear();
		gameObjects = null;
	}

}
