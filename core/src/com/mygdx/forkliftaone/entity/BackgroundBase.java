package com.mygdx.forkliftaone.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BackgroundBase extends Actor {

    private TextureRegion middleTexture, frontTexture, background;
    private ForkliftActorBase forklift;
    private Vector2 backTexturePosition, middleTexturePosition, frontTexturePosition;
    private Viewport viewport;
    private Camera camera;

    public BackgroundBase(TextureRegion background, TextureRegion layerOne, TextureRegion layerTwo, Viewport viewport, ForkliftActorBase forklift, Camera camera) {
        this.middleTexture = layerOne;
        this.frontTexture = layerTwo;
        this.background = background;
        this.forklift = forklift;
        this.viewport = viewport;
        this.camera = camera;

//        texturePosition = new Vector2(camera.position.x - getStage().getViewport().getWorldWidth()/2f,
//                getStage().getCamera().position.y - getStage().getViewport().getWorldHeight()/2f);
        backTexturePosition = new Vector2(camera.position.x - viewport.getWorldWidth()/2f,
                camera.position.y - viewport.getWorldHeight()/2f);

        middleTexturePosition = new Vector2(camera.position.x - viewport.getWorldWidth()/2f,
                camera.position.y - viewport.getWorldHeight()/2f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (background == null) {
            System.out.println("Region not set on Actor " + getClass().getName());
            return;
        } else {

            batch.draw(background, camera.position.x - getStage().getViewport().getWorldWidth()/2f, camera.position.y - getStage().getViewport().getWorldHeight()/2f,
                    getStage().getCamera().viewportWidth, getStage().getCamera().viewportHeight);


            batch.draw(frontTexture, backTexturePosition.x,
                        backTexturePosition.y = getStage().getCamera().position.y - getStage().getViewport().getWorldHeight()/2f,
                        getStage().getCamera().viewportWidth, getStage().getCamera().viewportHeight);

                batch.draw(frontTexture, backTexturePosition.x + viewport.getWorldWidth(),
                        backTexturePosition.y = getStage().getCamera().position.y - getStage().getViewport().getWorldHeight()/2f,
                        getStage().getCamera().viewportWidth, getStage().getCamera().viewportHeight);

//            batch.draw(middleTexture, middleTexturePosition.x,
//                    middleTexturePosition.y = getStage().getCamera().position.y - getStage().getViewport().getWorldHeight()/2f - 3f,
//                    getStage().getCamera().viewportWidth, getStage().getCamera().viewportHeight);
//
//            batch.draw(middleTexture, middleTexturePosition.x + viewport.getWorldWidth(),
//                    middleTexturePosition.y = getStage().getCamera().position.y - getStage().getViewport().getWorldHeight()/2f - 3f,
//                    getStage().getCamera().viewportWidth, getStage().getCamera().viewportHeight);

            batch.draw(middleTexture, middleTexturePosition.x,
                    middleTexturePosition.y = getStage().getCamera().position.y - getStage().getViewport().getWorldHeight()/2f,
                    getStage().getCamera().viewportWidth, getStage().getCamera().viewportHeight);

            batch.draw(middleTexture, middleTexturePosition.x + viewport.getWorldWidth(),
                    middleTexturePosition.y = getStage().getCamera().position.y - getStage().getViewport().getWorldHeight()/2f,
                    getStage().getCamera().viewportWidth, getStage().getCamera().viewportHeight);

        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Background layers scrolling speed
        backTexturePosition.x -= (forklift.getForklift().getLinearVelocity().x * 0);
        middleTexturePosition.x -= forklift.getForklift().getLinearVelocity().x * 0.0025;

        // Checking if texture is inside the viewport and positioning if not.
        if (viewport.getCamera().position.x - backTexturePosition.x > viewport.getWorldWidth() * 1.5f){
            System.out.println("Texture is out of screen");
            backTexturePosition.x += viewport.getWorldWidth();
        } else if (backTexturePosition.x - viewport.getCamera().position.x > 0 - viewport.getWorldWidth() / 2f){
            backTexturePosition.x -= viewport.getWorldWidth();
            System.out.println("Triggered");
        }

        if (viewport.getCamera().position.x - middleTexturePosition.x > viewport.getWorldWidth() * 1.5f){
            System.out.println("Texture is out of screen");
            middleTexturePosition.x += viewport.getWorldWidth();
        } else if (middleTexturePosition.x - viewport.getCamera().position.x > 0 - viewport.getWorldWidth() / 2f){
            middleTexturePosition.x -= viewport.getWorldWidth();
            System.out.println("Triggered");
        }

//        System.out.println("Backgroung X: " + backTexture.x + " Camera X: " +
//                (viewport.getCamera().position.x - viewport.getWorldWidth()/2f));

    }

//    private boolean isBoxInCamera(){
//        if (camera.frustum.boundsInFrustum(new Vector3(body.getPosition().x, body.getPosition().y, 0),
//                new Vector3(boxSize * 2, boxSize * 2, 0))){
//            return true;
//        }else
//            return  false;
//    }
}
