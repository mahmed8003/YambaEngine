package com.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.engine.entity.BasicEntity;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads shapes (fixture sets) defined with the Box2D Editor and applies them
 * to bodies. Has to be disposed to free some resources.
 *
 * @author Aurelien Ribon (aurelien.ribon@gmail.com)
 */
public class PhysicsEditorLoader {

	private final Map<String, BodyModel> bodyMap = new HashMap<String, BodyModel>();
	private final PolygonShape shape = new PolygonShape();
	private final World world;

	/**
	 * Creates a new fixture atlas from the selected file. This file has to
	 * exist and to be valid.
	 * @param shapeFile A file created with the editor.
	 */
	public PhysicsEditorLoader(String shapeFile, World world) {
		this.world = world;
		FileHandle handle = Gdx.files.internal(shapeFile);
		if (handle == null)
			throw new NullPointerException("shapeFile is null");
		importFromFile(handle.read());
	}

	public void dispose() {
		bodyMap.clear();
		shape.dispose();
	}

	// -------------------------------------------------------------------------
	// Public API
	// -------------------------------------------------------------------------
	
	public Body createBody(String name, final BasicEntity entity, final BodyType bodyType, final FixtureDef fixtureDef, final float pixelToMeterRatio) {
		
		BodyModel bm = bodyMap.get(name);
		if (bm == null)
			throw new RuntimeException(name + " does not exist in the fixture list.");

		Vector2[][] polygons = bm.getPolygons(entity.getWidthScaled(), entity.getHeightScaled());
		if (polygons == null)
			throw new RuntimeException(name + " does not declare any polygon. "
				+ "Should not happen. Is your shape file corrupted ?");

		
		
		final BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		
		final float halfWidth = entity.getWidth() * 0.5f / pixelToMeterRatio;
		final float halfHeight = entity.getHeight() * 0.5f / pixelToMeterRatio;

		bodyDef.position.x = entity.x / pixelToMeterRatio + halfWidth;
		bodyDef.position.y = entity.y / pixelToMeterRatio + halfHeight;

		final Body body = world.createBody(bodyDef);

		for (Vector2[] polygon : polygons) {
			shape.set(polygon);
			
			fixtureDef.shape = shape;
			body.createFixture(fixtureDef);
		}

		body.setTransform(body.getWorldCenter(), entity.rotation * MathUtils.degreesToRadians);

		return body;
	}

	// -------------------------------------------------------------------------
	// Import
	// -------------------------------------------------------------------------

	private void importFromFile(InputStream stream) {
		DataInputStream is = null;

		try {
			is = new DataInputStream(stream);
			while (is.available() > 0) {
				String name = is.readUTF();
				Vector2[][] points = readVecss(is);
				Vector2[][] polygons = readVecss(is);
				
				//
				name = removeExtension(name);

				BodyModel bm = new BodyModel(polygons);
				bodyMap.put(name, bm);
			}

		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage());

		} finally {
			if (is != null)
				try { is.close(); } catch (IOException ex) {}
		}
	}
	// -------------------------------------------------------------------------

	private Vector2 readVec(DataInputStream is) throws IOException {
		Vector2 v = new Vector2();
		v.x = is.readFloat();
		v.y = is.readFloat();
		return v;
	}

	private Vector2[] readVecs(DataInputStream is) throws IOException {
		int len = is.readInt();
		Vector2[] vs = new Vector2[len];
		for (int i=0; i<len; i++)
			vs[i] = readVec(is);
		return vs;
	}

	private Vector2[][] readVecss(DataInputStream is) throws IOException {
		int len = is.readInt();
		Vector2[][] vss = new Vector2[len][];
		for (int i=0; i<len; i++)
			vss[i] = readVecs(is);
		return vss;
	}

	// -------------------------------------------------------------------------
	// BodyModel class
	// -------------------------------------------------------------------------

	private class BodyModel {
		private final Vector2[][] normalizedPolygons;
		private final Vector2[][] polygons;

		public BodyModel(Vector2[][] polygons) {
			this.normalizedPolygons = polygons;
			this.polygons = new Vector2[polygons.length][];

			for (int i=0; i<polygons.length; i++) {
				this.polygons[i] = new Vector2[polygons[i].length];
				for (int ii=0; ii<polygons[i].length; ii++)
					this.polygons[i][ii] = new Vector2(polygons[i][ii]);
			}
		}

		public Vector2[][] getPolygons(float width, float height) {
			for (int i=0; i<normalizedPolygons.length; i++) {
				for (int ii=0; ii<normalizedPolygons[i].length; ii++) {
					this.polygons[i][ii] = new Vector2(normalizedPolygons[i][ii]);
					this.polygons[i][ii].x *= width  / 100f;
					this.polygons[i][ii].y *= height / 100f;
					
					//
					this.polygons[i][ii].x -= (width * 0.5f);
					this.polygons[i][ii].y -= (height * 0.5f);
				}
			}
			return polygons;
		}
	}
	
	// -------------------------------------------------------------------------
	// removeExtension
	// -------------------------------------------------------------------------
	public static String removeExtension(String s) {

	    String separator = System.getProperty("file.separator");
	    String filename;

	    // Remove the path upto the filename.
	    int lastSeparatorIndex = s.lastIndexOf(separator);
	    if (lastSeparatorIndex == -1) {
	        filename = s;
	    } else {
	        filename = s.substring(lastSeparatorIndex + 1);
	    }

	    // Remove the extension.
	    int extensionIndex = filename.lastIndexOf(".");
	    if (extensionIndex == -1)
	        return filename;

	    return filename.substring(0, extensionIndex);
	}
}
