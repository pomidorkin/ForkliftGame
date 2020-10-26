package com.mygdx.forkliftaone.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.forkliftaone.maps.CandyMap;
import com.mygdx.forkliftaone.maps.CustomTestMap;
import com.mygdx.forkliftaone.maps.MapBase;
import com.mygdx.forkliftaone.maps.TestMap;

public class MapModel {
    public enum MapName{
        CUSTOM,
        CANDY,
        TEST
    }

    private MapBase map;
    private TextureRegion backgroundTexture;

    public  MapModel(MapName md, AssetManager assetManager, World world, OrthographicCamera camera, Stage stage, TextureAtlas atlas){
        switch (md){
            case CUSTOM:
                map = new CustomTestMap(world, assetManager, camera, stage, atlas);
                this.backgroundTexture = map.getBackTexture();
                break;

            case CANDY:
                map = new CandyMap(world, assetManager, camera, stage, atlas);
                this.backgroundTexture = map.getBackTexture();
                break;

            case TEST:
                map = new TestMap(world, assetManager, camera, stage, atlas);
                this.backgroundTexture = map.getBackTexture();
                break;
        }

    }

    public  MapModel(MapName md, TextureAtlas atlas){
        switch (md){
            case CUSTOM:
                this.backgroundTexture = atlas.findRegion(RegionNames.BOX_TEXTURE);
                break;

            case CANDY:
                this.backgroundTexture = atlas.findRegion(RegionNames.MAP_CANDY_BACK);
                break;

            case TEST:
                this.backgroundTexture = atlas.findRegion(RegionNames.TEST_BACKGROUND);
                break;
        }

    }

    public MapBase getMap() {
        return map;
    }

    public TextureRegion getBackgroundTexture() {
        return backgroundTexture;
    }
}
