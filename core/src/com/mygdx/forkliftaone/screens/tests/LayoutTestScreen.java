package com.mygdx.forkliftaone.screens.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.screens.menu.MenuScreenBase;

public class LayoutTestScreen extends MenuScreenBase {

    private Skin skin;
    private Table table;
    private TextButton testButton, testTwo, testThree, testFour;

    public LayoutTestScreen(ForkLiftGame game) {
        super(game);
    }

    @Override
    protected Actor createUi() {
        skin = new Skin(Gdx.files.internal("neon/neon-ui.json"));

//        table = new Table();
//        table.setWidth(Gdx.graphics.getWidth());
//        table.setHeight(0);
//        table.align(Align.center | Align.top);
//        table.setPosition(0, Gdx.graphics.getHeight());

        Table header = new Table();

        testButton = new TextButton("Button one", skin);
        testButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Test button clicked");
            }
        });

        testTwo = new TextButton("Button two", skin);
        testThree = new TextButton("Button three", skin);
        testFour = new TextButton("One more button", skin);

//        header.padTop(30f);
//        table.add(testButton).expandX();
        header.add(testButton).width(150f).height(150f).expandX().align(Align.left);
        header.add(testTwo).width(150f).height(150f).expandX().align(Align.center);
        header.add(testThree).width(150f).height(150f).expandX().align(Align.right);

        Table main = new Table();
        main.add(header).fill();
        main.row();
        main.add(testFour).expandX().fill();
        main.debug();
        main.setFillParent(true);

        return main;
    }
}
