package net.bindernews.grackle.downfall.relics

import charbosses.actions.common.EnemyMakeTempCardInHandAction
import charbosses.relics.AbstractCharbossRelic
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.relics.AbstractRelic
import net.bindernews.grackle.downfall.DfUtil
import net.bindernews.grackle.relics.SimmeringHeat

class CBR_SimmeringHeat : AbstractCharbossRelic(SimmeringHeat()) {
    init {
        DfUtil.setImagesFromBase(this)
    }

    override fun atTurnStartPostDraw() {
        addToBot(EnemyMakeTempCardInHandAction(Burn()))
    }

    override fun onEquip() {
        owner.energy.energyMaster++
    }

    override fun onUnequip() {
        owner.energy.energyMaster--
    }

    override fun makeCopy(): AbstractRelic {
        return CBR_SimmeringHeat()
    }
}