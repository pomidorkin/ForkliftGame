package com.mygdx.forkliftaone.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.maps.MapBase;
import com.mygdx.forkliftaone.screens.game.GameScreen;
import com.mygdx.forkliftaone.screens.menu.MenuScreen;
import com.mygdx.forkliftaone.utils.Inventory;
import com.mygdx.forkliftaone.utils.ProcessInventoryImproved;

public class BackToMenuDialog extends Dialog {
    ForkLiftGame game;
    GameScreen gs;
    //    ProcessInventory pi = new ProcessInventory();
    ProcessInventoryImproved pi = new ProcessInventoryImproved();
    Inventory inv;
    MapBase map;

//     Skin skin;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
    float ratio = width / height;

    public BackToMenuDialog(ForkLiftGame game, GameScreen gs, MapBase map, String title, Skin skin) {
        super(title, skin);
        this.game = game;
        this.gs = gs;
        this.inv = pi.read();
        this.map = map;

//        this.skin = new Skin(Gdx.files.internal("custom/CustomSkinUI.json"));
    }

//    {
//        add().height(((Gdx.graphics.getHeight() / 2f) / 3f));
//        text("Do you want to go back to the menu?");
//        row();
//        button("Menu", 1).setHeight(((Gdx.graphics.getHeight() / 2f) / 3f));
//        button("Continue", 2);
//        row();
//        add().height(((Gdx.graphics.getHeight() / 2f) / 4f));
//
//        debug();
//    }

    {
        add().height(((1200 / ratio / 2f) / 3f));
        text("Do you want to go back to the menu?");
//        text("Do you want to go back to the menu?", skin.get(Label.LabelStyle.class));
        row();
        button("Menu", 1);
        button("Continue", 2);
        row();
        add().height(((1200 / ratio / 2f) / 4f));

        debug();
    }

    @Override
    protected void result(Object object) {
        super.result(object);
        if (object.equals(1)) {
            Inventory inv2 = new Inventory(inv.getBalance() + map.getSalary(),
                    inv.getDonateCurrency() + map.getDonateSalary(),
                    inv.isDonateBoxesPurchased(), inv.isTutorialPassed(), inv.getAllModels(), inv.getAllMaps(), inv.getSd());
            pi.write(inv2);
            game.setScreen(new MenuScreen(game));
            game.setScreen(new MenuScreen(game));
        } else if (object.equals(2)) {
            remove();
            gs.setGamePaused(false);
        }
    }
}
