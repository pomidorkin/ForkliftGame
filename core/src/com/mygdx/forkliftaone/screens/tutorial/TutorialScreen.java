package com.mygdx.forkliftaone.screens.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.screens.market.ChooseMarketScreen;
import com.mygdx.forkliftaone.screens.menu.MenuScreen;
import com.mygdx.forkliftaone.screens.menu.MenuScreenBase;
import com.mygdx.forkliftaone.screens.options.OptionsScreen;
import com.mygdx.forkliftaone.screens.purchase.PurchaseScreen;
import com.mygdx.forkliftaone.utils.AssetDescriptors;
import com.mygdx.forkliftaone.utils.Inventory;
import com.mygdx.forkliftaone.utils.ProcessInventoryImproved;
import com.mygdx.forkliftaone.utils.RegionNames;

public class TutorialScreen extends MenuScreenBase {

    private Skin skin;
    private Table table;
    private TextButton nextButton, quitButton, marketButton, donateButton, optionsButton;
    TextureAtlas atlas = assetManager.get(AssetDescriptors.TEST_ATLAS);


    private TextureRegion[] tutorialTextures;
    private int counter = 0;

    ProcessInventoryImproved pi = new ProcessInventoryImproved();
    private Inventory inv;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
    float ratio = width / height;

    public TutorialScreen(ForkLiftGame game) {
        super(game);

        tutorialTextures = new TextureRegion[3];
        tutorialTextures[0] = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
        tutorialTextures[1] = atlas.findRegion(RegionNames.BOX_TEXTURE);
        tutorialTextures[2] = atlas.findRegion(RegionNames.BIRD_TEXTURE);

        inv = pi.read();

    }

    @Override
    protected Actor createUi() {
        // При использовании Скин Композера ОБЯЗАТЕЛЬНО указывать зависимости на тен патч
        skin = new Skin(Gdx.files.internal("custom/CustomSkinUI.json"));

        table = new Table();

        nextButton = new TextButton("Next", skin);

        // Font scaling

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNextPicture();
            }
        });

        table.add(nextButton).padBottom(15).height((1200 / ratio) / 6);
        table.debug();

        Table main = new Table();
        main.add(table).padTop(20f).padRight(20f).padLeft(20f).fill();
        main.debug();
        main.setFillParent(true);

        return main;
    }

    @Override
    public void draw() {
        super.draw();
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        batch.draw(tutorialTextures[counter], 0, 0,
                viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();
    }


    private void showNextPicture() {
        if (counter >= tutorialTextures.length - 1){
//            counter = 0;

            // Saving
            Inventory inv2 = new Inventory(inv.getBalance(),
                    inv.getDonateCurrency(), inv.isDonateBoxesPurchased(), true,
                    inv.getAllModels(), inv.getAllMaps(), inv.getSd());
            pi.write(inv2);

            game.setScreen(new MenuScreen(game));
        }else {
            counter += 1;
        }
    }

}
