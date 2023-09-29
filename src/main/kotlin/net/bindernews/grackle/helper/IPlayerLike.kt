package net.bindernews.grackle.helper

import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.orbs.AbstractOrb
import com.megacrit.cardcrawl.potions.AbstractPotion
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.stances.AbstractStance

interface IPlayerLike {
    val relics: ArrayList<AbstractRelic>
    val potions: ArrayList<AbstractPotion>
    val stance: AbstractStance
    val masterDeck: CardGroup
    val drawPile: CardGroup
    val hand: CardGroup
    val discardPile: CardGroup
    val exhaustPile: CardGroup
    val limbo: CardGroup
    val orbs: ArrayList<AbstractOrb>

    /**
     * Returns the wrapped player-like creature.
     */
    fun into(): AbstractCreature

    fun hasRelic(relicId: String): Boolean {
        return relics.any { it.relicId == relicId }
    }

    class ForPlayer(var p: AbstractPlayer) : IPlayerLike {
        override val relics: ArrayList<AbstractRelic> get() = p.relics
        override val potions: ArrayList<AbstractPotion> get() = p.potions
        override val stance: AbstractStance get() = p.stance
        override val masterDeck: CardGroup get() = p.masterDeck
        override val drawPile: CardGroup get() = p.drawPile
        override val hand: CardGroup get() = p.hand
        override val discardPile: CardGroup get() = p.discardPile
        override val exhaustPile: CardGroup get() = p.exhaustPile
        override val limbo: CardGroup get() = p.limbo
        override val orbs: ArrayList<AbstractOrb> get() = p.orbs
        override fun into(): AbstractCreature = p
    }
}