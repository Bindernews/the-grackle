package io.bindernews.thegrackle.stance

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.stances.NeutralStance
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.MiscUtil
import io.bindernews.thegrackle.helper.ModInterop
import io.bindernews.thegrackle.power.CoolingPhoenixPower

class StancePhoenix : AbstractStance() {
    var owner: AbstractCreature
    var canExitStance: Boolean

    init {
        ID = STANCE_ID
        name = STRINGS.NAME
        owner = AbstractDungeon.player
        canExitStance = true
        updateDescription()
    }

    override fun atDamageReceive(damage: Float, damageType: DamageType): Float {
        // All the benefits
        return aloftInst.atDamageReceive(damage, damageType)
    }

    override fun atDamageGive(damage: Float, damageType: DamageType): Float {
        // None of the downsides
        return damage
    }

    override fun onEnterStance() {
        canExitStance = false
    }

    override fun onExitStance() {
        // If the turn has not ended then go back into our stance
        if (!canExitStance) {
            MiscUtil.addToBot(ModInterop.iop().changeStance(owner, STANCE_ID))
        }
    }

    override fun atStartOfTurn() {
        canExitStance = true
        MiscUtil.addToBot(ApplyPowerAction(owner, owner, CoolingPhoenixPower(owner, 1)))
        MiscUtil.addToBot(ModInterop.iop().changeStance(owner, NeutralStance.STANCE_ID))
    }

    override fun onEndOfTurn() {
        MiscUtil.addToBot(SkipEnemiesTurnAction())
    }

    override fun updateDescription() {
        description = STRINGS.DESCRIPTION[0]
    }

    companion object {
        @JvmField val STANCE_ID = GrackleMod.makeId(StancePhoenix::class.java)
        @JvmStatic val STRINGS = CardCrawlGame.languagePack.getStanceString(STANCE_ID)

        /**
         * So we can calculate damage without code duplication.
         */
        private val aloftInst = StanceAloft()
        @JvmStatic fun isStance(s: AbstractStance): Boolean {
            return s.ID == STANCE_ID
        }
    }
}