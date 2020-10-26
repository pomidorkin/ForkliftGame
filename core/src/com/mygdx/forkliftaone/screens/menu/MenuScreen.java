package com.mygdx.forkliftaone.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.screens.game.ChoosingScreen;
import com.mygdx.forkliftaone.screens.game.GameScreen;
import com.mygdx.forkliftaone.screens.market.ChooseMarketScreen;
import com.mygdx.forkliftaone.screens.market.MarketScreen;
import com.mygdx.forkliftaone.screens.options.OptionsScreen;
import com.mygdx.forkliftaone.screens.purchase.PurchaseScreen;

public class MenuScreen extends MenuScreenBase {
    private Skin skin;
    private Table table;
    private TextButton startButton, quitButton, marketButton, donateButton, optionsButton;

    public MenuScreen(ForkLiftGame game){super(game);}

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

        startButton = new TextButton("Start Game", skin);

        // Font scaling
//        startButton.getStyle().font.getData().setScale(2f);

        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                play();
            }
        });

        marketButton = new TextButton("Market", skin);
        marketButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                shop();
            }
        });

        donateButton = new TextButton("Donate market", skin);
        donateButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openDonate();
            }
        });

        optionsButton = new TextButton("Options", skin);
        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                options();
            }
        });

        quitButton = new TextButton("Quit Game", skin);
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quit();
            }
        });

        table.add(startButton).padBottom(15).height((1200/ratio)/6);
        table.row();
        table.add(marketButton).padBottom(15).height((1200/ratio)/6);
        table.row();
        table.add(donateButton).padBottom(15).height((1200/ratio)/6);
        table.row();
        table.add(optionsButton).padBottom(15).height((1200/ratio)/6);
        table.row();
        table.add(quitButton).height((1200/ratio)/6);
//        table.debug();

        Table main = new Table();
        main.add(table).padTop(20f).padRight(20f).padLeft(20f).fill();
//        main.debug();
        main.setFillParent(true);

        return main;
    }



    private void play(){
        game.setScreen(new ChoosingScreen(game));
    }

//    private void shop(){
//        game.setScreen(new MarketScreen(game));
//    }

    private void shop(){
        game.setScreen(new ChooseMarketScreen(game));
    }

    private void openDonate(){
        game.setScreen(new PurchaseScreen(game));
    }

    private void options(){
        game.setScreen(new OptionsScreen(game));
    }

    private void quit() {
        Gdx.app.exit();
    }
}
