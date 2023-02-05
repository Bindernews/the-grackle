package net.bindernews.grackle.cardmods

import basemod.abstracts.AbstractCardModifier
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.ModInterop
import net.bindernews.grackle.stance.StanceAloft

class DoubleAloftDamageMod : AbstractCardModifier() {
    override fun modifyDamage(damage: Float, type: DamageType, card: AbstractCard, target: AbstractMonster): Float {
        val owner = ModInterop.iop().getCardOwner(card)
        return if (StanceAloft.isAloft(owner)) {
            damage * 2f
        } else {
            damage
        }
    }

    override fun modifyDescription(rawDescription: String, card: AbstractCard): String {
        val ln = GrackleMod.miscUI["double_aloft_damage"]
        return String.format("%s NL %s", rawDescription, ln)
    }

    override fun makeCopy(): AbstractCardModifier {
        return DoubleAloftDamageMod()
    }
}