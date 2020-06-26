package com.mygdx.forkliftaone.screens.market;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.ForkliftModel;
import com.mygdx.forkliftaone.screens.menu.MenuScreen;
import com.mygdx.forkliftaone.utils.AssetDescriptors;
import com.mygdx.forkliftaone.utils.ForkliftData;
import com.mygdx.forkliftaone.utils.GeneralData;
import com.mygdx.forkliftaone.utils.Inventory;
import com.mygdx.forkliftaone.utils.MapData;
import com.mygdx.forkliftaone.utils.MapModel;
import com.mygdx.forkliftaone.utils.ProcessInventoryImproved;
import com.mygdx.forkliftaone.utils.RegionNames;

import java.util.ArrayList;
import java.util.List;

public class MapMarketScreen extends ScreenAdapter {

    private ForkLiftGame game;
    private Skin skin;
    private Table table;
    private TextButton backButton;
    //    ProcessInventory pi = new ProcessInventory();
    ProcessInventoryImproved pi = new ProcessInventoryImproved();
    private Inventory inv;
    private GeneralData gd;
    private MapData mapData;
    private SpriteBatch batch;
    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private MapModel mapModel;
    private AssetManager assetManager;
    private TextureAtlas gamePlayAtlas;
    private TextureRegion backgroundPlaceholder;

    private final GlyphLayout layout = new GlyphLayout();
    private BitmapFont font;
    private TextureRegion coinTexture, gemTexture;

    private List<MapData> unpurchasedMaps;

    private int counter;
    private int mapCounter;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
    float ratio = width / height;

    public MapMarketScreen(ForkLiftGame game) {
        this.game = game;
        this.counter = 0;
        this.mapCounter = 0;
        assetManager = game.getAssetManager();
        batch = game.getBatch();
        unpurchasedMaps = new ArrayList<>();
        inv = pi.read();
        gd = pi.readGeneralData();


    }

    public MapMarketScreen(ForkLiftGame game, int mapCounter) {
        this.game = game;
        this.mapCounter = mapCounter;
        assetManager = game.getAssetManager();
        batch = game.getBatch();
        unpurchasedMaps = new ArrayList<>();
        inv = pi.read();
        gd = pi.readGeneralData();

//        model = new ForkliftModel(unpurchasedForklifts.get(counter), gamePlayAtlas);
    }

    @Override
    public void show() {
        super.show();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1200, 1200 / ratio, camera);
        stage = new Stage(viewport, game.getBatch());
        gamePlayAtlas = assetManager.get(AssetDescriptors.TEST_ATLAS);

        stage.addActor(createUi());

        font = assetManager.get(AssetDescriptors.FONT);
        font.getData().setScale(1f);
        coinTexture = gamePlayAtlas.findRegion(RegionNames.COIN_TEXTURE);
        gemTexture = gamePlayAtlas.findRegion(RegionNames.GEM_TEXTURE);

        if (unpurchasedMaps.size() <= 0) {
            mapModel = null;
        } else {
            mapModel = new MapModel(unpurchasedMaps.get(mapCounter).getName(), gamePlayAtlas);
        }
        backgroundPlaceholder = gamePlayAtlas.findRegion(RegionNames.TEST_BACKGROUND);
        Gdx.input.setInputProcessor(stage);

    }

    protected Actor createUi() {
        skin = new Skin(Gdx.files.internal("custom/CustomSkinUI.json"));

        table = new Table();
//        table.setWidth(1200);
//        table.align(Align.center | Align.top);
//        table.setPosition(0, 1200 / ratio);

        // Testing cycle for showing models
        // Testing buying is working
        // Scrolling market

        for (final MapData md : gd.getAllMaps()) {
            boolean mapPurchased = false;

            // Checking if a forklift from general data exists in inventory
            for (MapData mdd : inv.getAllMaps()) {
                if (mdd.getName() == md.getName()) {
                    mapPurchased = true;
                }
            }
            if (!mapPurchased) {
                unpurchasedMaps.add(md);
            }
        }

        // Maps buying scrolling logic
        if (unpurchasedMaps.size() > 0) {

            mapData = unpurchasedMaps.get(mapCounter);

            ImageButton nextMapTB = new ImageButton(skin.get("rightButton", ImageButton.ImageButtonStyle.class));
//            TextButton nextMapTB = new TextButton("Next map", skin);
            nextMapTB.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if (unpurchasedMaps.size() != 0) {
                        if (mapCounter + 1 == unpurchasedMaps.size()) {
                            mapCounter = 0;
                        } else {
                            mapCounter++;
                        }
                    }

                    System.out.println("Map counter = " + mapCounter);


//                    mapData = unpurchasedMaps.get(mapCounter);

                    // Refreshing market screen
                    game.setScreen(new MapMarketScreen(game, mapCounter));

//                forkliftData = fdArray.get(counter);
                    System.out.println("Size of unpurchased maps " + unpurchasedMaps.size());

                }
            });

            ImageButton previousMapTB = new ImageButton(skin.get("leftButton", ImageButton.ImageButtonStyle.class));
            previousMapTB.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if (unpurchasedMaps.size() != 0) {
                        if (mapCounter == 0) {
                            mapCounter = unpurchasedMaps.size() - 1;
                        } else {
                            mapCounter--;
                        }
                    }

                    System.out.println("Map counter = " + mapCounter);


