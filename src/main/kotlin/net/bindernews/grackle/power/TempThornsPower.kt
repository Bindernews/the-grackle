package net.bindernews.grackle.power

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.GrackleMod

class TempThornsPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        loadRegion("thorns")
        isTurnBased = true
        updateDescription()
    }

    override fun onAttacked(info: DamageInfo, damageAmount: Int): Int {
        val dt = info.type
        val own = info.owner
        if (dt != DamageType.THORNS && dt != DamageType.HP_LOSS && own != null && own !== owner) {
            flash()
            addToTop(DamageAction(own, DamageInfo(owner, amount, DamageType.THORNS), SLASH_HORIZONTAL, true))
        }
        return damageAmount
    }

    override fun updateDescription() {
        description = formatDesc(0, amount)
    }

    override fun atStartOfTurn() {
        addToBot(RemoveSpecificPowerAction(owner, owner, this))
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(TempThornsPower::class.java)
    }
}