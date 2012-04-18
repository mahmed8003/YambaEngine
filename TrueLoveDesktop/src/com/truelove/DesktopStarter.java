package com.truelove;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class DesktopStarter {

	private final static int WINDOW_WIDTH = 480;
	private final static int WINDOW_HEIGHT = 320;

	public static void main (String[] args) {
		new JoglApplication(new TrueLoveGame(), "True Love", WINDOW_WIDTH, WINDOW_HEIGHT, false);
	}

}
