package io.bindernews.thegrackle;

import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import io.bindernews.thegrackle.cards.*;

import java.util.ArrayList;

import static io.bindernews.thegrackle.MiscUtil.arrayListOf;

public class Grackle extends CustomPlayer {

    public static final String CHAR_ID = GrackleMod.makeId("grackle");
    public static CharacterStrings STRINGS = CardCrawlGame.languagePack.getCharacterString(CHAR_ID);

    static final int STARTING_HP = 70;
    static final int ORB_SLOTS = 0;
    static final int STARTING_GOLD = 99;
    static final int CARD_DRAW = 5;
    static final int ENERGY_PER_TURN = 3;

    static final String RES_IMAGES_CHAR = Const.RES_IMAGES + "/char/grackle";
    static final String BUTTON = RES_IMAGES_CHAR + "/HermitButton.png";
    static final String PORTRAIT = RES_IMAGES_CHAR + "/HermitPortrait.png";
    static final String IMG_CAMPFIRE_LIT = RES_IMAGES_CHAR + "/campfire_lit.png";
    static final String IMG_CAMPFIRE_USED = RES_IMAGES_CHAR + "/campfire_used.png";
    static final String IMG_CORPSE = RES_IMAGES_CHAR + "/corpse.png";
    static final String ANIM_ATLAS = RES_IMAGES_CHAR + "/Hermit.atlas";
    static final String ANIM_SKELETON = RES_IMAGES_CHAR + "/Hermit.json";

    static final String[] ORB_TEXTURES = new String[] {
            RES_IMAGES_CHAR + "/orb/layer1.png",
            RES_IMAGES_CHAR + "/orb/layer2.png",
            RES_IMAGES_CHAR + "/orb/layer3.png",
            RES_IMAGES_CHAR + "/orb/layer4.png",
            RES_IMAGES_CHAR + "/orb/layer5.png",
            RES_IMAGES_CHAR + "/orb/layer6.png",
            RES_IMAGES_CHAR + "/orb/layer1d.png",
            RES_IMAGES_CHAR + "/orb/layer2d.png",
            RES_IMAGES_CHAR + "/orb/layer3d.png",
            RES_IMAGES_CHAR + "/orb/layer4d.png",
            RES_IMAGES_CHAR + "/orb/layer5d.png"
    };

    public Grackle(String name) {
        super(name, En.GRACKLE, ORB_TEXTURES, RES_IMAGES_CHAR + "/orb/vfx.png",
                new SpineAnimation(ANIM_ATLAS, ANIM_SKELETON, 1.0f));
        initializeClass(
                null, IMG_CAMPFIRE_USED, IMG_CAMPFIRE_LIT, IMG_CORPSE, getLoadout(),
                20.f, -10.f, 220.f, 290.f, new EnergyManager(ENERGY_PER_TURN)
        );

//        AnimationState.TrackEntry e = state.setAnimation(0, "Idle", true);

        // Text bubble location
        dialogX = (drawX + 0.f * Settings.scale);
        dialogY = (drawY + hb_h * Settings.scale);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        return arrayListOf(
                Strike_GK.CFG.ID, Strike_GK.CFG.ID, Strike_GK.CFG.ID, Strike_GK.CFG.ID,
                Defend_GK.C.ID, Defend_GK.C.ID, Defend_GK.C.ID, Defend_GK.C.ID,
                Takeoff.CFG.ID, CrashLanding.CFG.ID, FiredUpCard.C.ID, SelfBurn.C.ID
        );
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        // TODO
        return arrayListOf("Black Star");
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(STRINGS.NAMES[0], STRINGS.TEXT[0], STARTING_HP, STARTING_HP, ORB_SLOTS,
                STARTING_GOLD, CARD_DRAW, this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return STRINGS.NAMES[1];
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return En.COLOR_BLACK;
    }

    @Override
    public Color getCardRenderColor() {
        return En.COLOR_MAIN;
    }

    /**
     * Match and keep event cards?
     */
    @Override
    public AbstractCard getStartCardForEvent() {
        return null; // TODO
    }

    @Override
    public Color getCardTrailColor() {
        return En.COLOR_FX;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        // TODO
    }

    // TODO
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return null;
    }

    @Override
    public String getLocalizedCharacterName() {
        return STRINGS.NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Grackle(name);
    }

    @Override
    public String getSpireHeartText() {
        return STRINGS.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return En.COLOR_FX;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        // TODO
        return new AbstractGameAction.AttackEffect[0];
    }

    @Override
    public String getVampireText() {
        return STRINGS.TEXT[2];
    }


    public static void register() {
        BaseMod.addCharacter(new Grackle(CardCrawlGame.playerName), BUTTON, PORTRAIT, En.GRACKLE);
    }


    // Enums and a few colors that can't load the actual Grackle class
    public static class En {

        public static final Color COLOR_MAIN = new Color(0x063972ff);
        public static final Color COLOR_FX = Color.CYAN;

        @SpireEnum
        public static AbstractPlayer.PlayerClass GRACKLE;
        @SpireEnum(name = "GRACKLE_BLACK")
        public static AbstractCard.CardColor COLOR_BLACK;
        @SpireEnum(name = "GRACKLE_BLACK")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }


    public static boolean isPlaying() {
        return AbstractDungeon.player instanceof Grackle;
    }
}
