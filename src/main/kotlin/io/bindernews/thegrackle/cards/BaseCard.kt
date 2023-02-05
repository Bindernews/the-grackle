package io.bindernews.thegrackle.cards

import basemod.AutoAdd
import basemod.abstracts.CustomCard
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import io.bindernews.thegrackle.helper.ICardInitializer
import io.bindernews.thegrackle.Grackle
import io.bindernews.thegrackle.GrackleMod

@AutoAdd.Ignore
@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
abstract class BaseCard(opts: CardConfig, protected var cardInitializer: ICardInitializer?) : CustomCard(
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