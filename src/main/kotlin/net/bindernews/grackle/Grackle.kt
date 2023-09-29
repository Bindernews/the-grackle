package net.bindernews.grackle

import basemod.BaseMod
import basemod.abstracts.CustomPlayer
import basemod.animations.SpineAnimation
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.*
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.screens.CharSelectInfo
import net.bindernews.grackle.cards.*

class Grackle(name: String?) : CustomPlayer(
    name, Co.GRACKLE, Co.ORB_TEXTURES, Co.RES_IMAGES_CHAR + "/orb/vfx.png",
    SpineAnimation(Co.ANIM_ATLAS, Co.ANIM_SKELETON, 1.0f)
) {

    // Enums, constants, etc. that can't load the actual Grackle class
    object Co {
        @JvmStatic
        fun registerColor() {
            val c1 = COLOR_MAIN
            BaseMod.addColor(
                COLOR_BLACK, c1, c1, c1, c1, c1, c1, c1,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY,
                ENERGY_ORB,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_PORTRAIT, CARD_ENERGY_ORB
            )
        }

        val COLOR_MAIN = Color(0x063972ff)
        val COLOR_FX: Color = Color.CYAN

        @SpireEnum
        lateinit var GRACKLE: PlayerClass

        @SpireEnum(name = "GRACKLE_BLACK")
        lateinit var COLOR_BLACK: CardColor

        @SpireEnum(name = "GRACKLE_BLACK")
        lateinit var LIBRARY_COLOR: LibraryType

        const val RES_IMAGES_CHAR = GrackleMod.CO.RES_IMAGES + "/char/grackle"
        const val BUTTON = "$RES_IMAGES_CHAR/HermitButton.png"
        const val PORTRAIT = "$RES_IMAGES_CHAR/HermitPortrait.png"
        const val IMG_CAMPFIRE_LIT = "$RES_IMAGES_CHAR/campfire_lit.png"
        const val IMG_CAMPFIRE_USED = "$RES_IMAGES_CHAR/campfire_used.png"
        const val IMG_CORPSE = "$RES_IMAGES_CHAR/corpse.png"
        const val ANIM_ATLAS = "$RES_IMAGES_CHAR/Hermit.atlas"
        const val ANIM_SKELETON = "$RES_IMAGES_CHAR/Hermit.json"
        val ORB_TEXTURES = arrayOf(
            "$RES_IMAGES_CHAR/orb/layer1.png",
            "$RES_IMAGES_CHAR/orb/layer2.png",
            "$RES_IMAGES_CHAR/orb/layer3.png",
            "$RES_IMAGES_CHAR/orb/layer4.png",
            "$RES_IMAGES_CHAR/orb/layer5.png",
            "$RES_IMAGES_CHAR/orb/layer6.png",
            "$RES_IMAGES_CHAR/orb/layer1d.png",
            "$RES_IMAGES_CHAR/orb/layer2d.png",
            "$RES_IMAGES_CHAR/orb/layer3d.png",
            "$RES_IMAGES_CHAR/orb/layer4d.png",
            "$RES_IMAGES_CHAR/orb/layer5d.png"
        )
        const val ATTACK_DEFAULT_GRAY = GrackleMod.CO.RES_IMAGES + "/512/bg_attack_default_gray.png"
        const val SKILL_DEFAULT_GRAY = GrackleMod.CO.RES_IMAGES + "/512/bg_skill_default_gray.png"
        const val POWER_DEFAULT_GRAY = GrackleMod.CO.RES_IMAGES + "/512/bg_power_default_gray.png"
        const val ENERGY_ORB = GrackleMod.CO.RES_IMAGES + "/512/card_default_gray_orb.png"
        const val CARD_ENERGY_ORB = GrackleMod.CO.RES_IMAGES + "/512/card_small_orb.png"
        const val ATTACK_DEFAULT_GRAY_PORTRAIT = GrackleMod.CO.RES_IMAGES + "/1024/bg_attack_default_gray.png"
        const val SKILL_DEFAULT_GRAY_PORTRAIT = GrackleMod.CO.RES_IMAGES + "/1024/bg_skill_default_gray.png"
        const val POWER_DEFAULT_GRAY_PORTRAIT = GrackleMod.CO.RES_IMAGES + "/1024/bg_power_default_gray.png"
        const val ENERGY_ORB_PORTRAIT = GrackleMod.CO.RES_IMAGES + "/1024/orb_portrait.png"
    }

    init {
        initializeClass(
            null, Co.IMG_CAMPFIRE_USED, Co.IMG_CAMPFIRE_LIT, Co.IMG_CORPSE, loadout,
            20f, -10f, 220f, 290f, EnergyManager(ENERGY_PER_TURN)
        )
//        AnimationState.TrackEntry e = state.setAnimation(0, "Idle", true);

        // Text bubble location
        dialogX = drawX + 0f * Settings.scale
        dialogY = drawY + hb_h * Settings.scale
    }

    override fun getStartingDeck(): ArrayList<String> {
        return arrayListOf(
            Strike_GK.C.ID, Strike_GK.C.ID, Strike_GK.C.ID, Strike_GK.C.ID,
            Defend_GK.C.ID, Defend_GK.C.ID, Defend_GK.C.ID, Defend_GK.C.ID,
            Takeoff.C.ID, CrashLanding.C.ID
        )
    }

    override fun getStartingRelics(): ArrayList<String> = arrayListOf("Black Star")

    override fun getLoadout(): CharSelectInfo {
        return CharSelectInfo(
            STRINGS.NAMES[0], STRINGS.TEXT[0], STARTING_HP, STARTING_HP, ORB_SLOTS,
            STARTING_GOLD, CARD_DRAW, this, startingRelics, startingDeck, false
        )
    }

    override fun getTitle(playerClass: PlayerClass): String = STRINGS.NAMES[1]
    override fun getCardColor(): CardColor = Co.COLOR_BLACK
    override fun getCardRenderColor(): Color = Co.COLOR_MAIN

    /**
     * Match and keep event card
     */
    override fun getStartCardForEvent(): AbstractCard {
        return Flock()
    }

    override fun getCardTrailColor(): Color = Co.COLOR_FX
    override fun getAscensionMaxHPLoss(): Int = 4
    override fun getEnergyNumFont(): BitmapFont = FontHelper.energyNumFontRed

    override fun doCharSelectScreenSelectEffect() {
        // TODO
    }

    override fun getCustomModeCharacterButtonSoundKey(): String? {
        // TODO
        return null
    }

    override fun getLocalizedCharacterName(): String {
        return STRINGS.NAMES[0]
    }

    override fun newInstance(): AbstractPlayer {
        return Grackle(name)
    }

    override fun getSpireHeartText(): String {
        return STRINGS.TEXT[1]
    }

    override fun getSlashAttackColor(): Color {
        return Co.COLOR_FX
    }

    override fun getSpireHeartSlashEffect(): Array<AttackEffect> {
        return arrayOf(AttackEffect.BLUNT_LIGHT, AttackEffect.BLUNT_HEAVY, AttackEffect.BLUNT_LIGHT,
                AttackEffect.BLUNT_HEAVY, AttackEffect.BLUNT_HEAVY, AttackEffect.BLUNT_LIGHT)
    }

    override fun getVampireText(): String = STRINGS.TEXT[2]

    companion object {
        val CHAR_ID = GrackleMod.makeId("grackle")
        val STRINGS = CardCrawlGame.languagePack.getCharacterString(CHAR_ID)!!
        const val STARTING_HP = 70
        const val ORB_SLOTS = 0
        const val STARTING_GOLD = 99
        const val CARD_DRAW = 5
        const val ENERGY_PER_TURN = 3
        @JvmStatic
        fun register() {
            BaseMod.addCharacter(Grackle(CardCrawlGame.playerName), Co.BUTTON, Co.PORTRAIT, Co.GRACKLE)
        }

        @JvmStatic
        val isPlaying: Boolean
            get() = AbstractDungeon.player is Grackle
    }
}