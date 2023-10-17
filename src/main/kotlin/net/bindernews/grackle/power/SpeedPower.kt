package net.bindernews.grackle.power

import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import net.bindernews.grackle.helper.MiscUtil
import net.bindernews.grackle.helper.makeId
import net.bindernews.grackle.variables.SpeedBoostVar

class SpeedPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    // How much we
    private var dexDivisor: Int = DEFAULT_DEX_DIVISOR

    init {
        setOwnerAmount(owner, amount)
        type = PowerType.BUFF
        updateDescription()
        loadRegion("energized_blue");
    }

    override fun updateDescription() {
        description = formatDesc(0, amount, amount / dexDivisor)
    }

    override fun reducePower(reduceAmount: Int) {
        if (owner.hasPower(SpeedPreservePower.POWER_ID)) {
            return
        }
        super.reducePower(reduceAmount)
    }

    override fun modifyBlock(blockAmount: Float): Float {
        return blockAmount + (amount / dexDivisor).toFloat()
    }

    override fun onInitialApplication() {
        // Cache the dex divisor
        dexDivisor = DEFAULT_DEX_DIVISOR
        val owner = owner
        if (owner != null && owner is AbstractPlayer && owner.hasRelic("grackle:ExtraDexRelic")) {
            dexDivisor = DEFAULT_DEX_DIVISOR - 2
        }
    }

    companion object {
        @JvmField val POWER_ID = makeId(SpeedPower::class)
        @JvmField val STRINGS: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        const val DEFAULT_DEX_DIVISOR = 8

        /**
         * Returns true if the creature has enough `Speed` to boost the given card.
         */
        @JvmStatic fun canBoost(c: AbstractCreature, card: AbstractCard): Boolean {
            val speedRequired = SpeedBoostVar.inst.value(card)
            if (speedRequired == -1) {
                return false
            }
            val speedHas = c.getPower(POWER_ID)?.amount ?: 0
            return speedHas >= speedRequired
        }

        /**
         * If the creature has enough power to apply the boost, removes that much `Speed`
         * and returns true, else does nothing and returns false.
         */
        @JvmStatic fun tryBoost(c: AbstractCreature, card: AbstractCard): Boolean {
            val power = c.getPower(POWER_ID)
            val speedHas = power?.amount ?: 0
            val speedRequired = SpeedBoostVar.inst.value(card)
            return if (speedRequired != -1 && speedHas >= speedRequired) {
                if (speedRequired > 0) {
                    MiscUtil.addToBot(ReducePowerAction(c, c, power, speedRequired))
                }
                true
            } else {
                false
            }
        }

        @JvmStatic val needMoreSpeed: String get() = STRINGS.DESCRIPTIONS[1]
    }
}