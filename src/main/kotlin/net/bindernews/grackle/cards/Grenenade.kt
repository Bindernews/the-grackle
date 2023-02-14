package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.hits
import net.bindernews.grackle.variables.ExtraHitsVariable

class Grenenade : BaseCard(C) {
    init {
        // Custom upgrade mechanics, so we only need VARS for init
        VARS.init(this)
    }

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.FIRE
        val hits = ExtraHitsVariable.inst.value(this)
        for (i in 0 until hits) {
            addToBot(iop().damageAllEnemies(p, multiDamage, damageTypeForTurn, fx))
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
            upgradeDamage(2)
        } else {
            ExtraHitsVariable.inst.upgrade(this, 1)
        }
    }

    companion object {
        @JvmField val C = CardConfig("Grenenade", CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY)
        val VARS = CardVariables().apply {
            cost(2)
            damage(4)
            hits(1)
            multiDamage(true, true)
            addModifier(ExtraHitsMod())
        }
    }
}