package com.mygdx.forkliftaone.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.config.GameConfig;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.setProperty("user.name","Public");
		config.height = (int) GameConfig.HEIGHT;
		config.width = (int) GameConfig.WIDTH;
		new LwjglApplication(new ForkLiftGame(), config);
	}
}
