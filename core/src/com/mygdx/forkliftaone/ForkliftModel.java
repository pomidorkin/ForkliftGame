package com.mygdx.forkliftaone;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.forkliftaone.maps.MapBase;
import com.mygdx.forkliftaone.utils.ForkliftData;
import com.mygdx.forkliftaone.utils.RegionNames;

public class ForkliftModel {

    //    public enum ModelName{
//        SMALL,
//        MEDIUM,
//        LARGE
//    }
    public enum ModelName {
        MODEL_1,
        MODEL_4,
        MODEL_5,
        MODEL_9,
        MODEL_10,
        MODEL_11,
        MODEL_12,
    }

    private Vector2[] cabin;
    private Vector2[] engine;
    //    private Vector2[] tube;
    private float[] tubeSize;
    private float rearWheelRadius;
    private float frontWheelRadius;
    private float locationOfTubes;
    private int numberOfTubes;
    private Vector2 spawnPosition; // Should be taken from the map
    private Vector2 frontWheelPosition, rearWheelPosition;
    private ForkliftData fd;
    private TextureRegion forkliftRegion, wheelRegion, tubeRegion, forkRegion;
    private int price;
    private boolean threeweeler;

    private float bodyWidth, bodyHeight;

    //    public ForkliftModel(ModelName modelName, int numberOfTubes, MapBase map){
    public ForkliftModel(ForkliftData fd, MapBase map, TextureAtlas atlas) {

        spawnPosition = map.getSpawnCoordinates();
        tubeRegion = atlas.findRegion(RegionNames.TUBE_TEXTURE);
        forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

        switch (fd.getName()) {
//            case SMALL:
//                this.numberOfTubes = fd.getTubes();
//                cabin = new Vector2[4];
//                cabin[0] = new Vector2(0.9f, 0.2f);
//                cabin[1] = new Vector2(0.8f, 1.0f);
//                cabin[2] = new Vector2(0.2f, 1.0f);
//                cabin[3] = new Vector2(0.2f, 0.3f);
////                cabin[4] = new Vector2(1.6f, 0);
//
//                engine = new Vector2[4];
//                engine[0] = new Vector2(1.0f, 0.25f);
//                engine[1] = new Vector2(0, 0.4f);
//                engine[2] = new Vector2(0, 0);
//                engine[3] = new Vector2(1.0f, 0);
//
//                tubeSize = new float[2];
//                tubeSize[0] = 0.032f;
//                tubeSize[1] = 0.5f;
//
//                rearWheelRadius = 0.1f;
//                frontWheelRadius = 0.13f;
//                locationOfTubes = 1.05f; // max width + 0.05f
//                frontWheelPosition = new Vector2(0.83f, 0.0f);
//                rearWheelPosition = new Vector2(0.13f, -0.03f);
//
//                // Textures
//                forkliftRegion = atlas.findRegion(RegionNames.SMALL_FORKLIFT);
//                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
//                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);
//
//                bodyWidth = 1.0f; // The widest number in cabin/engine
//                bodyHeight = 1.0f; // The highest number
//
//                threeweeler = false;
//                break;
//
//            case MEDIUM:
//                this.numberOfTubes = fd.getTubes();
//                cabin = new Vector2[4];
//                cabin[0] = new Vector2(1.0f, 1.1f);
//                cabin[1] = new Vector2(0.4f, 1.2f);
//                cabin[2] = new Vector2(0.3f, 0.5f);
//                cabin[3] = new Vector2(1.25f, 0.5f);
//
//                engine = new Vector2[4];
//                engine[0] = new Vector2(1.5f, 0.5f);
//                engine[1] = new Vector2(0, 0.6f);
//                engine[2] = new Vector2(0, 0);
//                engine[3] = new Vector2(1.5f, 0);
//
//                tubeSize = new float[2];
//                tubeSize[0] = 0.032f;
//                tubeSize[1] = 0.7f;
//
//                rearWheelRadius = 0.2f;
//                frontWheelRadius = 0.22f;
//                locationOfTubes = 1.55f;
//                frontWheelPosition = new Vector2(1.27f, 0);
//                rearWheelPosition = new Vector2(0.2f, 0);
//
//                // Textures
//                forkliftRegion = atlas.findRegion(RegionNames.FORKLIFT_BODY);
//                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
//                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);
//
//                bodyWidth = 1.5f; // The biggest X number in cabin/engine
//                bodyHeight = 1.2f; // The biggest Y number in cabin/engine
//
//                threeweeler = false;
//
//                break;

            case MODEL_1:
                this.numberOfTubes = fd.getTubes();
                cabin = new Vector2[4];
                cabin[0] = new Vector2(0.9f, 0.3f);
                cabin[1] = new Vector2(0.7f, 1.45f);
                cabin[2] = new Vector2(0.05f, 1.45f);
                cabin[3] = new Vector2(0.05f, 0.55f);
//                cabin[4] = new Vector2(1.6f, 0);

                engine = new Vector2[4];
                engine[0] = new Vector2(1.05f, 0.3f);
                engine[1] = new Vector2(0, 0.6f);
                engine[2] = new Vector2(0, 0);
                engine[3] = new Vector2(1.0f, 0);

                tubeSize = new float[2];
                tubeSize[0] = 0.032f;
                tubeSize[1] = 0.5f;

                rearWheelRadius = 0.1f;
                frontWheelRadius = 0.175f;
                locationOfTubes = 1.1f; // max width + 0.05f
                frontWheelPosition = new Vector2(0.95f, 0.1f);
                rearWheelPosition = new Vector2(0.2f, -0.05f);

                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_1);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                bodyWidth = 1.05f; // The widest number in cabin/engine
                bodyHeight = 1.45f; // The highest number

                threeweeler = true;
                break;

            case MODEL_4:
                this.numberOfTubes = fd.getTubes();
                cabin = new Vector2[4];
                cabin[0] = new Vector2(1.6f, 0.4f);
                cabin[1] = new Vector2(1.3f, 1.6f);
                cabin[2] = new Vector2(0.6f, 1.5f);
                cabin[3] = new Vector2(0.55f, 0.5f);
//                cabin[4] = new Vector2(1.6f, 0);

                engine = new Vector2[5];
                engine[0] = new Vector2(1.6f, 0.4f);
                engine[1] = new Vector2(0.1f, 0.6f);
                engine[2] = new Vector2(0, 0.2f);
                engine[3] = new Vector2(0.4f, 0f);
                engine[4] = new Vector2(1.7f, 0f);

                tubeSize = new float[2];
                tubeSize[0] = 0.032f;
                tubeSize[1] = 0.5f;

                rearWheelRadius = 0.175f;
                frontWheelRadius = 0.21f;
                locationOfTubes = 1.75f; // max width + 0.05f
                frontWheelPosition = new Vector2(1.6f, 0.1f);
                rearWheelPosition = new Vector2(0.25f, 0.05f);

                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_4);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                bodyWidth = 1.7f; // The widest number in cabin/engine
                bodyHeight = 1.6f; // The highest number

