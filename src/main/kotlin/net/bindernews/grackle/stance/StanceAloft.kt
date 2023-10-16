package net.bindernews.grackle.stance

import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.MiscUtil.addToBot
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.power.StanceAloftPower
import java.util.*

class StanceAloft : AbstractStance(), StanceDelegate {
    var owner: AbstractCreature = AbstractDungeon.player

    /**
     * Used for temporary damage calculations.
     */
    var enabled = true

    init {
        ID = STANCE_ID
        name = STRINGS.NAME
        updateDescription()
    }

    override fun getDescription(): String = description

    override fun onEnterStance() {
        addToBot(iop().actionApplyPower(owner, owner, StanceAloftPower.POWER_ID, -1))
    }

    override fun onExitStance() {
        addToBot(RemoveSpecificPowerAction(owner, owner, StanceAloftPower.POWER_ID))
        addToBot(GainEnergyAction(energyGain))
    }

    override fun atDamageGive(damage: Float, type: DamageType): Float {
        return if (type == DamageType.NORMAL && enabled) {
            damage * 0.75f
        } else {
            damage
        }
    }

    override fun updateDescription() {
        description = STRINGS.DESCRIPTION[0]
    }

    companion object {
        @JvmField val STANCE_ID = GrackleMod.makeId(StanceAloft::class.java)
        @JvmStatic val STRINGS = CardCrawlGame.languagePack.getStanceString(STANCE_ID)

        @JvmField var energyGain = 1

        /**
         * If the player is aloft then returns true, otherwise sets `cantUseMessage` and returns false.
         * @param p player
         * @param card the card to update
         */
        @Suppress("UNUSED_PARAMETER")
        fun checkPlay(card: AbstractCard, p: AbstractPlayer, ignoredM: AbstractMonster?): Boolean {
            val b = isAloft(p)
            if (!b) {
                card.cantUseMessage = STRINGS.DESCRIPTION[1]
            }
            return b
        }

        fun isAloft(p: AbstractCreature?): Boolean {
            val stanceId = p?.let { iop().getStance(it) }?.ID ?: ""
            return stanceId == STANCE_ID
        }

        fun getInstanceOn(c: AbstractCreature): Optional<StanceAloft> {
            return Optional.ofNullable(iop().getStance(c)).map { st -> st as? StanceAloft }
        }
    }
}