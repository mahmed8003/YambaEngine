package com.engine;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.engine.entity.Boxi;
import com.engine.entity.Platform;
import com.engine.entity.Sprite;
import com.truelove.screens.GamePlayScreen;

public class LevelLoader {
	
	public static final String TAG = "LevelLoader";
	PhysicsEditorLoader mPhysicsEditorLoader;
	HashMap<String, FixtureDef> mFixtures;
	private final GamePlayScreen mGamePlayScreen;
	
	public LevelLoader(GamePlayScreen screen) {
		mGamePlayScreen = screen;
		mFixtures = new HashMap<String, FixtureDef>();
		
	}
	
	public void loadLevel(String levelFile) {
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			dbf.setValidating(false);
			dbf.setIgnoringComments(false);
			dbf.setIgnoringElementContentWhitespace(true);
			dbf.setNamespaceAware(true);

			DocumentBuilder db = null;
			db = dbf.newDocumentBuilder();

			Document document = db.parse(Gdx.files.internal(levelFile).read());
			Element root = document.getDocumentElement();
			parseXML(root);
		} catch (Exception e) {
			Gdx.app.log(TAG, e.toString());
		}
		
	}
	
	public void dispose() {
		mPhysicsEditorLoader.dispose();
		mFixtures.clear();
	}
	
	private void parseXML(Element root) {
		loadResources(root);
		loadFixtures(root);
		loadPlatforms(root);
		loadBoxies(root);
	}
	
	private void loadResources(Element root) {
		NodeList list = root.getElementsByTagName("texture");
		for(int i = 0; i < list.getLength(); i++){
			Element element = (Element) list.item(i);
			
			String tex_file = element.getAttribute("tex_file");
			String reg_file = element.getAttribute("reg_file");
			
			mGamePlayScreen.getAssets().loadTextureRegions(tex_file, reg_file);
			
		}
		
		// loading physics file
		list = root.getElementsByTagName("physics");
		if(list.getLength() == 1) {
			Element element = (Element) list.item(0);
			String file = element.getAttribute("file");
			mPhysicsEditorLoader = new PhysicsEditorLoader(file, mGamePlayScreen.getWorld());	
		}
		
	}
	
	private void loadFixtures(Element root) {
		NodeList list = root.getElementsByTagName("fixture");
		for(int i = 0; i < list.getLength(); i++){
			Element element = (Element) list.item(i);
			
			String id = element.getAttribute("id");
			float density = Float.parseFloat(element.getAttribute("density"));
			float elasticity = Float.parseFloat(element.getAttribute("elasticity"));
			float friction = Float.parseFloat(element.getAttribute("friction"));
			
			FixtureDef def = PhysicsFactory.createFixtureDef(density, elasticity, friction, false);
			mFixtures.put(id, def);
		}
	}

	private void loadPlatforms(Element root) {
		NodeList list = root.getElementsByTagName("platform");
		for(int i = 0; i < list.getLength(); i++){
			Element element = (Element) list.item(i);
			
			String name = element.getAttribute("name");
			float x = Float.parseFloat(element.getAttribute("x"));
			float y = Float.parseFloat(element.getAttribute("y"));
			float rotation = Float.parseFloat(element.getAttribute("rotation"));
			float scale_x = Float.parseFloat(element.getAttribute("scale_x"));
			float scale_y = Float.parseFloat(element.getAttribute("scale_y"));
			String fixture_id = element.getAttribute("fixture_id");
			String body_type = element.getAttribute("body_type");
			BodyType bodyType = BodyType.StaticBody;
			if(body_type.equals("dynamic") == true) {
				bodyType = BodyType.DynamicBody;
			}
			
			Sprite sprite = new Sprite(name, x, y, mGamePlayScreen.getAssets().getTextureRegion(name));
			sprite.setRotation(rotation);
			sprite.setScale(scale_x, scale_y);
			Body body = mPhysicsEditorLoader.createBody(name, sprite, bodyType, mFixtures.get(fixture_id), Settings.PIXEL_TO_METER_RATIO);
			Platform platform = new Platform(name, sprite, body);
			mGamePlayScreen.mPlatforms.add(platform);
		}
	}
	
	private void loadBoxies(Element root) {
		NodeList list = root.getElementsByTagName("boxi");
		for(int i = 0; i < list.getLength(); i++){
			Element element = (Element) list.item(i);
			
			String name = element.getAttribute("name");
			float x = Float.parseFloat(element.getAttribute("x"));
			float y = Float.parseFloat(element.getAttribute("y"));
			float rotation = Float.parseFloat(element.getAttribute("rotation"));
			float scale_x = Float.parseFloat(element.getAttribute("scale_x"));
			float scale_y = Float.parseFloat(element.getAttribute("scale_y"));
			String color = element.getAttribute("color");
			String convertable = element.getAttribute("convertable");
			String face = element.getAttribute("face");
			String fixture_id = element.getAttribute("fixture_id");
			
			int con, fac;
			
			if(convertable.equalsIgnoreCase("yes")) {
				con = Boxi.CONVERTIBLE;
			}else{
				con =Boxi.NONCONVERTABLE;
			}
			
			if(face.equalsIgnoreCase("box")) {
				fac = Boxi.BOX;
			}else{
				fac = Boxi.CIRCLE;
			}
			
			Boxi boxi = new Boxi(mGamePlayScreen.getWorld(), name, x, y, null, fac, con);
			mGamePlayScreen.mBoxis.add(boxi);
		}
	}
}
