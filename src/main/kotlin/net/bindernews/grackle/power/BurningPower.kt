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
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters()
                .areMonstersBasicallyDead()
        ) {
            if (owner !is AbstractPlayer) {
                dealGroupDamage()
            }
        }
    }

    override fun atEndOfTurn(isPlayer: Boolean) {
        if (owner is AbstractPlayer && isPlayer) {
            dealGroupDamage()
        }
    }

    override fun atDamageReceive(damage: Float, damageType: DamageType): Float {
        return if (damageType == DamageType.NORMAL) {
            damage + (amount * receiveExtraDamage)
        } else {
            damage
        }
    }

    private fun dealGroupDamage() {
        flashWithoutSound()
        // Damage the owner
        applyDamage(owner, amount)
        // If the owner is a monster, damage all monsters
        if (owner !is AbstractPlayer) {
            val aoeDamage = amount / 2
            for (monster in AbstractDungeon.getMonsters().monsters) {
                if (monster !== owner) {
                    applyDamage(monster, aoeDamage)
                }
            }
        }
        // Reduce power
        addToBot(ReducePowerAction(owner, owner, POWER_ID, REDUCE_PER_TURN))
    }

    private fun applyDamage(target: AbstractCreature, dmg: Int) {
        val info = BindingHelper.makeInfo(damageMods, owner, dmg, DamageType.THORNS)
        addToBot(DamageAction(target, info, AttackEffect.FIRE))
    }

    override fun updateDescription() {
        description = if (owner is AbstractPlayer) {
            strings.DESCRIPTIONS[0]
        } else {
            strings.DESCRIPTIONS[1]
        }
        description += amount.toString() + strings.DESCRIPTIONS[2]
    }

    override fun makeCopy(): AbstractPower {
        return BurningPower(owner, source, amount)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(BurningPower::class.java)

        /** How much this is reduced by each turn.  */
        var REDUCE_PER_TURN = 2

        /**
         * Creatures with Burning will receive extra damage equal to the amount of burning multiplied by this value.
         * Set to 0.0 to disable.
         */
        var receiveExtraDamage: Float = 1.0f

        fun makeAction(
            source: AbstractCreature?, target: AbstractCreature, amount: Int
        ): AbstractGameAction {
            return ApplyPowerAction(target, source, BurningPower(target, source, amount), amount)
        }
    }
}