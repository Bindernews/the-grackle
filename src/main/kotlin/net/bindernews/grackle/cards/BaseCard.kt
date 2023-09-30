package net.bindernews.grackle.cards

import basemod.AutoAdd
import basemod.abstracts.CustomCard
import basemod.helpers.TooltipInfo
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.Grackle
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.api.GCardTags
import net.bindernews.grackle.helper.ICardInitializer
import net.bindernews.grackle.helper.ModInterop
import net.bindernews.grackle.helper.extraHits

@AutoAdd.Ignore
@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
abstract class BaseCard(private val opts: CardConfig, protected var cardInitializer: ICardInitializer?) : CustomCard(
    opts.ID, opts.strings.NAME, RegionName(""), 1, opts.strings.DESCRIPTION,
    opts.type, Grackle.Co.COLOR_BLACK, opts.rarity, opts.target
) {
    var owner: AbstractCreature? = AbstractDungeon.player

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

    /**
     * A more generic version of [BaseCard.use] which will be useful
     * when integrating with the Downfall mod.
     * @param p Player or CharBoss
     * @param m Monster or Player target
     */
    open fun apply(p: AbstractCreature, m: AbstractCreature?) {}

    override fun getCustomTooltips(): MutableList<TooltipInfo> {
        val tips = super.getCustomTooltips() ?: arrayListOf()
        if (this is IDamageTip || hasTag(GCardTags.TAG_DAMAGE_TIP)) {
            val dmg = damage * extraHits
            tips.add(TooltipInfo("Total Damage", "$dmg"))
        }
        return tips
    }

    inline val iop: ModInterop get() = ModInterop.iop()

    companion object {
        val cards by lazy { TextureAtlas(GrackleMod.CO.RES_IMAGES + "/cards/cards.atlas") }

        /**
         * Should be used in [BaseCard.apply] to indicate that the card is only applicable to players,
         * not downfall enemy bosses.
         */
        fun throwPlayerOnly() {
            throw UnsupportedOperationException("FightFire only works with AbstractPlayer")
        }
    }
}