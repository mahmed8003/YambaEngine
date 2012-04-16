package com.engine;

/**
 * PhysicsEditor Importer Library
 *
 * Usage:
 * - Create an instance of this class
 * - Use the "open" method to load an XML file from PhysicsEditor
 * - Invoke "createBody" to create bodies from library.
 *
 * by Adrian Nilsson (ade at ade dot se)
 * BIG IRON GAMES (bigirongames.org)
 * Date: 2011-08-30
 * Time: 11:51
 */



import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.engine.entity.BasicEntity;

public class PhysicsEditorShapeLibrary {
    private HashMap<String, BodyTemplate> shapes = new HashMap<String, BodyTemplate>();
    private float pixelToMeterRatio;

    public PhysicsEditorShapeLibrary(float pixelToMeterRatio) {
        this.pixelToMeterRatio = pixelToMeterRatio;
    }

    /**
     * If you wish, you may access the template data and create custom bodies.
     * Be advised that vertex positions are pre-adjusted for Box2D coordinates (pixel to meter ratio).
     * @param key
     * @return
     */
    public BodyTemplate get(String key) {
        return this.shapes.get(key);
    }

    /**
     * Create a Box2D Body with a shape from the library.
     * @param name name of the shape (usually filename of the image, without extension)
     * @param entity the AndEngine shape you will be associating the body with (physics connector is not created here).
     * @param world the AndEngine Box2D physics world object.
     * @return
     */
    public Body createBody(String name, BasicEntity entity, World world) {
        BodyTemplate bodyTemplate = this.shapes.get(name);

        final BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyTemplate.isDynamic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;

		/*final float[] sceneCenterCoordinates = entity.getSceneCenterCoordinates();
		boxBodyDef.position.x = sceneCenterCoordinates[Constants.VERTEX_INDEX_X] / this.pixelToMeterRatio;
		boxBodyDef.position.y = sceneCenterCoordinates[Constants.VERTEX_INDEX_Y] / this.pixelToMeterRatio;*/
		final float halfWidth = entity.getWidthScaled() * 0.5f / pixelToMeterRatio;
		final float halfHeight = entity.getHeightScaled() * 0.5f / pixelToMeterRatio;
		
		boxBodyDef.position.x = entity.x / this.pixelToMeterRatio + halfWidth;
		boxBodyDef.position.y = entity.y / this.pixelToMeterRatio + halfHeight;

		final Body boxBody = world.createBody(boxBodyDef);

        for(FixtureTemplate fixtureTemplate : bodyTemplate.fixtureTemplates) {
            for(int i = 0; i < fixtureTemplate.polygons.size(); i++) {
                final PolygonShape polygonShape = new PolygonShape();
                final FixtureDef fixtureDef = fixtureTemplate.fixtureDef;

                //Vector2[] vertices = vectorList.toArray(new Vector2[vectorList.size()]);
                int size = fixtureTemplate.polygons.get(i).vertices.size();
                polygonShape.set(fixtureTemplate.polygons.get(i).vertices.toArray(new Vector2[size]));

                fixtureDef.shape = polygonShape;
                boxBody.createFixture(fixtureDef);
                polygonShape.dispose();
            }
        }

        return boxBody;
    }

    
    public void open(String file) {
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			dbf.setValidating(false);
			dbf.setIgnoringComments(false);
			dbf.setIgnoringElementContentWhitespace(true);
			dbf.setNamespaceAware(true);

			DocumentBuilder db = null;
			db = dbf.newDocumentBuilder();

			Document document = db.parse(Gdx.files.internal(file).read());
			Element root = document.getDocumentElement();
			ShapeLoader loader = new ShapeLoader(shapes, pixelToMeterRatio);
			loader.loadBodyTemplates(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

    protected static class ShapeLoader {
        public static final String TAG_BODY = "body";
        public static final String TAG_FIXTURE = "fixture";
        public static final String TAG_POLYGON = "polygon";
        public static final String TAG_VERTEX = "vertex";
        public static final String TAG_NAME = "name";
        public static final String TAG_X = "x";
        public static final String TAG_Y = "y";
        public static final String TAG_DENSITY = "density";
        public static final String TAG_RESTITUTION = "restitution";
        public static final String TAG_FRICTION = "friction";
        public static final String TAG_FILTER_CATEGORY_BITS = "filter_categoryBits";
        public static final String TAG_FILTER_GROUP_INDEX = "filter_groupIndex";
        public static final String TAG_FILTER_MASK_BITS = "filter_maskBits";
        public static final String TAG_ISDYNAMIC = "dynamic";
        public static final String TAG_ISSENSOR = "isSensor";

        private float pixelToMeterRatio;
        private HashMap<String, BodyTemplate> shapes;
        private BodyTemplate currentBody;
        private FixtureTemplate currentFixtureTemplate;
        private PolygonTemplate currentPolygonTemplate;

        protected ShapeLoader(HashMap<String, BodyTemplate> shapes, float pixelToMeterRatio) {
            this.shapes = shapes;
            this.pixelToMeterRatio = pixelToMeterRatio;
        }
        
        /*
         * 
         */
        private void loadBodyTemplates(Element element) {
        	NodeList bList = element.getElementsByTagName(TAG_BODY);
        	for(int i = 0; i < bList.getLength(); i++) {
        		Element e = (Element) bList.item(i);
        		currentBody = new BodyTemplate();
        		currentBody.name = e.getAttribute(TAG_NAME);
        		currentBody.isDynamic = e.getAttribute(TAG_ISDYNAMIC).equalsIgnoreCase("true");
        		loadFixtureTemplates(e);
        		shapes.put(currentBody.name, currentBody);
        	}
        }
        
        void loadFixtureTemplates(Element element) {
        	NodeList fList = element.getElementsByTagName(TAG_FIXTURE);
        	for(int i = 0; i < fList.getLength(); i++) {
        		Element e = (Element) fList.item(i);
        		float restitution = Float.parseFloat(e.getAttribute(TAG_RESTITUTION));
                float friction = Float.parseFloat(e.getAttribute(TAG_FRICTION));
                float density = Float.parseFloat(e.getAttribute(TAG_DENSITY));
                short category = parseShort(e.getAttribute(TAG_FILTER_CATEGORY_BITS));
                short groupIndex = parseShort(e.getAttribute(TAG_FILTER_GROUP_INDEX));
                short maskBits = parseShort(e.getAttribute(TAG_FILTER_MASK_BITS));
                boolean isSensor = e.getAttribute(TAG_ISSENSOR).equalsIgnoreCase("true");
                
                currentFixtureTemplate = new FixtureTemplate();
                currentFixtureTemplate.fixtureDef = PhysicsFactory.createFixtureDef(density, restitution, friction, isSensor, category, maskBits, groupIndex);
                loadPolygonTemplate(e);
        		currentBody.fixtureTemplates.add(currentFixtureTemplate);
        	}
        }
        
        void loadPolygonTemplate(Element element) {
        	NodeList pList = element.getElementsByTagName(TAG_POLYGON);
        	for(int i = 0; i < pList.getLength(); i++) {
        		Element e = (Element) pList.item(i);
        		currentPolygonTemplate = new PolygonTemplate();
        		loadVertexs(e);
        		currentFixtureTemplate.polygons.add(currentPolygonTemplate);
        	}
        	
        }
        
        void loadVertexs(Element element) {
        	NodeList vList = element.getElementsByTagName(TAG_VERTEX);
        	for(int i = 0; i < vList.getLength(); i++) {
        		Element e = (Element) vList.item(i);
        		float x = Float.parseFloat(e.getAttribute(TAG_X));
        		float y = Float.parseFloat(e.getAttribute(TAG_Y));
        		Vector2 vec = new Vector2(x / pixelToMeterRatio, y / pixelToMeterRatio);
        		currentPolygonTemplate.vertices.add(vec);
        	}
        }
        
        
    }
    private static short parseShort(String val) {
        int intVal = Integer.parseInt(val);
        short ret = (short)(intVal & 65535);
        return (short)intVal;
    }
    
    private static class BodyTemplate {
        public String name;
        public boolean isDynamic = true;
        public ArrayList<FixtureTemplate> fixtureTemplates;
        
        public BodyTemplate() {
			fixtureTemplates = new ArrayList<PhysicsEditorShapeLibrary.FixtureTemplate>();
		}
    }
    
    
    private static class FixtureTemplate {
    	public FixtureDef fixtureDef;
        public ArrayList<PolygonTemplate> polygons;
        
        public FixtureTemplate() {
			polygons = new ArrayList<PhysicsEditorShapeLibrary.PolygonTemplate>();
		}
    }
    
    
    private static class PolygonTemplate {
        public ArrayList<Vector2> vertices;
        
        public PolygonTemplate() {
			vertices = new ArrayList<Vector2>();
		}
    }
}