//                    mapData = unpurchasedMaps.get(mapCounter);

                    // Refreshing market screen
                    game.setScreen(new MapMarketScreen(game, mapCounter));

//                forkliftData = fdArray.get(counter);
                    System.out.println("Size of unpurchased maps " + unpurchasedMaps.size());

                }
            });

            // Button depends on the currency type (coins/gems)
//            TextButton buyMapButton = new TextButton("Buy Map " + mapData.getPrice() + "$", skin);

            TextButton buyMapButton;

            if (!mapData.getPrice().isDonateCurrency()){
                buyMapButton = new TextButton("Buy Map " + mapData.getPrice().getPrice() + "$", skin);
            } else {
                buyMapButton = new TextButton("Buy Map " + mapData.getPrice().getPrice() + " gems", skin);
            }

            buyMapButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    mapData = unpurchasedMaps.get(mapCounter);
                    if (!mapData.getPrice().isDonateCurrency()) {
                        if (inv.getBalance() >= mapData.getPrice().getPrice()) {

                            inv.setBalance(inv.getBalance() - mapData.getPrice().getPrice());


                            mapData.setPurchased(true);

                            // Saving models
                            inv.getAllMaps().add(mapData);

                            // Saving
                            Inventory inv2 = new Inventory(inv.getBalance(), inv.getDonateCurrency(), inv.isTutorialPassed(),
                                    inv.isDonateBoxesPurchased(), inv.getAllModels(), inv.getAllMaps(), inv.getSd());
                            pi.write(inv2);

                            GeneralData gd2 = new GeneralData(gd.getAllModels(), gd.getAllMaps());
                            pi.write(gd2);

                            // Refreshing market screen
                            game.setScreen(new MapMarketScreen(game));
                        } else {
                            System.out.println("Not enough money. Map price:" + mapData.getPrice());
                        }
                    }else {
                        if (inv.getDonateCurrency() >= mapData.getPrice().getPrice()) {

                            inv.setDonateCurrency(inv.getDonateCurrency() - mapData.getPrice().getPrice());


                            mapData.setPurchased(true);

                            // Saving models
                            inv.getAllMaps().add(mapData);

                            // Saving
                            Inventory inv2 = new Inventory(inv.getBalance(), inv.getDonateCurrency(), inv.isTutorialPassed(),
                                    inv.isDonateBoxesPurchased(), inv.getAllModels(), inv.getAllMaps(), inv.getSd());
                            pi.write(inv2);

                            GeneralData gd2 = new GeneralData(gd.getAllModels(), gd.getAllMaps());
                            pi.write(gd2);

                            // Refreshing market screen
                            game.setScreen(new MapMarketScreen(game));
                        } else {
                            System.out.println("Not enough money. Map price:" + mapData.getPrice());
                        }
                    }

                }
            });

            table.row();
            table.add(previousMapTB).padBottom(30).padRight(30);
            table.add(buyMapButton).padBottom(30).padRight(30);
            table.add(nextMapTB).padBottom(30);
        } else {
            System.out.println("No maps to buy");
            // Code telling that everything is purchased here
        }


        // Other buttons

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setScreen(new MenuScreen(game));
            }
        });


        table.row();
        table.add(backButton).colspan(3).padBottom(30);
        table.debug();

        Table main = new Table();
        main.add().width(1200 / 2f).height(1200 / ratio);
        main.add(table).fill();
        main.row();
//        main.debug();
        main.setFillParent(true);

        return main;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw();
        stage.draw();

    }

    private void draw() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

