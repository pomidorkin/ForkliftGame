package com.mygdx.forkliftaone.maps;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.forkliftaone.entity.Sensor;
import com.mygdx.forkliftaone.utils.TiledObjectUtil;

//Spawning objects should be up to the map actor
public abstract class MapBase extends Actor{

    private World world;
    private TiledMap tiledMap;
    private Vector2 spawnCoordinates;
    private String mapName;
    private Sensor sensor;
    protected TextureRegion backTexture, middleTexture, frontTexture, truckTexture;
    private float truckX, truckY, truckWidth, truckHeight;

    public MapBase(World world, String mapName, Vector2 spawnCoordinates,
                   float x, float y, float width, float height, TextureRegion truckTexture,
                   float sensorX, float sensorY, float sensorWidth, float sensorHeight){
        this.world = world;
        this.mapName = mapName;
        this.spawnCoordinates = spawnCoordinates;
        this.truckX = x;
        this.truckY = y;
        this.truckWidth = width;
        this.truckHeight = height;

        this.truckTexture = truckTexture;

        sensor = new Sensor(world, sensorX, sensorY, sensorWidth, sensorHeight);
    }

    public void createMap(){
        //TiledMap
        tiledMap = new TmxMapLoader().load(mapName);
        TiledObjectUtil.parseTiledTiledObjectLayer(world, tiledMap.getLayers().get("collision-layer").getObjects());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
//       batch.draw(truckTexture, // Texture
//               truckX, truckY, // Texture position
//                    getOriginX(), getOriginY(), // Rotation point (width / 2, height /2 = center)
//               truckWidth, truckHeight, // Width and height of the texture
//                    1f, 1f, //scaling
//                    0); // Rotation (radiants to degrees)


    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public int getSalary(){
        return sensor.getSalary();
    }

    public int getDonateSalary() {
        return sensor.getDonateSalary();
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void disposeTiledMap(){
        tiledMap.dispose();
    }

    public Vector2 getSpawnCoordinates() {
        return spawnCoordinates;
    }

    public abstract void openDoor();

    public abstract void spawnBoxes();

    public TextureRegion getBackTexture() {
        return backTexture;
    }

    public TextureRegion getMiddleTexture() {
        return middleTexture;
    }

    public TextureRegion getFrontTexture() {
        return frontTexture;
    }

    public TextureRegion getTruckTexture() {
        return truckTexture;
    }

    public float getTruckX() {
        return truckX;
    }

    public float getTruckY() {
        return truckY;
    }

    public float getTruckWidth() {
        return truckWidth;
    }

    public float getTruckHeight() {
        return truckHeight;
    }
}
