package com.mygdx.forkliftaone;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.mygdx.forkliftaone.ForkLiftGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		ForkLiftGame game = new ForkLiftGame();
		initialize(game, config);
		game.purchaseManager = new PurchaseManagerGoogleBilling(this);
	}
}
