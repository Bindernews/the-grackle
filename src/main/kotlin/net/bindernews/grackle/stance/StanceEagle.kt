package net.bindernews.grackle.stance

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.MiscUtil.addToBot
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.power.PeckingOrderPower

class StanceEagle : AbstractStance(), StanceDelegate {
    var owner: AbstractCreature = AbstractDungeon.player

    init {
        ID = STANCE_ID
        name = STRINGS.NAME
        updateDescription()
    }

    override fun getDescription(): String = description

    override fun updateDescription() {
        description = STRINGS.DESCRIPTION[0].format(BUF_AMOUNT)
    }

    override fun onExitStance() {
        addToBot(iop().actionApplyPower(owner, owner, PeckingOrderPower.POWER_ID, BUF_AMOUNT))
    }

    companion object {
        @JvmStatic val STANCE_ID = GrackleMod.makeId("StanceEagle")
        @JvmStatic val STRINGS = CardCrawlGame.languagePack.getStanceString(STANCE_ID)!!
        var BUF_AMOUNT = 2
    }
}