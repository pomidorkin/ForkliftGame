package com.mygdx.forkliftaone.screens.market;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.screens.menu.MenuScreen;
import com.mygdx.forkliftaone.screens.menu.MenuScreenBase;
import com.mygdx.forkliftaone.screens.options.OptionsScreen;

public class ChooseMarketScreen extends MenuScreenBase {
    private Skin skin;
    private Table table;
    private TextButton forkliftMarket, mapMarket, backButton;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
    float ratio = width / height;

    public ChooseMarketScreen(ForkLiftGame game){
        super(game);
    };

    @Override
    protected Actor createUi() {
//        skin = new Skin(Gdx.files.internal("uiskin.json"));
//        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
//        skin = new Skin(Gdx.files.internal("neon/neon-ui.json"));
//        skin = new Skin(Gdx.files.internal("freezing/freezing-ui.json"));
        // При использовании Скин Композера ОБЯЗАТЕЛЬНО указывать зависимости на тен патч
        skin = new Skin(Gdx.files.internal("custom/CustomSkinUI.json"));

        table = new Table();
//        table.setWidth(Gdx.graphics.getWidth());
//        table.align(Align.center | Align.top);
//        table.setPosition(0, Gdx.graphics.getHeight());

        forkliftMarket = new TextButton("Forklift market", skin);

        // Font scaling
//        startButton.getStyle().font.getData().setScale(2f);

        forkliftMarket.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openForkliftMarket();
            }
        });

        mapMarket = new TextButton("Map market", skin);
        mapMarket.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openMapMarket();
            }
        });

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goBack();
            }
        });

        table.add(forkliftMarket).padBottom(15).height((1200/ratio)/6);
        table.row();
        table.add(mapMarket).padBottom(15).height((1200/ratio)/6);
        table.row();
        table.add(backButton).padBottom(15).height((1200/ratio)/6);

//        table.debug();

        Table main = new Table();
        main.add(table).padTop(20f).padRight(20f).padLeft(20f).fill();
//        main.debug();
        main.setFillParent(true);

        return main;
    }

    private void openForkliftMarket(){
        game.setScreen(new ForkliftMarketScreen(game));
    }
    private void openMapMarket(){
        game.setScreen(new MapMarketScreen(game));
    }
    private void goBack(){
        game.setScreen(new MenuScreen(game));
    }
}