                threeweeler = false;
                break;

            case MODEL_5:
                this.numberOfTubes = fd.getTubes();
                cabin = new Vector2[4];
                cabin[0] = new Vector2(1.6f, 0.4f);
                cabin[1] = new Vector2(1.2f, 1.5f);
                cabin[2] = new Vector2(0.4f, 1.6f);
                cabin[3] = new Vector2(0.3f, 0.6f);
//                cabin[4] = new Vector2(1.6f, 0);

                engine = new Vector2[4];
                engine[0] = new Vector2(1.5f, 0.4f);
                engine[1] = new Vector2(0, 0.9f);
                engine[2] = new Vector2(0, 0);
                engine[3] = new Vector2(1.6f, 0);

                tubeSize = new float[2];
                tubeSize[0] = 0.032f;
                tubeSize[1] = 0.5f;

                rearWheelRadius = 0.16f;
                frontWheelRadius = 0.225f;
                locationOfTubes = 1.65f; // max width + 0.05f
                frontWheelPosition = new Vector2(1.47f, 0.12f);
                rearWheelPosition = new Vector2(0.35f, 0.05f);

                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_5);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                bodyWidth = 1.6f; // The widest number in cabin/engine
                bodyHeight = 1.6f; // The highest number

                threeweeler = false;
                break;

            case MODEL_9:
                this.numberOfTubes = fd.getTubes();
                cabin = new Vector2[4];
                cabin[0] = new Vector2(1.9f, 0.6f);
                cabin[1] = new Vector2(1.4f, 1.7f);
                cabin[2] = new Vector2(0.8f, 1.7f);
                cabin[3] = new Vector2(0.6f, 0.9f);
