package com.mygdx.forkliftaone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.forkliftaone.screens.loading.TestLoading;

public class ForkLiftGame extends Game {
    private AssetManager assetManager;
    private SpriteBatch batch;
    public PurchaseManager purchaseManager;
//	private Inventory inv;


    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Logger.DEBUG);
//		this.inv = loadInventory();

        setScreen(new TestLoading(this));

    }

    @Override
    public void dispose() {
        assetManager.dispose();
        batch.dispose();

        if (purchaseManager != null)
            purchaseManager.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

//	private Inventory loadInventory(){
//		ProcessInventory pi = new ProcessInventory();
//		Inventory inv = pi.read();
//		return inv;
//	}
}
