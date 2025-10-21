package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables
import java.util.function.Consumer

class SpeedForm : BaseCard(C, VARS) {
    public override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        super.apply(p, m)
    }

    companion object {
        val C: CardConfig = CardConfig("SpeedForm", CardType.POWER, CardRarity.RARE, CardTarget.SELF)

        private val VARS = CardVariables().apply {
            cost(3, -1)
            onInit { c -> c.isEthereal = true }
            onUpgrade { c -> c.isEthereal = false }
            addModifier(AutoDescription())
        }
    }
}