package com.mygdx.forkliftaone.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.ForkliftModel;
import com.mygdx.forkliftaone.config.GameConfig;
import com.mygdx.forkliftaone.dialogs.BackToMenuDialog;
import com.mygdx.forkliftaone.dialogs.EmptyFuelDialog;
import com.mygdx.forkliftaone.entity.BackgroundBase;
import com.mygdx.forkliftaone.entity.ForkliftActorBase;
import com.mygdx.forkliftaone.entity.FuelCan;
import com.mygdx.forkliftaone.entity.TruckEntity;
import com.mygdx.forkliftaone.handlers.SensorContactListener;
import com.mygdx.forkliftaone.maps.MapBase;
import com.mygdx.forkliftaone.screens.menu.MenuScreen;
import com.mygdx.forkliftaone.utils.AssetDescriptors;
import com.mygdx.forkliftaone.utils.ForkliftData;
import com.mygdx.forkliftaone.utils.Inventory;
import com.mygdx.forkliftaone.utils.MapData;
import com.mygdx.forkliftaone.utils.MapModel;
import com.mygdx.forkliftaone.utils.ProcessInventory;
import com.mygdx.forkliftaone.utils.ProcessInventoryImproved;
import com.mygdx.forkliftaone.utils.RegionNames;

public class GameScreen extends ScreenAdapter implements InputProcessor {

    private final ForkLiftGame game;
    private final AssetManager assetManager;
    private final SpriteBatch batch;
    private final GlyphLayout layout = new GlyphLayout();

    private Viewport viewport, uiViewport, dynamicViewport;
    private OrthographicCamera camera, uiCamera, dynamicCamera;
    private Stage stage, uiStage, fuelStage;
    private float accumulator = 0;

    // UI
    private BitmapFont font;
    //    private ShapeRenderer shapeRenderer;
    private Table table;
    private Skin skin;
    private ImageButton fuelButton;
    private TextureRegion coinTexture, gemTexture;

    //Music
    private Music music;
    private Sound engineSound, idleSound, beepingSound, servoSound, fillingSound;
    private long soundID, beepSoundID, idleSoundID, servoSoundID, fillingSoundID;
    private boolean paused = false;
    private boolean reversePaused = false;
    private boolean servoPaused = false;


    //test
    private Box2DDebugRenderer b2dr;
    private OrthogonalTiledMapRenderer tmr;
    private World world;
    private ForkliftModel model;
    private MapModel mapModel;
    private ForkliftActorBase forklift;
    private ForkliftData fd;
    private MapData md;
    //    private ProcessInventory pi = new ProcessInventory();
    ProcessInventoryImproved pi = new ProcessInventoryImproved();
    private Inventory inv;
    private SensorContactListener scl;

    private ProgressBar bar;
    private MapBase map;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
    float ratio = width / height;

    private boolean gamePaused;

