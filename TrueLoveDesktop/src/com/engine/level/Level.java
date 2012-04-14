package com.engine.level;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.engine.entity.BasicEntity;

public class Level {

	private String name;
	

	/**
	 * A Level contains several Layers. Each Layer contains several Items.
	 */
	private ArrayList<Layer> layers;

	
	
	public Level() {
		layers = new ArrayList<Layer>();
	}
	
	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}


	public ArrayList<Layer> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}

	public void addLayer(Layer layer) {
		this.layers.add(layer);
	}
	
	public void removeLayer(Layer layer) {
		this.layers.remove(layer);
	}

	
	public BasicEntity getGameObjectByName(String name) {
		for (Layer layer : layers) {
			for(BasicEntity gameObject : layer.getGameObjects()) {
				if(gameObject.getName().compareTo(name) == 0) {
					return gameObject;
				}
			}
		}
		return null;
	}


	public Layer getLayerByName(String name) {
		for (Layer layer : layers) {
			if (layer.getName().compareTo(name) == 0) {
				return layer;
			}
		}
		return null;
	}
	
	
	public void addLevelToStage(Stage stage) {
		for (Layer layer : layers) {
			for(BasicEntity gameObject : layer.getGameObjects()) {
				stage.addActor(gameObject);
			}
		}
	}
	
	public void dispose() {
		for(Layer layer : layers) {
			layer.dispose();
		}
		layers.clear();
		layers = null;
	}

}
