package net.bindernews.grackle.downfall

import charbosses.bosses.AbstractCharBoss
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.orbs.AbstractOrb
import com.megacrit.cardcrawl.potions.AbstractPotion
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.helper.IPlayerLike

class EnemyPlayerLike(val p: AbstractCharBoss) : IPlayerLike {
    private val _potions = arrayListOf<AbstractPotion>()
    private val _dummyGroup = CardGroup(CardGroup.CardGroupType.UNSPECIFIED)

    @Suppress("UNCHECKED_CAST")
    override val relics: ArrayList<AbstractRelic> get() = p.relics as ArrayList<AbstractRelic>
    override val potions: ArrayList<AbstractPotion> get() = _potions
    override val stance: AbstractStance get() = p.stance
    override val masterDeck: CardGroup get() = _dummyGroup
    override val drawPile: CardGroup get() = _dummyGroup
    override val hand: CardGroup = p.hand
    override val discardPile: CardGroup get() = _dummyGroup
    override val exhaustPile: CardGroup get() = _dummyGroup
    override val limbo: CardGroup = p.limbo
    override val orbs: ArrayList<AbstractOrb> = p.orbs
    override fun into(): AbstractCreature = p
}