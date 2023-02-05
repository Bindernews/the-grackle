package net.bindernews.grackle.stance

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.stances.NeutralStance
import net.bindernews.bnsts.MiscUtil.addToBot
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.downfall.stances.EnemyStanceDelegate
import net.bindernews.grackle.helper.ModInterop
import net.bindernews.grackle.power.CoolingPhoenixPower

class StancePhoenix : AbstractStance(), EnemyStanceDelegate {
    var owner: AbstractCreature
    var canExitStance: Boolean

    init {
        ID = STANCE_ID
        name = STRINGS.NAME
        owner = AbstractDungeon.player
        canExitStance = true
        updateDescription()
    }

    override val description: String
        get() = description

    override fun atDamageReceive(damage: Float, damageType: DamageType): Float {
        // All the benefits
        return aloftInst.atDamageReceive(damage, damageType)
    }

    override fun atDamageGive(damage: Float, type: DamageType): Float {
        // None of the downsides
        return damage
    }

    override fun onEnterStance() {
        canExitStance = false
    }

    override fun onExitStance() {
        // If the turn has not ended then go back into our stance
        if (!canExitStance) {
            addToBot(ModInterop.iop().changeStance(owner, STANCE_ID))
        }
    }

    override fun atStartOfTurn() {
        canExitStance = true
        addToBot(ApplyPowerAction(owner, owner, CoolingPhoenixPower(owner, 1)))
        addToBot(ModInterop.iop().changeStance(owner, NeutralStance.STANCE_ID))
    }

    override fun onEndOfTurn() {
        addToBot(SkipEnemiesTurnAction())
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
        @JvmStatic fun isStance(s: AbstractStance?): Boolean {
            if (s == null) {
                return false
            }
            return s.ID == STANCE_ID
        }
    }
}