package net.bindernews.grackle.cardmods

import basemod.abstracts.AbstractCardModifier
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.variables.ExtraHitsVariable

class SuplexMod : AbstractCardModifier() {
    init {
        // Decrease priority
        priority = 1
    }

    override fun modifyBaseDamage(
        damage: Float,
        type: DamageInfo.DamageType,
        card: AbstractCard,
        target: AbstractMonster
    ): Float {
        return damage + (card.baseMagicNumber * ExtraHitsVariable.inst.value(card))
    }

    override fun makeCopy(): AbstractCardModifier {
        return SuplexMod()
    }
}