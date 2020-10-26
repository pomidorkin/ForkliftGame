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
import com.mygdx.forkliftaone.dialogs.MessageDialog;
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

public class ForkliftMarketScreen extends ScreenAdapter {

    private ForkLiftGame game;
    private Skin skin;
    private Table table;
    private TextButton buyButton, backButton;
    //    ProcessInventory pi = new ProcessInventory();
    ProcessInventoryImproved pi = new ProcessInventoryImproved();
    private Inventory inv;
    private GeneralData gd;
    private ForkliftData forkliftData;
    private SpriteBatch batch;
    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ForkliftModel model;
    private AssetManager assetManager;
    private TextureAtlas gamePlayAtlas;
    private TextureRegion backgroundPlaceholder;

    private final GlyphLayout layout = new GlyphLayout();
    private BitmapFont font;
    private TextureRegion coinTexture, gemTexture;

    private List<ForkliftData> unpurchasedForklifts;

    private int counter;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
    float ratio = width / height;


    public ForkliftMarketScreen(ForkLiftGame game) {
        this.game = game;
        this.counter = 0;
        assetManager = game.getAssetManager();
        batch = game.getBatch();
        unpurchasedForklifts = new ArrayList<>();
        inv = pi.read();
        gd = pi.readGeneralData();
        this.forkliftData = gd.getAllModels()[0];

        System.out.println(forkliftData.getPrice());

    }

    public ForkliftMarketScreen(ForkLiftGame game, int counter) {
        this.game = game;
        this.counter = counter;
        assetManager = game.getAssetManager();
        batch = game.getBatch();
        unpurchasedForklifts = new ArrayList<>();
        inv = pi.read();
        gd = pi.readGeneralData();
        this.forkliftData = gd.getAllModels()[counter];

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

        if (unpurchasedForklifts.size() <= 0) {
            model = null;
        } else {
            model = new ForkliftModel(unpurchasedForklifts.get(counter), gamePlayAtlas);
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

        for (final ForkliftData fd : gd.getAllModels()) {
            boolean purchased = false;

            // Checking if a forklift from general data exists in inventory
            for (ForkliftData fdd : inv.getAllModels()) {
                if (fdd.getName() == fd.getName()) {
                    purchased = true;
                }
            }
            if (!purchased) {
                unpurchasedForklifts.add(fd);
            }
        }

        // Forklift scrolling buttons logic
        if (unpurchasedForklifts.size() > 0) {

            forkliftData = unpurchasedForklifts.get(counter);

            ImageButton nextTB = new ImageButton(skin.get("rightButton", ImageButton.ImageButtonStyle.class));
//            TextButton nextTB = new TextButton("Next", skin);
            nextTB.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if (unpurchasedForklifts.size() != 0) {
                        if (counter + 1 == unpurchasedForklifts.size()) {
                            counter = 0;
                        } else {
                            counter++;
                        }
                    }

                    System.out.println("counter = " + counter);


//                    forkliftData = unpurchasedForklifts.get(counter);

                    // Refreshing market screen
                    game.setScreen(new ForkliftMarketScreen(game, counter));

//                forkliftData = fdArray.get(counter);
                    System.out.println("Size of unpurchasedForklifts " + unpurchasedForklifts.size());

                }
            });

            // "Previous button"
            ImageButton previousTB = new ImageButton(skin.get("leftButton", ImageButton.ImageButtonStyle.class));
//            TextButton previousTB = new TextButton("Previous", skin);
            previousTB.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if (unpurchasedForklifts.size() != 0) {
                        if (counter == 0) {
                            counter = unpurchasedForklifts.size() - 1;
                        } else {
                            counter--;
                        }
                    }

                    System.out.println("counter = " + counter);


//                    forkliftData = unpurchasedForklifts.get(counter);

                    // Refreshing market screen
                    game.setScreen(new ForkliftMarketScreen(game, counter));

//                forkliftData = fdArray.get(counter);
                    System.out.println("Size of unpurchasedForklifts " + unpurchasedForklifts.size());

                }
            });

            // Button depends on the currency type (coins/gems)
