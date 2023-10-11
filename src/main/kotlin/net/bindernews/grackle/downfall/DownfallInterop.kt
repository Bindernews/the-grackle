package net.bindernews.grackle.downfall

import charbosses.actions.common.EnemyGainEnergyAction
import charbosses.actions.common.EnemyMakeTempCardInDiscardAction
import charbosses.actions.unique.EnemyChangeStanceAction
import charbosses.bosses.AbstractCharBoss
import charbosses.cards.AbstractBossCard
import charbosses.powers.general.EnemyDrawCardNextTurnPower
import charbosses.powers.general.EnemyVigorPower
import charbosses.relics.AbstractCharbossRelic
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.EnergyManager
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.helper.ModInterop
import java.util.HashMap
import java.util.stream.Stream

class DownfallInterop : ModInterop() {
    private val powerReplacements = HashMap<String, Class<out AbstractPower>>()

    init {
        initPowerReplacements()
    }

    override fun getStance(c: AbstractCreature): AbstractStance? {
        return if (c is AbstractCharBoss) {
            c.stance
        } else next!!.getStance(c)
    }

    override fun changeStance(c: AbstractCreature, stanceId: String): AbstractGameAction? {
        return if (c is AbstractCharBoss) {
            EnemyChangeStanceAction(stanceId)
        } else next!!.changeStance(c, stanceId)
    }

    override fun getRelics(c: AbstractCreature): Stream<AbstractRelic> {
        return if (c is AbstractCharBoss) {
            c.relics.stream().map { r: AbstractCharbossRelic -> r }
        } else next!!.getRelics(c)
    }

    override fun getPowerClass(c: AbstractCreature, powerId: String): Class<out AbstractPower>? {
        if (c is AbstractCharBoss) {
            val altClz = powerReplacements[powerId]
            if (altClz != null) {
                return altClz
            }
        }
        return next!!.getPowerClass(c, powerId)
    }

    override fun getCardOwner(card: AbstractCard?): AbstractCreature {
        return if (card is AbstractBossCard) {
            card.owner
        } else next!!.getCardOwner(card)!!
    }

    override fun actionMakeTempCardInDiscard(
        c: AbstractCreature?,
        card: AbstractCard?,
        amount: Int
    ): AbstractGameAction? {
        return if (c is AbstractCharBoss) {
            EnemyMakeTempCardInDiscardAction(card, amount)
        } else next!!.actionMakeTempCardInDiscard(c, card, amount)
    }

    override fun actionGainEnergy(c: AbstractCreature?, amount: Int): AbstractGameAction? {
        return if (c is AbstractCharBoss) {
            EnemyGainEnergyAction(c as AbstractCharBoss?, amount)
        } else next!!.actionGainEnergy(c, amount)
    }

    override fun getEnergy(c: AbstractCreature): EnergyManager? {
        return if (c is AbstractCharBoss) {
            c.energy
        } else next!!.getEnergy(c)
    }

    private fun initPowerReplacements() {
        val m = powerReplacements
        m["Draw Card"] = EnemyDrawCardNextTurnPower::class.java
        m["Vigor"] = EnemyVigorPower::class.java
    }
}