package io.bindernews.thegrackle.ui;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.options.ToggleButton;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.api.IPopup;
import lombok.Getter;
import lombok.val;

public class MainMenuMetricsRequest implements IPopup {

    @Getter(lazy = true)
    private static final MainMenuMetricsRequest inst = new MainMenuMetricsRequest();

    private final UIStrings strings =
            CardCrawlGame.languagePack.getUIString(GrackleMod.makeId(MainMenuMetricsRequest.class));

    private final MyConfirmPopup confirmPopup =
            new MyConfirmPopup(strings.TEXT[0], strings.TEXT[1]);

    /** True if we've already shown the popup. */
    public boolean prompted = false;

    public MainMenuMetricsRequest() {
        confirmPopup.getOnAction().on(a -> {
            if (a == MyConfirmPopup.Action.YES) {
                setUploadData(true);
            }
        });
    }

    @Override
    public void render(SpriteBatch sb) {
        confirmPopup.render(sb);
    }

    @Override
    public void update() {
        if (!prompted && !Settings.UPLOAD_DATA) {
            prompted = true;
            confirmPopup.show();
        }
        confirmPopup.update();
    }

    @Override
    public boolean isEnabled() {
        val screen = CardCrawlGame.mainMenuScreen;
        if (screen == null) {
            return false;
        }
        return screen.screen == MainMenuScreen.CurScreen.MAIN_MENU;
    }

    public static void setUploadData(boolean enabled) {
        val panel = CardCrawlGame.mainMenuScreen.optionPanel;
        val btn = (ToggleButton) ReflectionHacks.getPrivate(panel, panel.getClass(), "uploadToggle");
        if (btn.enabled != enabled) {
            btn.toggle();
        }
    }

}