//                cabin[4] = new Vector2(1.6f, 0);

                engine = new Vector2[5];
                engine[0] = new Vector2(2.2f, 0.6f);
                engine[1] = new Vector2(0.1f, 0.9f);
                engine[2] = new Vector2(0, 0.1f);
                engine[3] = new Vector2(0.7f, 0);
                engine[4] = new Vector2(2.2f, 0.2f);

                tubeSize = new float[2];
                tubeSize[0] = 0.032f;
                tubeSize[1] = 0.5f;

                rearWheelRadius = 0.25f;
                frontWheelRadius = 0.3f;
                locationOfTubes = 2.25f; // max width + 0.05f
                frontWheelPosition = new Vector2(1.9f, 0.15f);
                rearWheelPosition = new Vector2(0.35f, 0.1f);

                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_9);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                bodyWidth = 2.2f; // The widest number in cabin/engine
                bodyHeight = 1.7f; // The highest number

                threeweeler = false;
                break;

            case MODEL_10:
                this.numberOfTubes = fd.getTubes();
                cabin = new Vector2[4];
                cabin[0] = new Vector2(2.0f, 0.5f);
                cabin[1] = new Vector2(1.6f, 1.7f);
                cabin[2] = new Vector2(0.9f, 1.8f);
                cabin[3] = new Vector2(0.9f, 0.7f);
//                cabin[4] = new Vector2(1.6f, 0);

                engine = new Vector2[4];
                engine[0] = new Vector2(2.0f, 0.5f);
                engine[1] = new Vector2(0.1f, 0.8f);
                engine[2] = new Vector2(0, 0);
                engine[3] = new Vector2(2.15f, 0);

                tubeSize = new float[2];
                tubeSize[0] = 0.032f;
                tubeSize[1] = 0.5f;

                rearWheelRadius = 0.3f;
                frontWheelRadius = 0.35f;
                locationOfTubes = 2.2f; // max width + 0.05f
                frontWheelPosition = new Vector2(2.0f, 0.1f);
                rearWheelPosition = new Vector2(0.45f, 0.05f);

                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_10);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                bodyWidth = 2.15f; // The widest number in cabin/engine
                bodyHeight = 1.8f; // The highest number

                threeweeler = false;
                break;

            case MODEL_11:
                this.numberOfTubes = fd.getTubes();
                cabin = new Vector2[4];
                cabin[0] = new Vector2(2.3f, 0.6f);
                cabin[1] = new Vector2(1.8f, 1.85f);
                cabin[2] = new Vector2(1.05f, 1.85f);
                cabin[3] = new Vector2(0.9f, 0.9f);