    public GameScreen(ForkLiftGame game, ForkliftData fd, MapData md) {
        this.game = game;
        this.fd = fd;
        this.md = md;
        assetManager = game.getAssetManager();
        batch = game.getBatch();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
//        viewport = new FitViewport(width, height, camera);
        viewport = new FitViewport(8f, 8f / ratio, camera);
        stage = new Stage(viewport, batch);
//        Gdx.input.setInputProcessor(this);

        // Initializing UI
        uiCamera = new OrthographicCamera();
        dynamicCamera = new OrthographicCamera();
//        uiViewport = new FitViewport(800, 800/ratio, uiCamera);
        uiViewport = new FitViewport(1200, 1200/ratio, uiCamera);
        dynamicViewport = new FitViewport(width, height, dynamicCamera);
//        uiViewport = new FitViewport(width, height, uiCamera);
        font = assetManager.get(AssetDescriptors.FONT);
//        font.getData().setScale(height / 480f); // Should be fixed
        font.getData().setScale(height / (1200/ratio));
//        shapeRenderer = new ShapeRenderer();
        uiStage = new Stage(uiViewport, game.getBatch());

        // Load & Saving logic
        inv = pi.read();

        // Initializing music
        music = assetManager.get(AssetDescriptors.TEST_MUSIC);
        engineSound = assetManager.get(AssetDescriptors.TEST_ENGINE);
        beepingSound = assetManager.get(AssetDescriptors.BACKUP_SOUND);
        servoSound = assetManager.get(AssetDescriptors.SERVO_SOUND);
        fillingSound = assetManager.get(AssetDescriptors.FILLING_SOUND);
        idleSound = assetManager.get(AssetDescriptors.IDLE_ENGINE); // Should be changed to the idle sound
        soundID = engineSound.loop();
        beepSoundID = beepingSound.loop();
        idleSoundID = idleSound.loop();
        servoSoundID = servoSound.loop();
        engineSound.pause(soundID);
        beepingSound.pause(beepSoundID);
        idleSound.pause(idleSoundID);
        servoSound.pause(servoSoundID);
        engineSound.setVolume(soundID, inv.getSd().getSoundVolume());
        beepingSound.setVolume(beepSoundID, inv.getSd().getSoundVolume());
        idleSound.setVolume(idleSoundID, inv.getSd().getSoundVolume());
        servoSound.setVolume(servoSoundID, inv.getSd().getSoundVolume());
        fillingSound.setVolume(fillingSoundID, inv.getSd().getSoundVolume());
        paused = true;
        reversePaused = true;
        music.play();
        // Volume should be obtained from the savings
        music.setVolume(inv.getSd().getMusicVolume());
        music.setLooping(true);

        // May be refactor later, because there are too many stages
        fuelStage = new Stage(uiViewport, game.getBatch());
//        Gdx.input.setInputProcessor(uiStage);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(fuelStage);
        Gdx.input.setInputProcessor(multiplexer);

        uiStage.addActor(createUi());
        fuelStage.addActor(createFuelButton());


        //test
        world = new World(new Vector2(0, -9.8f), false);
        // Adding contact listeners
        this.scl = new SensorContactListener();
        world.setContactListener(scl);

        // Creating texture atlas
        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.TEST_ATLAS);
//        TextureRegion forkliftRegion = gamePlayAtlas.findRegion(RegionNames.FORKLIFT_BODY);
//        TextureRegion wheelRegion = gamePlayAtlas.findRegion(RegionNames.FORKLIFT_WHEEL);
//        TextureRegion backgoundRegion = gamePlayAtlas.findRegion(RegionNames.TEST_BACKGROUND);
        this.coinTexture = gamePlayAtlas.findRegion(RegionNames.COIN_TEXTURE);
        this.gemTexture = gamePlayAtlas.findRegion(RegionNames.GEM_TEXTURE);

//        map = new TestMap(world, camera, stage, gamePlayAtlas);
//        map = new CustomTestMap(world, camera, stage, gamePlayAtlas);
//        map.setRegion(backgoundRegion);
//        map.createMap();
//        stage.addActor(map);
//        map.spawnBoxes();

        mapModel = new MapModel(md.getName(), assetManager, world, camera, stage, gamePlayAtlas);
        map = mapModel.getMap();
        map.createMap();


        // Class ForkliftModel should have a constructor taking arguments from inventory
//        model = new ForkliftModel(ForkliftModel.ModelName.MEDIUM, map);
        model = new ForkliftModel(fd, map, gamePlayAtlas);
        forklift = new ForkliftActorBase(world, model);
        forklift.createForklift(model);
        forklift.setRegion();


        b2dr = new Box2DDebugRenderer();
        //Second parameter is responsible for scaling
        tmr = new OrthogonalTiledMapRenderer(map.getTiledMap(), 1 / GameConfig.SCALE);

        // Parallax background
        BackgroundBase backBase = new BackgroundBase(map.getBackTexture(), map.getMiddleTexture(), map.getFrontTexture(), viewport, forklift, camera);

        // The order determines the drawing consequence
        stage.addActor(backBase);
        stage.addActor(forklift);
        stage.addActor(map);
        map.spawnBoxes();
        stage.addActor(new TruckEntity(map));

        // Rubbish
//        BoxBase box = new TestBox(world, camera, gamePlayAtlas);
//        BoxFactory factory = new BoxFactory();
//        stage.addActor(factory.getBox(world, camera, gamePlayAtlas, new Vector2(5f, 5f)));
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Разборки с текстурами
        viewport.apply();
        stage.draw();
        tmr.render();
//        b2dr.render(world, camera.combined);

        // Testing UI
        dynamicViewport.apply();
        renderUi();

//        if (!gamePaused) {
//            // Stage acting
//            stage.act();
//        }
        uiViewport.apply();
        uiStage.draw();

