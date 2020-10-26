package com.mygdx.forkliftaone.screens.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.screens.market.MarketScreen;
import com.mygdx.forkliftaone.screens.menu.MenuScreen;
import com.mygdx.forkliftaone.screens.menu.MenuScreenBase;
import com.mygdx.forkliftaone.utils.Inventory;
import com.mygdx.forkliftaone.utils.ProcessInventoryImproved;
import com.mygdx.forkliftaone.utils.SettingsData;

public class OptionsScreen extends MenuScreenBase {
    private Skin skin;
    private Table table;
    private Slider slider, soundSlider;
    private SettingsData sd;

    ProcessInventoryImproved pi = new ProcessInventoryImproved();
    private Inventory inv;

    public OptionsScreen(ForkLiftGame game) {
        super(game);
        inv = pi.read();
        sd = inv.getSd();
    }

    @Override
    protected Actor createUi() {
        skin = new Skin(Gdx.files.internal("custom/CustomSkinUI.json"));
//        skin = new Skin(Gdx.files.internal("neon/neon-ui.json"));
        table = new Table();

        slider = new Slider(0, 1f, 0.1f, false, skin);
        slider.setValue(inv.getSd().getMusicVolume());

        soundSlider = new Slider(0, 1f, 0.1f, false, skin);
        soundSlider.setValue(inv.getSd().getSoundVolume());

        TextButton saveButton = new TextButton("Save", skin);
        saveButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Saving
                sd.setMusicVolume(slider.getValue());
                sd.setSoundVolume(soundSlider.getValue());

                Inventory inv2 = new Inventory(inv.getBalance(),
                        inv.getDonateCurrency(), inv.isDonateBoxesPurchased(), inv.isTutorialPassed(),
                        inv.getAllModels(), inv.getAllMaps(), sd);
                pi.write(inv2);

                game.setScreen(new MenuScreen(game));
            }
        });

        TextField musicText = new TextField("Music volume", skin);
        TextField soundText = new TextField("Sound volume", skin);

        table.add(musicText).padBottom(30).width(1200/5);
        table.row();
        table.add(slider).padBottom(30).width(1200/5);
        table.row();
        table.add(soundText).padBottom(30).width(1200/5);
        table.row();
        table.add(soundSlider).padBottom(30).width(1200/5);
        table.row();
        table.add(saveButton);

//        table.debug();

        Table main = new Table();
        main.add(table).padTop(20f).padRight(20f).padLeft(20f).fill();
        main.row();
        main.setFillParent(true);

        return main;
    }
}