//            TextButton buyButton = new TextButton("Buy " + forkliftData.getPrice().getPrice() + "$", skin);

            TextButton buyButton;
            if (!forkliftData.getPrice().isDonateCurrency()) {
//                 buyButton = new TextButton("Buy " + forkliftData.getPrice().getPrice() + "$", skin);
                buyButton = new TextButton("Buy " + forkliftData.getPrice().getPrice() + "$",
                        skin.get("dollarButton", TextButton.TextButtonStyle.class));

            } else {
                buyButton = new TextButton("Buy " + forkliftData.getPrice().getPrice() + " gems",
                        skin.get("gemButton", TextButton.TextButtonStyle.class));
            }
            buyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    forkliftData = unpurchasedForklifts.get(counter);
                    if (!forkliftData.getPrice().isDonateCurrency()) {
                        if (inv.getBalance() >= forkliftData.getPrice().getPrice()) {
                            inv.setBalance(inv.getBalance() - forkliftData.getPrice().getPrice());
//                    forkliftData.setPurchased(true);

                            forkliftData.setPurchased(true);

                            // Saving models
                            inv.getAllModels().add(forkliftData);

                            // Saving
                            Inventory inv2 = new Inventory(inv.getBalance(),
                                    inv.getDonateCurrency(), inv.isDonateBoxesPurchased(), inv.isTutorialPassed(),
                                    inv.getAllModels(), inv.getAllMaps(), inv.getSd());
                            pi.write(inv2);

                            GeneralData gd2 = new GeneralData(gd.getAllModels(), gd.getAllMaps());
                            pi.write(gd2);

                            // Refreshing market screen
                            game.setScreen(new ForkliftMarketScreen(game));
                        } else {
                            MessageDialog menuDialog = new MessageDialog(game, "", "Not enough money", skin);
                            menuDialog.show(stage);
                            menuDialog.setWidth(1200 / 2);
                            menuDialog.setHeight(1200 / ratio / 2);
                            menuDialog.setPosition(1200 / 2f - (menuDialog.getWidth() / 2), 1200 / ratio / 2f - (menuDialog.getHeight() / 2));
                            menuDialog.setMovable(false);
                        }
                    } else {
                        if (inv.getDonateCurrency() >= forkliftData.getPrice().getPrice()) {
                            inv.setDonateCurrency(inv.getDonateCurrency() - forkliftData.getPrice().getPrice());
//                    forkliftData.setPurchased(true);

                            forkliftData.setPurchased(true);

                            // Saving models
                            inv.getAllModels().add(forkliftData);

                            // Saving
                            Inventory inv2 = new Inventory(inv.getBalance(),
                                    inv.getDonateCurrency(), inv.isDonateBoxesPurchased(), inv.isTutorialPassed(),
                                    inv.getAllModels(), inv.getAllMaps(), inv.getSd());
                            pi.write(inv2);

                            GeneralData gd2 = new GeneralData(gd.getAllModels(), gd.getAllMaps());
                            pi.write(gd2);

                            // Refreshing market screen
                            game.setScreen(new ForkliftMarketScreen(game));
                        } else {
                            MessageDialog menuDialog = new MessageDialog(game, "", "Not enough money", skin);
                            menuDialog.show(stage);
                            menuDialog.setWidth(1200 / 2);
                            menuDialog.setHeight(1200 / ratio / 2);
                            menuDialog.setPosition(1200 / 2f - (menuDialog.getWidth() / 2), 1200 / ratio / 2f - (menuDialog.getHeight() / 2));
                            menuDialog.setMovable(false);
                        }
                    }

                }
            });

            table.row();
            table.add(previousTB).padBottom(30).padRight(30);
            table.add(buyButton).padBottom(30).padRight(30);
            table.add(nextTB).padBottom(30);
        } else {
            System.out.println("No forklifts to buy");
            // Code telling that everything is purchased here
        }

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setScreen(new MenuScreen(game));
            }
        });

        table.row();
        table.add(backButton).colspan(3).padBottom(30);
//        table.debug();


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

        batch.draw(backgroundPlaceholder, 0, 0,
                viewport.getWorldWidth(), viewport.getWorldHeight());


        if (model != null) {

            batch.draw(model.getForkliftRegion(), // Texture
                    1200 / 4f - (model.getForkliftRegion().getRegionWidth() / 2f) * 0.75f,
                    1200 / ratio / 2f - (model.getForkliftRegion().getRegionHeight() / 2f) * 0.75f, // Texture position
                    model.getForkliftRegion().getRegionWidth() / 2f, model.getForkliftRegion().getRegionHeight() / 2f, // Rotation point (width / 2, height /2 = center)
                    model.getForkliftRegion().getRegionWidth() * 0.75f, model.getForkliftRegion().getRegionHeight() * 0.75f, // Width and height of the texture
                    1f, 1f, //scaling
                    0); // Rotation (radiants to degrees)


        }


        // draw balance
        String balanceText = "" + inv.getBalance();
        layout.setText(font, balanceText);
//        font.draw(batch, layout, (uiViewport.getScreenHeight() / 10f) * 1.5f, height - uiViewport.getScreenHeight() / 10f / 2);
//        font.draw(batch, layout, (1200 / ratio / 10f) * 1.5f, 1200 / ratio - 1200 / ratio / 10f / 2);
        font.draw(batch, layout, ((1200/ratio)  / 10f) * 1.5f, (1200/ratio) - (1200/ratio) / 10f - ((1200/ratio) / 20f) + (((1200/ratio) / 10f) * 0.70f));


        // draw gems
        String donate = "" + inv.getDonateCurrency();
        layout.setText(font, donate);
//        font.draw(batch, layout,
//                (1200 / ratio / 10f) * 1.5f,
//                1200 / ratio - 1200 / ratio / 6f
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
//                10f, 1200 / ratio - 1200 / ratio / 10f - 10f, // Texture position
//                coinTexture.getRegionWidth() / 2, coinTexture.getRegionHeight() / 2, // Rotation point (width / 2, height /2 = center)
//                1200 / ratio / 10f, 1200 / ratio / 10f, // Width and height of the texture
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