package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.VulnerablePower
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import java.util.stream.Stream

class ThisWillHurt : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.NONE
        var targets = iop().getEnemies(p)
        if (!upgraded) {
            targets = Stream.concat(iop().getFriends(p), targets)
        }
        targets.forEach { cr ->
            val power = iop().createPower(VulnerablePower.POWER_ID, cr, magicNumber)
            addToBot(ApplyPowerAction(cr, p, power, magicNumber, true, fx))
        }
        addToBot(GainBlockAction(p, p, block))
    }

    override fun initializeDescription() {
        rawDescription = if (upgraded) C.strings.UPGRADE_DESCRIPTION else C.strings.DESCRIPTION
        super.initializeDescription()
    }

    companion object {
        @JvmField val C = CardConfig("ThisWillHurt", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY)
        val VARS = CardVariables().apply {
            cost(1, 0)
            magic(1, -1)
            block(4, -1)
            onUpgrade { it.initializeDescription() }
        }
    }
}