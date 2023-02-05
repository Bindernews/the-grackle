package net.bindernews.grackle.helper

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect
import net.bindernews.grackle.variables.ExtraHitsVariable

abstract class ExtraHitModifier : AbstractDamageModifier() {
    lateinit var attackEffect: AttackEffect
    var applied = false
    var extraHits = 0
    fun checkApplied(): Boolean {
        val b = applied
        applied = true
        return b
    }

    class SimpleExtraHits(effect: AttackEffect?) : ExtraHitModifier() {
        init {
            attackEffect = effect!!
        }

        override fun onAttack(info: DamageInfo, damageAmount: Int, target: AbstractCreature) {
            if (checkApplied()) {
                return
            }
            for (i in 0 until extraHits) {
                addToBot(DamageAction(target, info, attackEffect))
            }
        }

        override fun makeCopy(): AbstractDamageModifier {
            return SimpleExtraHits(attackEffect)
        }
    }

    class SwordBoomerangMod : ExtraHitModifier() {
        private var card: AbstractCard? = null
        override fun atDamageGive(
            damage: Float,
            type: DamageType,
            target: AbstractCreature,
            card: AbstractCard
        ): Float {
            this.card = card
            applied = false
            this.extraHits = ExtraHitsVariable.inst.getExtraHitsCard(card, 4)
            return damage
        }

        override fun onAttack(info: DamageInfo, damageAmount: Int, target: AbstractCreature) {
            if (checkApplied()) {
                return
            }
            for (i in 0 until extraHits) {
                addToBot(AttackDamageRandomEnemyAction(card, AttackEffect.SLASH_HORIZONTAL))
            }
        }

        override fun makeCopy(): AbstractDamageModifier {
            return SwordBoomerangMod()
        }
    }

    class DaggerSprayMod : ExtraHitModifier() {
        override fun atDamageFinalGive(
            damage: Float,
            type: DamageType,
            target: AbstractCreature,
            card: AbstractCard
        ): Float {
            val flipVfx = AbstractDungeon.getMonsters().shouldFlipVfx()
            val extraHits = ExtraHitsVariable.inst.getExtraHitsCard(card, 0)
            val p = AbstractDungeon.player
            for (i in 0 until extraHits) {
                addToBot(VFXAction(DaggerSprayEffect(flipVfx), 0.0f))
                addToBot(DamageAllEnemiesAction(p, card.multiDamage, card.damageTypeForTurn, AttackEffect.NONE))
            }
            return damage
        }

        override fun makeCopy(): AbstractDamageModifier {
            return DaggerSprayMod()
        }
    }
}