        if (!gamePaused) {
            // Stage acting
            update(Gdx.graphics.getDeltaTime());
            stage.act();

            // Fuel system
            if (forklift.isFuelTankEmpty()) {
                final GameScreen gs = this;
//                game.setScreen(new MenuScreen(game));
//                Inventory inv2 = new Inventory(inv.getBalance() + map.getSalary(),
//                        inv.getDonateCurrency() + map.getDonateSalary(),
//                        inv.isDonateBoxesPurchased(), inv.getAllModels(), inv.getAllMaps());
//                pi.write(inv2);
//                game.setScreen(new MenuScreen(game));
                EmptyFuelDialog fuelDialog = new EmptyFuelDialog(game, gs, forklift, map, "Return to menu?", skin);
                gs.setGamePaused(true);
                fuelDialog.show(uiStage);
            }

            if (forklift.isHasFuel()) {
                if (fuelButton.getTouchable().equals(Touchable.disabled)) {
                    fuelButton.setTouchable(Touchable.enabled);
                }
                fuelStage.draw();
            } else if (fuelButton.getTouchable().equals(Touchable.enabled)) {
                fuelButton.setTouchable(Touchable.disabled);
            }
        }

    }

    private void update(float delta) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        while (accumulator >= 1 / 60f) {
            world.step(1 / 60f, 6, 2);
            accumulator -= 1 / 60f;
        }

        tmr.setView(camera);
        cameraUpdate(delta);

        // Разборки с текстурами
//        stage.setViewport(viewport);
//        batch.setProjectionMatrix(camera.combined);
    }

    // Testing UI
    private void renderUi() {
        // Testing style. Should be replaced with a picture from Asset Manager
        // Position should be absolute. It should not depend on screen/viewport width and height

        batch.setProjectionMatrix(dynamicCamera.combined);
        batch.begin();

        // draw balance
//        String balanceText = inv.getBalance() + " +" + scl.getSalary();
//        layout.setText(font, balanceText);
////        font.draw(batch, layout, (uiViewport.getScreenHeight() / 10f) * 1.5f, height - uiViewport.getScreenHeight() / 10f / 2);
//        font.draw(batch, layout, (Gdx.graphics.getHeight() / 10f) * 1.5f, height - Gdx.graphics.getHeight()  / 10f / 2);
//
//
//
//        // draw gems
//        String donate = inv.getDonateCurrency() + " + " + scl.getDonateSalary();
//        layout.setText(font, donate);
//        font.draw(batch, layout,
//                (dynamicViewport.getScreenHeight() / 10f) * 1.5f,
//                height - dynamicViewport.getScreenHeight() / 6f
//        );

        // draw balance
        String balanceText = "" + inv.getBalance() + " + " + scl.getSalary();
        layout.setText(font, balanceText);
//        font.draw(batch, layout, (uiViewport.getScreenHeight() / 10f) * 1.5f, height - uiViewport.getScreenHeight() / 10f / 2);
        font.draw(batch, layout, (height  / 10f) * 1.5f, height - height / 10f - (height / 20f) + ((height / 10f) * 0.70f));

        // draw gems
        String donate = "" + inv.getDonateCurrency() + " + " + scl.getDonateSalary();
        layout.setText(font, donate);
        font.draw(batch, layout,
                (height  / 10f) * 1.5f,
                height - ((height / 10f - (height / 20f)) + height / 10f * 2.1f)  + ((height / 10f) * 0.70f)
        );

        // Draw fuel icon
        bar.setValue(forklift.getFuelTank());

        // Drawing coin image
//        batch.draw(coinTexture, // Texture
//                10f, height - dynamicViewport.getScreenHeight() / 10f - 10f, // Texture position
//                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
//                height / 11f, height / 11f, // Width and height of the texture
//                1f, 1f, //scaling
//                0); // Rotation (radiants to degrees)
//
//        // Drawing gem image
//        batch.draw(gemTexture, // Texture
//                10f, height - (dynamicViewport.getScreenHeight() / 6f + coinTexture.getRegionHeight() / 2f), // Texture position
//                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
//                height / 11f, height / 11f, // Width and height of the texture
//                1f, 1f, //scaling
//                0); // Rotation (radiants to degrees)

        // Drawing coin image
        batch.draw(coinTexture, // Texture
                10f, height - height / 10f - (height / 20f), // Texture position
                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
                height / 10f, height / 10f, // Width and height of the texture
                1f, 1f, //scaling
                0); // Rotation (radiants to degrees)

        // Drawing gem image
        batch.draw(gemTexture, // Texture
                10f, height - ((height / 10f - (height / 20f)) + height / 10f * 2.1f), // Texture position
                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
                height / 10f, height / 10f, // Width and height of the texture
                1f, 1f, //scaling
                0); // Rotation (radiants to degrees)

        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        uiViewport.update(width, height, true);
        dynamicViewport.update(width, height, true);
    }

    @Override
    public void hide() {
        // Saving example
        super.hide();
        music.stop();
        engineSound.stop();
        idleSound.stop();
        beepingSound.stop();
        servoSound.stop();
        fillingSound.stop();

        dispose();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        tmr.dispose();
        b2dr.dispose();
        map.disposeTiledMap();
        font.dispose();
//        shapeRenderer.dispose();
        skin.dispose();
//        batch.dispose();
    }

    private Actor createUi() {
//        skin = new Skin(Gdx.files.internal("neon/neon-ui.json"));
        skin = new Skin(Gdx.files.internal("custom/CustomSkinUI.json"));

        table = new Table();

        ImageButton menuButton = new ImageButton(skin.get("pauseButton", ImageButton.ImageButtonStyle.class));
        final GameScreen gs = this;
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

//                Inventory inv2 = new Inventory(inv.getBalance() + map.getSalary(),
//                        inv.getDonateCurrency() + map.getDonateSalary(),
//                        inv.isDonateBoxesPurchased(), inv.getAllModels(), inv.getAllMaps());
//                pi.write(inv2);
//                game.setScreen(new MenuScreen(game));
                BackToMenuDialog menuDialog = new BackToMenuDialog(game, gs, map, "", skin);
                gs.setGamePaused(true);
                menuDialog.show(uiStage);
                menuDialog.setWidth(1200/2);
                menuDialog.setHeight(1200/ratio/2);
                menuDialog.setPosition(1200/2f - (menuDialog.getWidth()/2), 1200/ratio/2f - (menuDialog.getHeight()/2));
                menuDialog.setMovable(false);
            }
        });

        // Testing Fuel icon
        bar = new ProgressBar(0, 100, 0.01f, false, skin);

        // The size of the fuel icon can be changed here
