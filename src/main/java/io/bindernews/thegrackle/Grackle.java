package io.bindernews.thegrackle;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import io.bindernews.thegrackle.cards.CrashLanding;
import io.bindernews.thegrackle.cards.Defend_GK;
import io.bindernews.thegrackle.cards.Strike_GK;
import io.bindernews.thegrackle.cards.Takeoff;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;

import static io.bindernews.thegrackle.MiscUtil.arrayListOf;

public class Grackle extends CustomPlayer {

    public static final String BUTTON = Const.RES_IMAGES + "/character/grackleButton.png";
    public static final String PORTRAIT = Const.RES_IMAGES + "/character/gracklePortrait.png";

    public Grackle(String name) {
        super(name, En.GRACKLE, null, "grackle/images/vfx.png",
                new SpineAnimation("", "", 1.0f));

    }

    @Override
    public ArrayList<String> getStartingDeck() {
        return arrayListOf(
                Strike_GK.C.ID, Strike_GK.C.ID, Strike_GK.C.ID, Strike_GK.C.ID,
                Defend_GK.C.ID, Defend_GK.C.ID, Defend_GK.C.ID, Defend_GK.C.ID,
                Takeoff.C.ID, CrashLanding.C.ID
        );
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        return null;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return null;
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return null;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return En.COLOR_BLACK;
    }

    @Override
    public Color getCardRenderColor() {
        return null;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return null;
    }

    @Override
    public Color getCardTrailColor() {
        return null;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 0;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return null;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {

    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return null;
    }

    @Override
    public String getLocalizedCharacterName() {
        return null;
    }

    @Override
    public AbstractPlayer newInstance() {
        return null;
    }

    @Override
    public String getSpireHeartText() {
        return null;
    }

    @Override
    public Color getSlashAttackColor() {
        return null;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[0];
    }

    @Override
    public String getVampireText() {
        return null;
    }

    public static class En {
        @SpireEnum
        public static AbstractPlayer.PlayerClass GRACKLE;
        @SpireEnum(name = "GRACKLE_BLACK")
        public static AbstractCard.CardColor COLOR_BLACK;
        @SpireEnum(name = "GRACKLE_BLACK")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }
}