//        batch.draw(backgroundTexture, 0, 0,
//                viewport.getWorldWidth(), viewport.getWorldHeight());

        if (mapModel != null) {
            batch.draw(mapModel.getBackgroundTexture(), 0, 0,
                    viewport.getWorldWidth(), viewport.getWorldHeight());
        } else {
            batch.draw(backgroundPlaceholder, 0, 0,
                    viewport.getWorldWidth(), viewport.getWorldHeight());
        }


        // draw balance
        String balanceText = "" + inv.getBalance();
        layout.setText(font, balanceText);
//        font.draw(batch, layout, (uiViewport.getScreenHeight() / 10f) * 1.5f, height - uiViewport.getScreenHeight() / 10f / 2);
//        font.draw(batch, layout, (1200/ratio / 10f) * 1.5f, 1200/ratio - 1200/ratio  / 10f / 2);
        font.draw(batch, layout, ((1200/ratio)  / 10f) * 1.5f, (1200/ratio) - (1200/ratio) / 10f - ((1200/ratio) / 20f) + (((1200/ratio) / 10f) * 0.70f));

        // draw gems
        String donate = "" + inv.getDonateCurrency();
        layout.setText(font, donate);
//        font.draw(batch, layout,
//                (1200/ratio / 10f) * 1.5f,
//                1200/ratio - 1200/ratio / 6f
//        );
        font.draw(batch, layout,
                ((1200/ratio)  / 10f) * 1.5f,
                (1200/ratio) - (((1200/ratio) / 10f - ((1200/ratio) / 20f)) + (1200/ratio) / 10f * 2.1f)  + (((1200/ratio) / 10f) * 0.70f)
        );

        // draw balance
//        String balanceText = "" + inv.getBalance();
//        layout.setText(font, balanceText);
////        font.draw(batch, layout, (uiViewport.getScreenHeight() / 10f) * 1.5f, height - uiViewport.getScreenHeight() / 10f / 2);
//        font.draw(batch, layout, (height  / 10f) * 1.5f, height - height / 10f - (height / 20f) + ((height / 10f) * 0.70f));
//
//        // draw gems
//        String donate = "" + inv.getDonateCurrency();
//        layout.setText(font, donate);
//        font.draw(batch, layout,
//                (height  / 10f) * 1.5f,
//                height - ((height / 10f - (height / 20f)) + height / 10f * 2.1f)  + ((height / 10f) * 0.70f)
//        );

        // Drawing coin image
//        batch.draw(coinTexture, // Texture
//                10f, 1200/ratio - 1200/ratio / 10f - 10f, // Texture position
//                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
//                1200/ratio / 10f, 1200/ratio / 10f, // Width and height of the texture
//                1f, 1f, //scaling
//                0); // Rotation (radiants to degrees)
//
//        // Drawing gem image
//        batch.draw(gemTexture, // Texture
//                10f, 1200/ratio - ((1200/ratio / 10f - 10f) + coinTexture.getRegionHeight() * 1.5f), // Texture position
//                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
//                1200/ratio / 10f, 1200/ratio / 10f, // Width and height of the texture
//                1f, 1f, //scaling
//                0); // Rotation (radiants to degrees)

        batch.draw(coinTexture, // Texture
                10f, (1200/ratio) - (1200/ratio) / 10f - ((1200/ratio) / 20f), // Texture position
                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
                (1200/ratio) / 10f, (1200/ratio) / 10f, // Width and height of the texture
                1f, 1f, //scaling
                0); // Rotation (radiants to degrees)

        batch.draw(gemTexture, // Texture
                10f, (1200/ratio) - (((1200/ratio) / 10f - ((1200/ratio) / 20f)) + (1200/ratio) / 10f * 2.1f), // Texture position
                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
                (1200/ratio) / 10f, (1200/ratio) / 10f, // Width and height of the texture
                1f, 1f, //scaling
                0); // Rotation (radiants to degrees)

        // Drawing coin image
//        batch.draw(coinTexture, // Texture
//                10f, height - height / 10f - (height / 20f), // Texture position
//                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
//                height / 10f, height / 10f, // Width and height of the texture
//                1f, 1f, //scaling
//                0); // Rotation (radiants to degrees)
//
//        // Drawing gem image
//        batch.draw(gemTexture, // Texture
//                10f, height - ((height / 10f - (height / 20f)) + height / 10f * 2.1f), // Texture position
//                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
//                height / 10f, height / 10f, // Width and height of the texture
//                1f, 1f, //scaling
//                0); // Rotation (radiants to degrees)

        batch.end();
    }

    @Override
    public void hide() {
        super.hide();
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
    }


}
