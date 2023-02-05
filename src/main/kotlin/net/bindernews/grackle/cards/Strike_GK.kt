package net.bindernews.grackle.cards

import basemod.AutoAdd.Seen
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables

@Seen
class Strike_GK : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.SLASH_HORIZONTAL
        addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), fx))
    }

    companion object {
        @JvmField
        val C = CardConfig("Strike_GK", CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.damage(6, 9)
            c.tags(CardTags.STARTER_STRIKE, CardTags.STRIKE)
        }
    }
}