package net.bindernews.grackle.power

import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.rooms.AbstractRoom
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.cardmods.EmbodyFireMod

class BurningPower(owner: AbstractCreature, val source: AbstractCreature?, amount: Int) : BasePower(POWER_ID) {
    private val damageMods: DamageModContainer

    @Suppress("unused")
    constructor(owner: AbstractCreature, amount: Int) : this(owner, null, amount)

    init {
        setOwnerAmount(owner, amount)
        damageMods = DamageModContainer(this, EmbodyFireMod())
        isTurnBased = true
        type = PowerType.DEBUFF
        if (this.amount >= 9999) {
            this.amount = 9999
        }
        updateDescription()
        loadRegion("flameBarrier")
    }

    override fun atStartOfTurn() {
//        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
//            && owner is AbstractMonster
//            && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()
//        ) {
//            processOwnerDamage()
//        }
    }

    override fun atEndOfTurn(isPlayer: Boolean) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
            && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()
        ) {
            processOwnerDamage()
        }
    }

    override fun atDamageReceive(damage: Float, type: DamageType?): Float {
        return if (type == DamageType.NORMAL) {
            damage * damageMultiplier
        } else {
            damage
        }
    }

    /**
     * Process the damage to the owner and decreasing the amount of the power.
     */
    private fun processOwnerDamage() {
        flashWithoutSound()
        // Damage the owner
        applyDamage(owner, amount)
        // Reduce debuff by half, rounded up
        addToBot(ReducePowerAction(owner, owner, POWER_ID, (amount + 1) / 2))
    }

    private fun applyDamage(target: AbstractCreature, dmg: Int) {
        val info = BindingHelper.makeInfo(damageMods, owner, dmg, DamageType.THORNS)
        addToBot(DamageAction(target, info, AttackEffect.FIRE))
    }

    override fun updateDescription() {
        description = strings.DESCRIPTIONS[0].format(damagePercent) + "\n"
        description += if (owner is AbstractPlayer) {
            strings.DESCRIPTIONS[1]
        } else {
            strings.DESCRIPTIONS[2]
        }
        description += amount.toString() + strings.DESCRIPTIONS[3]
    }

    override fun makeCopy(): AbstractPower {
        return BurningPower(owner, source, amount)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(BurningPower::class.java)

        /**
         * Damage received multiplier, basically like vulnerable.
         */
        val damageMultiplier: Float
            get() = (100f + damagePercent.toFloat()) / 100f
        var damagePercent: Int = 50


        fun makeAction(
            source: AbstractCreature?, target: AbstractCreature, amount: Int
        ): AbstractGameAction {
            return ApplyPowerAction(target, source, BurningPower(target, source, amount), amount)
        }
    }
}