//        table.add(bar).width(Gdx.graphics.getWidth() / 8).height(10f);

        table.add().height(1200/ratio/3 - 50);// Adding an empty column (Empty cell)
        table.add(bar).top().width((1200 - (1200 / ratio /3) * 2f) / 4f);
        table.add(menuButton).width(1200/ratio/6).height(1200/ratio/6).right().top();
        table.row();
        table.add().width(1200/5f).height(1200/ratio/3);
//        table.add().width((800/5) * 3f  - 50).height(800/ratio/3);
        table.add().width((1200 - ((1200 / ratio /3) * 2f)) - 100f);
        table.add().width(1200/5f).height(1200/ratio/3);
        table.row();

        // Touchpad test
        // Left touchpad
//        skin = new Skin(Gdx.files.internal("neon/neon-ui.json"));
        skin = new Skin(Gdx.files.internal("custom/CustomSkinUI.json"));
        final Touchpad touchpad = new Touchpad(1f, skin);

        // Setting the minimal size of the knob (Размер джойстика)
//        touchpad.getStyle().knob.setMinWidth(height/6f);
//        touchpad.getStyle().knob.setMinHeight(height/6f);


        // Logic for mooving and playing the engine sound
        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (touchpad.getKnobX() < touchpad.getWidth() / 2) {
                    forklift.moveForkliftLeft();
                    if (paused){
                        engineSound.resume(soundID);
                        idleSound.pause(idleSoundID);
//                        beepingSound.resume(beepSoundID);
                        paused = false;
                    }
                    if (reversePaused){
                        beepingSound.resume(beepSoundID);
                        reversePaused = false;
                    }
                } else if (touchpad.getKnobX() > touchpad.getWidth() / 2) {
                    forklift.moveForkliftRight();
                    if (paused){
                        engineSound.resume(soundID);
                        idleSound.pause(idleSoundID);
                        paused = false;
                    }
                    if (!reversePaused){
                        beepingSound.pause(beepSoundID);
                        reversePaused = true;
                    }
                } else {
                    forklift.stopMoveForkliftLeft();
                    if (!paused){
                        engineSound.pause(soundID);
                        idleSound.resume(idleSoundID);
//                        beepingSound.pause(beepSoundID);
                        paused = true;
                    }
                    if (!reversePaused){
                        beepingSound.pause(beepSoundID);
                        reversePaused = true;
                    }
                }

            }
        });

        final Touchpad rightTouchpad = new Touchpad(1f, skin);


        // Logic for mooving and playing the engine sound
        rightTouchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (rightTouchpad.getKnobY() < rightTouchpad.getHeight() / 2) {
                    forklift.moveTubeDown();
                    if (servoPaused){
                        servoSound.resume(servoSoundID);
                        servoPaused = false;
                    }
                } else if (rightTouchpad.getKnobY() > rightTouchpad.getHeight() / 2) {
                    forklift.moveTubeUp();
                    if (servoPaused){
                        servoSound.resume(servoSoundID);
                        servoPaused = false;
                    }
                } else {
                    forklift.stopMoveTubeUp();
                    if (!servoPaused){
                        servoSound.pause(servoSoundID);
                        servoPaused = true;
                    }
                }

            }
        });


