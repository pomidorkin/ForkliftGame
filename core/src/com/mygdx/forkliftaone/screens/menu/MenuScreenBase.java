package com.mygdx.forkliftaone.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.ForkliftModel;
import com.mygdx.forkliftaone.utils.AssetDescriptors;
import com.mygdx.forkliftaone.utils.RegionNames;

public abstract class MenuScreenBase extends ScreenAdapter {
    protected final ForkLiftGame game;
    protected final AssetManager assetManager;

    protected Viewport viewport;
    private Stage stage;
    protected SpriteBatch batch;
    protected OrthographicCamera uiCamera;

    protected TextureRegion backgoundRegion;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
    float ratio = width / height;


    public MenuScreenBase(ForkLiftGame game){
        this.game = game;
        this.batch = game.getBatch();
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        super.show();
        uiCamera = new OrthographicCamera();
        viewport = new FitViewport(1200, 1200/ratio, uiCamera);
//        viewport = new FitViewport(8f, 4.8f);
        stage = new Stage(viewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);
        stage.addActor(createUi());

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.TEST_ATLAS);
        backgoundRegion = gamePlayAtlas.findRegion(RegionNames.TEST_BACKGROUND);
    }

    protected abstract Actor createUi();

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw();

        stage.act();
        stage.draw();
    }

    public void draw(){
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        batch.draw(backgoundRegion, 0,0,
                viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
//        batch.dispose();
    }

}
