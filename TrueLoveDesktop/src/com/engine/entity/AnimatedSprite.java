package com.engine.entity;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSprite extends BasicEntity {

	/**
	 * This constant is for looping the animation backwards, so it would be
	 * animated, if the frames were "1,2,3" like this, "1,2,3,2,1" etc.
	 */
	public static final int LOOP_REVERSE = 2;
	/**
	 * This constant is for looping the animation to the beggining, so it would
	 * be animated, if the frames were "1,2,3" like this, "1,2,1,2,3" etc.
	 */
	public static final int LOOP_BEGINNING = 3;
	/**
	 * The loop method, will be one of the above
	 */
	private int loopMethod = LOOP_BEGINNING;
	/**
	 * Up is true, Down is false
	 */
	private boolean loopDir = true;
	private boolean stopAnim = false;

	private float frameTime;
	private float timeElapsed;

	/**
	 * This holds all the frames, named by a string key; for instance, you could
	 * enter the key 'walk' which would return a frameset, frameset-walk-right,
	 * frameset-walk-left... etc.
	 */
	private HashMap<String, int[]> animations;

	/**
	 * All the images of this AnimatedSprite
	 */
	private TextureRegion[] regions;
	/**
	 * The current 'state' or animation that the sprite is in
	 */
	private String currentAnim;

	/**
	 * The current frame being rendered, it's not the number of the frame, its
	 * the key for the array as in it's this number
	 * "frameset[currentframe] = 1;" not this number
	 * "frameset[0] = currentframe;"
	 */
	private int currentFrame;

	/**
	 * The current set of frames being used
	 */
	private int[] currentFrameSet;

	private ArrayList<AnimationEventListener> listeners;

	public AnimatedSprite(TextureRegion tRegion, int cols, int rows) {
		int tileWidth = tRegion.getRegionWidth() / cols;
		int tileHeight = tRegion.getRegionHeight() / rows;

		regions = new TextureRegion[cols * rows];

		animations = new HashMap<String, int[]>();

		width = tileWidth;
		height = tileHeight;

		setOrigin(width / 2.0f, height / 2.0f);
		TextureRegion[][] textureRegions = tRegion.split(tileWidth, tileHeight);

		listeners = new ArrayList<AnimationEventListener>();

		int k = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				regions[k] = textureRegions[i][j];
				k++;
			}
		}
	}

	/**
	 * Lets you add a new animation set to the Sprite
	 * 
	 * @param name
	 *            The name of the Sprite, this name will be used in setting the
	 *            animation
	 * @param set
	 *            The frames of the animation
	 */
	public void addNewAnimation(String name, int[] set) {
		animations.put(name, set);
		setAnimation(name);
	}

	@Override
	public void act(float elapsedTime) {
		

		if (stopAnim)
			return;

		timeElapsed += elapsedTime;
		if (timeElapsed >= frameTime) {
			timeElapsed = 0;

			// Increment the current frame
			if (currentFrame == currentFrameSet.length - 1) {
				sendEvent();

				if (loopMethod == LOOP_BEGINNING) {
					currentFrame = 0;
				} else {
					loopDir = false;
					currentFrame--;
				}
			} else {
				if (loopMethod == LOOP_BEGINNING) {
					currentFrame++;
				} else {
					if (currentFrame == 0) {
						loopDir = true;
					}
					if (loopDir) {
						currentFrame++;
					}
					if (!loopDir) {
						currentFrame--;
					}
				}
			}
		}
		super.act(elapsedTime);
	}

	/**
	 * Draws the current frame of the sprite
	 * 
	 * @param g
	 */
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// Now we need to get the current frame, from the current frameset
		int imgNum = currentFrameSet[currentFrame];
		batch.draw(regions[imgNum], x, y, originX, originY, width, height,
				scaleX, scaleY, rotation);
	}
	
	@Override
	public void remove() {
		listeners.clear();
		regions = null;
		currentFrameSet = null;
		super.remove();
	}

	/**
	 * Sets the current animation of the Sprite
	 * 
	 * @param name
	 */
	public void setAnimation(String name) {
		if (animations.containsKey(name)) {
			currentAnim = name;
			currentFrameSet = animations.get(currentAnim);
			currentFrame = 0;
			startAnimation();
		}
	}

	public void startAnimation() {
		stopAnim = false;
		currentFrame = 0;
		timeElapsed = 0;
	}

	public void stopAnimation() {
		currentFrame = 0;
		stopAnim = true;
	}

	/**
	 * Sets how the Sprite cycles through the animations.
	 * 
	 * @param method
	 *            One of the constants LOOP_REVERSE or LOOP_BEGINNING
	 *            LOOP_BEGINNING: Will start over when loop reaches the edn
	 *            LOOP_REVERSE: Will start backwards when loop is over
	 */
	public void setLoopMethod(int method) {
		if (method == LOOP_REVERSE || method == LOOP_BEGINNING) {
			this.loopMethod = method;
		}
	}

	public String getCurrentAnimationName() {
		return currentAnim;
	}
	
	public void setFrameTime(float miliSeconds) {
		frameTime = miliSeconds / 1000.0f;
	}
	
	public void registerListener(AnimationEventListener eventListener) {
		listeners.add(eventListener);
	}

	private void sendEvent() {
		int len = listeners.size();
		for (int i = 0; i < len; i++) {
			listeners.get(i).onAnimationEnded(this);
		}
	}

}