//                cabin[4] = new Vector2(1.6f, 0);

                engine = new Vector2[4];
                engine[0] = new Vector2(2.3f, 0.6f);
                engine[1] = new Vector2(0.1f, 0.9f);
                engine[2] = new Vector2(0, 0);
                engine[3] = new Vector2(2.4f, 0.1f);

                tubeSize = new float[2];
                tubeSize[0] = 0.032f;
                tubeSize[1] = 0.5f;

                rearWheelRadius = 0.35f;
                frontWheelRadius = 0.4f;
                locationOfTubes = 2.45f; // max width + 0.05f
                frontWheelPosition = new Vector2(2.3f, 0.1f);
                rearWheelPosition = new Vector2(0.55f, 0.1f);

                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_11);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                bodyWidth = 2.4f; // The widest number in cabin/engine
                bodyHeight = 1.85f; // The highest number

                threeweeler = false;
                break;

            case MODEL_12:
                this.numberOfTubes = fd.getTubes();
                cabin = new Vector2[4];
                cabin[0] = new Vector2(2.4f, 1.0f);
                cabin[1] = new Vector2(2.2f, 2.0f);
                cabin[2] = new Vector2(1.2f, 1.9f);
                cabin[3] = new Vector2(1.1f, 1.0f);
//                cabin[4] = new Vector2(1.6f, 0);

                engine = new Vector2[6];
                engine[0] = new Vector2(2.5f, 0.4f);
                engine[1] = new Vector2(2.45f, 1.0f);
                engine[2] = new Vector2(0.1f, 1.0f);
                engine[3] = new Vector2(0f, 0f);
                engine[4] = new Vector2(1.0f, 0f);
                engine[5] = new Vector2(1.6f, 0f);

                tubeSize = new float[2];
                tubeSize[0] = 0.032f;
//                tubeSize[1] = 0.5f;
                tubeSize[1] = 1.0f;

                rearWheelRadius = 0.4f;
                frontWheelRadius = 0.45f;
                locationOfTubes = 2.55f; // max width + 0.05f
                frontWheelPosition = new Vector2(2.05f, 0.2f);
                rearWheelPosition = new Vector2(0.5f, 0.1f);

                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_12);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                bodyWidth = 2.5f; // The widest number in cabin/engine
                bodyHeight = 2.0f; // The highest number

                threeweeler = false;
                break;
        }
    }

    public ForkliftModel(ForkliftData fd, TextureAtlas atlas) {

        spawnPosition = new Vector2(0, 0);

        switch (fd.getName()) {
            case MODEL_1:
                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_1_WHEELS);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);
                break;

            case MODEL_4:
                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_4_WHEELS);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                break;

            case MODEL_9:
                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_9_WHEELS);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                break;

            case MODEL_10:
                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_10_WHEELS);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                break;

            case MODEL_11:
                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_11_WHEELS);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                break;

            case MODEL_12:
                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_12_WHEELS);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                break;

            case MODEL_5:
                // Textures
                forkliftRegion = atlas.findRegion(RegionNames.MODEL_5_WHEELS);
                wheelRegion = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
                forkRegion = atlas.findRegion(RegionNames.FORK_TEXTURE);

                break;
        }

    }

    public Vector2[] getCabin() {
        return cabin;
    }

    public Vector2[] getEngine() {
        return engine;
    }

    public float[] getTubeSize() {
        return tubeSize;
    }

    public float getRearWheelRadius() {
        return rearWheelRadius;
    }

    public float getFrontWheelRadius() {
        return frontWheelRadius;
    }

    public int getNumberOfTubes() {
        return numberOfTubes;
    }

    public float getLocationOfTubes() {
        return locationOfTubes;
    }

    public Vector2 getSpawnPosition() {
        return spawnPosition;
    }

    public Vector2 getFrontWheelPosition() {
        return frontWheelPosition;
    }

    public Vector2 getRearWheelPosition() {
        return rearWheelPosition;
    }

    public TextureRegion getForkliftRegion() {
        return forkliftRegion;
    }

    public TextureRegion getWheelRegion() {
        return wheelRegion;
    }

    public TextureRegion getTubeRegion() {
        return tubeRegion;
    }

    public TextureRegion getForkRegion() {
        return forkRegion;
    }

    public float getBodyWidth() {
        return bodyWidth;
    }

    public float getBodyHeight() {
        return bodyHeight;
    }

    public boolean isThreeweeler() {
        return threeweeler;
    }
}
