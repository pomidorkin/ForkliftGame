package com.mygdx.forkliftaone.config;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class GameConfig {
    private GameConfig() {
    }

    public static final float WIDTH = 1200; // pixels
    public static final float HEIGHT = 800; // pixels
//    public static final float WIDTH = (Gdx.graphics.getWidth());
//    public static final float HEIGHT = (Gdx.graphics.getHeight());

    public static final float WORLD_WIDTH = 80f; // world units
    public static final float WORLD_HEIGHT = 48f; // world units

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2f; // world units
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2f; // world units

    public static final float SCALE = 100f; // Scaling for map

    public static final float FORK_WIDTH = 0.4f;
    public static final float FORK_HEIGHT = 0.032f;

    // Collision Filters
    public static final short BIT_MAP = 1; // 1 2 4 8 16
    public static final short BIT_FORKLIFT = 2;
    public static final short BIT_OBSTACLE = 4;
    public static final short BIT_WALLS = 6;
    public static final short BIT_INTERNALS = 8;

}
