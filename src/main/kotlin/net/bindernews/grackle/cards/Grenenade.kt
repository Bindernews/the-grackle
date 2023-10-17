package net.bindernews.grackle.cards

import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.baseExtraHits
import net.bindernews.grackle.helper.extraHits
import net.bindernews.grackle.variables.ExtraHitsVariable

class Grenenade : BaseCard(C) {
    init {
        cost = 2
        costForTurn = 2
        baseDamage = 4
        baseExtraHits = 1
        isMultiDamage = true
        CardModifierManager.addModifier(this, ExtraHitsMod())
    }

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.FIRE
        val hits = extraHits
        for (i in 0 until hits) {
            addToBot(iop.damageAllEnemies(p, multiDamage, damageTypeForTurn, fx))
        }
    }

    override fun upgradeName() {
        super.upgradeName()
        var neCount = timesUpgraded
        if (neCount > 8) {
            neCount = 1
        }
        val sb = StringBuilder()
        val nameParts = C.strings.EXTENDED_DESCRIPTION
        sb.append(nameParts[0])
        for (i in 0 until neCount) {
            sb.append(nameParts[1])
        }
        sb.append(nameParts[2])
        sb.append('+')
        sb.append(timesUpgraded)
        name = sb.toString()
    }

    override fun canUpgrade(): Boolean {
        return true
    }

    override fun upgrade() {
        upgradeName()
        // Odd upgrades (1st, 3rd, etc.) will upgrade hit count
        // Even upgrades will upgrade damage amount
        if (timesUpgraded % 2 == 0) {
            upgradeDamage(3)
        } else {
            ExtraHitsVariable.inst.upgrade(this, 1)
        }
    }

    companion object {
        @JvmField val C = CardConfig("Grenenade", CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY)
    }
}