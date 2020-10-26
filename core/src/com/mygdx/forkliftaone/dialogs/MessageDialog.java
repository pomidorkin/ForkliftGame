package com.mygdx.forkliftaone.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.forkliftaone.ForkLiftGame;

public class MessageDialog extends Dialog {

    ForkLiftGame game;
    private String textMessage;

    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
    float ratio = width / height;

    public MessageDialog(ForkLiftGame game, String title, String infoMessage, Skin skin) {
        super(title, skin);
        this.game = game;
        textMessage = infoMessage;

        {
            add().height(((1200 / ratio / 2f) / 3f));
            row();
            text(textMessage);
            row();
            add().height(((1200 / ratio / 2f) / 4f));
            button("OK", 1);

//            debug();
        }
    }



    @Override
    protected void result(Object object) {
        super.result(object);
        if (object.equals(1)) {
            remove();
        }
    }
}
