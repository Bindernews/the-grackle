package net.bindernews.grackle.cards

import basemod.AutoAdd
import basemod.abstracts.CustomCard
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.Grackle
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.ICardInitializer
import net.bindernews.grackle.helper.ModInterop
import net.bindernews.grackle.variables.SpeedBoostCard
import net.bindernews.grackle.variables.VariableInst
import java.util.function.Function

@AutoAdd.Ignore
@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
abstract class BaseCard(
    private val opts: CardConfig,
    protected var cardInitializer: ICardInitializer?
): CustomCard(
    opts.ID, opts.strings.NAME, RegionName(""), 1, opts.strings.DESCRIPTION,
    opts.type, Grackle.Co.COLOR_BLACK, opts.rarity, opts.target
), SpeedBoostCard {
    var owner: AbstractCreature? = AbstractDungeon.player
    override val speedBoost = VariableInst(-1)

    constructor(opts: CardConfig) : this(opts, null)

    init {
        cardInitializer?.init(this)
        opts.loadImages(cards)
        portrait = opts.portrait
        jokePortrait = opts.betaPortrait
    }

    override fun applyPowers() {
        magicNumber = baseMagicNumber
        super.applyPowers()
    }

    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        apply(p, m)
    }

    override fun upgrade() {
        cardInitializer?.upgrade(this)
    }

    override fun triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy()
        if (isBonusActive()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
        }
    }

    /**
     * Returns true when the card's bonus effect is ready to trigger.
     */
    open fun isBonusActive(): Boolean = false

    /**
     * A more generic version of [BaseCard.use] which will be useful
     * when integrating with the Downfall mod.
     * @param p Player or CharBoss
     * @param m Monster or Player target
     */
    open fun apply(p: AbstractCreature, m: AbstractCreature?) {}

    /**
     * If not null, this builder will be used to set `rawDescription` during [initializeDescription].
     */
    open val descriptionSource: Function<Boolean, String>? get() = null

    override fun initializeDescription() {
        val b = descriptionSource
        if (b != null) {
            rawDescription = b.apply(upgraded)
        }
        super.initializeDescription()
    }

    inline val iop: ModInterop get() = ModInterop.iop()

    companion object {
        val cards by lazy { TextureAtlas(GrackleMod.CO.RES_IMAGES + "/cards/cards.atlas") }

        /**
         * Should be used in [BaseCard.apply] to indicate that the card is only applicable to players,
         * not downfall enemy bosses.
         */
        fun throwPlayerOnly() {
            throw UnsupportedOperationException("this card only works with AbstractPlayer")
        }
    }
}