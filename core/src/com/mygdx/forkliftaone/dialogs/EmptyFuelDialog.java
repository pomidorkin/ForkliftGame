package com.mygdx.forkliftaone.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.entity.ForkliftActorBase;
import com.mygdx.forkliftaone.maps.MapBase;
import com.mygdx.forkliftaone.screens.game.GameScreen;
import com.mygdx.forkliftaone.screens.menu.MenuScreen;
import com.mygdx.forkliftaone.screens.purchase.PurchaseScreen;
import com.mygdx.forkliftaone.utils.Inventory;
import com.mygdx.forkliftaone.utils.ProcessInventory;
import com.mygdx.forkliftaone.utils.ProcessInventoryImproved;

public class EmptyFuelDialog extends Dialog {
    ForkLiftGame game;
    GameScreen gs;
    ForkliftActorBase forklift;
//    ProcessInventory pi = new ProcessInventory();
ProcessInventoryImproved pi = new ProcessInventoryImproved();
    Inventory inv;
    MapBase map;

    public EmptyFuelDialog(ForkLiftGame game, GameScreen gs, ForkliftActorBase forklift, MapBase map, String title, Skin skin) {
        super(title, skin);
        this.game = game;
        this.gs = gs;
        this.inv = pi.read();
        this.map = map;
        this.forklift = forklift;
    }

    {
        text("Fuel tank is empty");
        button("Menu", 1);
        button("Buy fuel", 2);
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
        } else if (object.equals(2)) {
            if (inv.getDonateCurrency() - 10 < 0){
                game.setScreen(new PurchaseScreen(game));
            }else {
                Inventory inv2 = new Inventory(inv.getBalance(),
                        inv.getDonateCurrency() - 10,
                        inv.isDonateBoxesPurchased(), inv.isTutorialPassed(), inv.getAllModels(), inv.getAllMaps(), inv.getSd());
                pi.write(inv2);
                forklift.setFuelTank(100f);
                gs.setInv(pi.read());
                remove();
                gs.setGamePaused(false);
            }
        }
    }
}
