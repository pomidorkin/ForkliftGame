package com.mygdx.forkliftaone.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.forkliftaone.entity.BoxBase;
import com.mygdx.forkliftaone.entity.FuelCan;
import com.mygdx.forkliftaone.entity.MiddleBox;
import com.mygdx.forkliftaone.entity.SpecialBox;
import com.mygdx.forkliftaone.entity.TestBox;

import java.util.Random;

public class BoxFactory {
    private int variatyOfCheap = 2;

    public BoxBase getRandomCheapBox(World world, AssetManager assetManager, Camera camera, TextureAtlas atlas, Vector2 coords){

        BoxBase randomBox;

        switch (new Random().nextInt(variatyOfCheap) + 1){
            case (1):
                randomBox = getBox(world, assetManager, camera, atlas, coords);
                break;
            case (2):
                randomBox = getFuelCan(world, assetManager, camera, atlas, coords);
                break;
            default:
                randomBox = getBox(world, assetManager, camera, atlas, coords);
        }

        return randomBox;
    }

//    public BoxBase getRandomMiddleBox(World world, AssetManager assetManager, Camera camera, TextureAtlas atlas, Vector2 coords){
//        //
//    }
//
//    public BoxBase getRandomExpensiveBox(World world, AssetManager assetManager, Camera camera, TextureAtlas atlas, Vector2 coords){
//        //
//    }

    public BoxBase getBox(World world, AssetManager assetManager, Camera camera, TextureAtlas atlas, Vector2 coords){
        return new TestBox(world, assetManager, camera, atlas, coords);
    }

    public BoxBase getFuelCan(World world, AssetManager assetManager, Camera camera, TextureAtlas atlas, Vector2 coords){
        return new FuelCan(world, assetManager, camera, atlas, coords);
    }

    public BoxBase getMiddleBox(World world, AssetManager assetManager, Camera camera, TextureAtlas atlas, Vector2 coords){
        return new MiddleBox(world, assetManager, camera, atlas, coords);
    }

    public SpecialBox getSpecialBox(World world, AssetManager assetManager, Camera camera, TextureAtlas atlas, Vector2 coords){
        return new SpecialBox(world, assetManager, camera, atlas, coords);
    }
}
