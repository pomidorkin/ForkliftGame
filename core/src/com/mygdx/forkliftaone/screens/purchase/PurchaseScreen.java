package com.mygdx.forkliftaone.screens.purchase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.forkliftaone.ForkLiftGame;
import com.mygdx.forkliftaone.screens.game.ChoosingScreen;
import com.mygdx.forkliftaone.screens.market.MarketScreen;
import com.mygdx.forkliftaone.screens.menu.MenuScreen;
import com.mygdx.forkliftaone.screens.menu.MenuScreenBase;
import com.mygdx.forkliftaone.utils.Inventory;
import com.mygdx.forkliftaone.utils.ProcessInventory;
import com.mygdx.forkliftaone.utils.ProcessInventoryImproved;

public class PurchaseScreen extends MenuScreenBase {
    // A stock-keeping unit (SKU) is a scannable bar code
    public static final String MY_CONSUMABLE = "consumable_sku";
    public static final String MY_BOX = "box_sku";

    private Skin skin;
    private Table table;
    private TextButton backButton;
    private IapButton buyButton, buyBox;
    private Inventory inv;

//    ProcessInventory pi = new ProcessInventory();
ProcessInventoryImproved pi = new ProcessInventoryImproved();

    public PurchaseScreen(ForkLiftGame game) {
        super(game);
        inv = pi.read();
        initPurchaseManager();
    }

    @Override
    protected Actor createUi() {
        // При использовании Скин Композера ОБЯЗАТЕЛЬНО указывать зависимости на тен патч
        skin = new Skin(Gdx.files.internal("custom/CustomSkinUI.json"));
//        skin = new Skin(Gdx.files.internal("neon/neon-ui.json"));

        table = new Table();
//        table.setWidth(Gdx.graphics.getWidth());
//        table.align(Align.center | Align.top);
//        table.setPosition(0, Gdx.graphics.getHeight());

        buyButton = new IapButton(MY_CONSUMABLE, 500);
        buyBox = new IapButton(MY_BOX, 500);


        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goBack();
            }
        });

        table.add(buyButton).padBottom(30);
        table.row();
        table.add(buyBox).padBottom(30);
        table.row();
        table.add(backButton).padBottom(30);

        Table main = new Table();
        main.add(table).padTop(20f).padRight(20f).padLeft(20f).fill();
        main.row();
//        main.debug();
        main.setFillParent(true);

        return main;
    }

    private void initPurchaseManager() {
        // the purchase manager config here in the core project works if your SKUs are the same in every
        // payment system. If this is not the case, inject them like the PurchaseManager is injected
        PurchaseManagerConfig pmc = new PurchaseManagerConfig();
        pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(MY_CONSUMABLE));

        game.purchaseManager.install(new MyPurchaseObserver(), pmc, true);
    }

    private void goBack() {
        game.setScreen(new MenuScreen(game));
    }

    private void quit() {
        Gdx.app.exit();
    }

    private class IapButton extends TextButton {
        private final String sku;
        private final int usdCents;

        public IapButton(String sku, int usdCents) {
            super(sku, skin);
            this.sku = sku;
            this.usdCents = usdCents;

            addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    buyItem();
                }
            });
        }

        private void buyItem() {
            game.purchaseManager.purchase(sku);
        }

        public void setBought(boolean fromRestore) {
            setDisabled(true);
        }

        public void updateFromManager() {
            Information skuInfo = game.purchaseManager.getInformation(sku);

            if (skuInfo == null || skuInfo.equals(Information.UNAVAILABLE)) {
                setDisabled(true);
                setText("Not available");
            } else {
                setText(skuInfo.getLocalName() + " " + skuInfo.getLocalPricing());
            }
        }
    }

    private void updateGuiWhenPurchaseManInstalled(String errorMessage) {
        // einfüllen der Infos
        buyButton.updateFromManager();
        buyBox.updateFromManager();

        if (game.purchaseManager.installed() && errorMessage == null) {
//            restoreButton.setDisabled(false);
        } else {
            errorMessage = (errorMessage == null ? "Error instantiating the purchase system" : errorMessage);
            //TODO show dialog here (happens when no internet connection available)
        }

    }

    private class MyPurchaseObserver implements PurchaseObserver {

        @Override
        public void handleInstall() {
            Gdx.app.log("IAP", "Installed");

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    updateGuiWhenPurchaseManInstalled(null);
                }
            });
        }

        @Override
        public void handleInstallError(final Throwable e) {
            Gdx.app.error("IAP", "Error when trying to install PurchaseManager", e);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    updateGuiWhenPurchaseManInstalled(e.getMessage());
                }
            });
        }

        @Override
        public void handleRestore(final Transaction[] transactions) {
            if (transactions != null && transactions.length > 0)
                for (Transaction t : transactions) {
                    handlePurchase(t, true);
                }
//            else if (restorePressed)
//                showErrorOnMainThread("Nothing to restore");
        }

        @Override
        public void handleRestoreError(Throwable e) {
//            if (restorePressed)
//                showErrorOnMainThread("Error restoring purchases: " + e.getMessage());
        }

        @Override
        public void handlePurchase(final Transaction transaction) {
            handlePurchase(transaction, false);
        }

        protected void handlePurchase(final Transaction transaction, final boolean fromRestore) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (transaction.isPurchased()) {
//                        if (transaction.getIdentifier().equals(MY_ENTITLEMENT))
//                            buyEntitlement.setBought(fromRestore);
                        if (transaction.getIdentifier().equals(MY_CONSUMABLE)) {
                            buyButton.setBought(fromRestore);
                            // Test (Should be changed later)
                            Inventory inv2 = new Inventory(inv.getBalance(), inv.getDonateCurrency() + 10, inv.isDonateBoxesPurchased(), inv.isTutorialPassed(),
                                    inv.getAllModels(), inv.getAllMaps(), inv.getSd());
                            pi.write(inv2);
                            game.setScreen(new PurchaseScreen(game));
                        } else if (transaction.getIdentifier().equals(MY_BOX)) {
                            Inventory inv2 = new Inventory(inv.getBalance(), inv.getDonateCurrency(), true, inv.isTutorialPassed(),
                                    inv.getAllModels(), inv.getAllMaps(), inv.getSd());
                            pi.write(inv2);
                            game.setScreen(new PurchaseScreen(game));
                        }
                    }
                }
            });
        }

        @Override
        public void handlePurchaseError(Throwable e) {
            showErrorOnMainThread("Error on buying:\n" + e.getMessage());
        }

        @Override
        public void handlePurchaseCanceled() {

        }

        private void showErrorOnMainThread(final String message) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    // show a dialog here...
                }
            });
        }
    }
}
