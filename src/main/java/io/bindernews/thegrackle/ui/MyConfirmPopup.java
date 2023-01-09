package io.bindernews.thegrackle.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.screens.options.ConfirmPopup;
import io.bindernews.bnsts.eventbus.EventEmit;
import io.bindernews.bnsts.eventbus.IEventEmit;
import lombok.Getter;

public class MyConfirmPopup extends ConfirmPopup {
    @Getter
    private final IEventEmit<Action> onAction = new EventEmit<>();

    public MyConfirmPopup(String title, String desc) {
        super(title, desc, CO.CUSTOM);
    }

    @Override
    public void show() {
        if (!shown) {
            shown = true;
            onAction.emit(Action.SHOW);
        }
    }

    @Override
    public void hide() {
        if (shown) {
            shown = false;
            onAction.emit(Action.HIDE);
        }
    }

    @Override
    protected void noButtonEffect() {
        hide();
        onAction.emit(Action.NO);
    }

    @Override
    protected void yesButtonEffect() {
        hide();
        onAction.emit(Action.YES);
    }



    public enum Action {
        YES,
        NO,
        SHOW,
        HIDE,
    }


    public static class CO {
        @SpireEnum(name = "GRACKLE_CUSTOM")
        public static ConfirmType CUSTOM;
    }
}
