package com.mygdx.forkliftaone.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.forkliftaone.maps.CustomTestMap;
import com.mygdx.forkliftaone.maps.MapBase;
import com.mygdx.forkliftaone.maps.TestMap;

public class MapModel {
    public enum MapName{
        CUSTOM,
        TEST
    }

    private MapBase map;
    private TextureRegion backgroundTexture, middleTexture, frontTexture;

    public  MapModel(MapName md, AssetManager assetManager, World world, OrthographicCamera camera, Stage stage, TextureAtlas atlas){
        switch (md){
            case CUSTOM:
                this.backgroundTexture = atlas.findRegion(RegionNames.BOX_TEXTURE);
                this.middleTexture = atlas.findRegion(RegionNames.TEST_BACKGROUND);

                map = new CustomTestMap(world, assetManager, backgroundTexture, middleTexture, camera, stage, atlas);
                break;

            case TEST:
                this.backgroundTexture = atlas.findRegion(RegionNames.TEST_BACKGROUND);
                this.middleTexture = atlas.findRegion(RegionNames.BOX_TEXTURE);
                map = new TestMap(world, assetManager, backgroundTexture, middleTexture, camera, stage, atlas);
                break;
        }

    }

    public  MapModel(MapName md, TextureAtlas atlas){
        switch (md){
            case CUSTOM:
                this.backgroundTexture = atlas.findRegion(RegionNames.BOX_TEXTURE);
                this.middleTexture = atlas.findRegion(RegionNames.TEST_BACKGROUND);
                break;

            case TEST:
                this.backgroundTexture = atlas.findRegion(RegionNames.TEST_BACKGROUND);
                this.middleTexture = atlas.findRegion(RegionNames.BOX_TEXTURE);
                break;
        }

    }

    public MapBase getMap() {
        return map;
    }

    public TextureRegion getBackgroundTexture() {
        return backgroundTexture;
    }

    public TextureRegion getMiddleTexture() {
        return middleTexture;
    }

    public TextureRegion getFrontTexture() {
        return frontTexture;
    }
}