//        table.add(touchpad).height(1200/ratio/3).width(1200/ratio/3);
//        table.add();
//        table.add(rightTouchpad).height(1200/ratio/3).width(1200/ratio/3);
//        table.row();


        table.add().height(1200/ratio/12).width(1200/ratio/12);
        table.add();
        table.add().height(1200/ratio/12).width(1200/ratio/12);
        table.row();
        table.add(touchpad).height(1200/ratio/4).width(1200/ratio/4);
        table.add();
        table.add(rightTouchpad).height(1200/ratio/4).width(1200/ratio/4);
        table.row();

        // Debug enabled
//        table.debug();

//        // Testing Fuel icon
//        bar = new ProgressBar(0, 100, 0.01f, false, skin);
//
//        // The size of the fuel icon can be changed here
//        table.add(bar).width(Gdx.graphics.getWidth() / 8).height(10f);

        Table main = new Table();
        main.add(table).padTop(25f).padRight(25f).padLeft(25f).padBottom(25f).fill();
        main.row();
//        main.debug();
        main.setFillParent(true);

        return main;
    }

    private Actor createFuelButton() {
//        skin = new Skin(Gdx.files.internal("custom/CustomSkinUI.json"));


        Table localTable = new Table();
//        table.setWidth(Gdx.graphics.getWidth());
//        table.align(Align.center | Align.top);
//        table.setPosition(0, Gdx.graphics.getHeight());

        // Fuel button test rough
//        fuelButton = new TextButton("Fill", skin);
        fuelButton = new ImageButton(skin.get("cannisterButton", ImageButton.ImageButtonStyle.class));
        fuelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                forklift.setFuelTank(100f);

                // Logic for deleting fuel can after pressing the button
                for (Actor can : stage.getActors()) {
                    if (can instanceof FuelCan && ((FuelCan) can).isActive()) {
                        ((FuelCan) can).detroyBox();
                    }
                }

                fillingSound.play();

            }
        });

        localTable.add().height(1200/ratio/3 - 50);// Adding an empty column (Empty cell)
        localTable.add().width((1200 - ((1200 / ratio /3) * 2f)) - 100f);
        localTable.add().width(1200/5f);
        localTable.row();
        localTable.add(fuelButton).width(1200/ratio/6).height(1200/ratio/6).left();
        localTable.add().width((1200 - ((1200 / ratio /3) * 2f)) - 100f);
        localTable.add().width(1200/5f).height(800/ratio/3);
        localTable.row();
        localTable.add().height(1200/ratio/3).width(1200/ratio/3);
        localTable.add();
        localTable.add().height(1200/ratio/3).width(1200/ratio/3);

//        localTable.debug();

        Table main = new Table();
        main.add(localTable).padTop(25f).padRight(25f).padLeft(25f).padBottom(25f).fill();
        main.row();
//        main.debug();
        main.setFillParent(true);

        return main;

    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.D) {
            forklift.moveForkliftRight();
            return true;
        }

        if (keycode == Input.Keys.A) {
            forklift.moveForkliftLeft();
            return true;
        }

        if (keycode == Input.Keys.W) {
            forklift.moveTubeUp();
            return true;
        }

        if (keycode == Input.Keys.S) {
            forklift.moveTubeDown();
            return true;
        }

        if (keycode == Input.Keys.E) {
            forklift.rotateForkUp();
            return true;
        }

        if (keycode == Input.Keys.Q) {
            forklift.rotateForkDown();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.D) {
            forklift.stopMoveForkliftRight();
            return true;
        }

        if (keycode == Input.Keys.A) {
            forklift.stopMoveForkliftLeft();
            return true;
        }

        if (keycode == Input.Keys.W) {
            forklift.stopMoveTubeUp();
            return true;
        }

        if (keycode == Input.Keys.S) {
            forklift.stopMoveTubeDown();
            return true;
        }

        if (keycode == Input.Keys.E) {
            forklift.stopRotatingFork();
            return true;
        }

        if (keycode == Input.Keys.Q) {
            forklift.stopRotatingFork();
            return true;
        }
        return false;
    }

    private void cameraUpdate(float delta) {
        Vector3 position = camera.position;

        // Interpolation implemented
        position.x = camera.position.x + (forklift.getFrokPosition().x - camera.position.x) * 0.1f;
        position.y = camera.position.y + ((forklift.getFrokPosition().y + 1.5f) - camera.position.y) * 0.1f;

        camera.position.set(position);

        camera.update();
    }

    public Inventory getInv() {
        return inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
