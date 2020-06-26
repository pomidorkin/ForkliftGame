package com.mygdx.forkliftaone.screens.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.screens.menu.MenuScreen;
import com.mygdx.forkliftaone.screens.tests.LayoutTestScreen;
import com.mygdx.forkliftaone.screens.tutorial.TutorialScreen;
import com.mygdx.forkliftaone.utils.AssetDescriptors;
import com.mygdx.forkliftaone.utils.Inventory;
import com.mygdx.forkliftaone.utils.ProcessInventoryImproved;
import com.mygdx.forkliftaone.utils.RegionNames;

public class TestLoading extends ScreenAdapter {
    // == attributes ==
    private Viewport viewport;

    private float progress;
    private float waitTime = 0.75f;
    private boolean changeScreen;

    protected SpriteBatch batch;
    protected Texture backgoundRegion;
    protected OrthographicCamera uiCamera;

    private final ForkLiftGame game;
    private final AssetManager assetManager;

    ProcessInventoryImproved pi = new ProcessInventoryImproved();
    private Inventory inv;

    private Skin skin;
    private Table table;
    private ProgressBar pb;
    private Stage stage;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
    float ratio = width / height;

    // == constructors ==
    public TestLoading(ForkLiftGame game) {
        this.game = game;
        this.batch = game.getBatch();
//        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera = new OrthographicCamera();
        viewport = new FitViewport(1200, 1200/ratio, uiCamera);
        stage = new Stage(viewport, game.getBatch());
        assetManager = game.getAssetManager();
        inv = pi.read();
    }

    @Override
    public void show() {

//        skin = new Skin(Gdx.files.internal("neon/neon-ui.json"));
//        skin = new Skin(Gdx.files.internal("freezing/freezing-ui.json"));
        skin = new Skin(Gdx.files.internal("custom/CustomSkinUI.json"));


        backgoundRegion = new Texture(Gdx.files.internal("upacked/background.png"));

        // How to access different fields within the json class
//        pb = new ProgressBar(0.0f, Gdx.graphics.getWidth()/2, 0.01f, false, skin.get("fancy", ProgressBar.ProgressBarStyle.class));
        pb = new ProgressBar(0.0f, Gdx.graphics.getWidth()/2, 0.01f, false, skin);

//        pb.setAnimateDuration(0.25f);

        table = new Table();
        table.setWidth(Gdx.graphics.getWidth());
        table.align(Align.center | Align.top);
        table.setPosition(0, Gdx.graphics.getHeight()/2);

        table.add(pb).width(Gdx.graphics.getWidth()/2).height(10f);
//        table.add(pb);
        stage.addActor(table);

        this.loadAssets();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();

        draw();

        pb.setValue(assetManager.getProgress() * Gdx.graphics.getWidth()/2);
        stage.draw();

        if(changeScreen) {

            // Проверять на прохождение туториала
            if (!inv.isTutorialPassed()){
                game.setScreen(new TutorialScreen(game));
            } else {
                game.setScreen(new MenuScreen(game));
            }


//            game.setScreen(new LayoutTestScreen(game)); // Testing lining up & layout
        }
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
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        // NOTE: screens dont dispose automatically
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgoundRegion.dispose();
//        batch.dispose();
    }

    // == private methods ==
    private void update(float delta) {
        // progress is between 0 and 1
        progress = assetManager.getProgress();

        // update returns true when all assets are loaded
        if(assetManager.update()) {
            waitTime -= delta;

            if(waitTime <= 0) {
                changeScreen = true;
            }
        }
    }


    private static void waitMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAssets(){
        assetManager.load(AssetDescriptors.FONT);
        assetManager.load(AssetDescriptors.TEST_ATLAS);
        assetManager.load(AssetDescriptors.TEST_MUSIC);
        assetManager.load(AssetDescriptors.TEST_ENGINE);
        assetManager.load(AssetDescriptors.IDLE_ENGINE);
        assetManager.load(AssetDescriptors.BACKUP_SOUND);
        assetManager.load(AssetDescriptors.BREAK_SOUND);
        assetManager.load(AssetDescriptors.FILLING_SOUND);
        assetManager.load(AssetDescriptors.SERVO_SOUND);
        assetManager.finishLoading();//?
    }
}
