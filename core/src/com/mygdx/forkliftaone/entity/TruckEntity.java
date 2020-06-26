package com.mygdx.forkliftaone.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.forkliftaone.maps.MapBase;

public class TruckEntity extends Actor {

    private TextureRegion truckTexture;
    private float truckX, truckY, truckWidth, truckHeight;

    public TruckEntity(MapBase map){
        this.truckTexture = map.getTruckTexture();
        this.truckX = map.getTruckX();
        this.truckY = map.getTruckY();
        this.truckWidth = map.getTruckWidth();
        this.truckHeight = map.getTruckHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(truckTexture, // Texture
                truckX, truckY, // Texture position
                getOriginX(), getOriginY(), // Rotation point (width / 2, height /2 = center)
                truckWidth, truckHeight, // Width and height of the texture
                1f, 1f, //scaling
                0); // Rotation (radiants to degrees)
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
