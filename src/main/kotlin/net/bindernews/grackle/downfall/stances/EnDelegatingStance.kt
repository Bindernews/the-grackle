package net.bindernews.grackle.downfall.stances

import charbosses.stances.AbstractEnemyStance
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType

@Suppress("LeakingThis")
abstract class EnDelegatingStance : AbstractEnemyStance() {
    abstract val inner: EnemyStanceDelegate

    override fun updateDescription() {
        inner.updateDescription()
        description = inner.description
    }

    override fun atStartOfTurn() {
        inner.atStartOfTurn()
    }

    override fun onEndOfTurn() {
        inner.onEndOfTurn()
    }

    override fun onEnterStance() {
        inner.onEnterStance()
    }

    override fun onExitStance() {
        inner.onExitStance()
    }

    override fun atDamageGive(damage: Float, type: DamageType): Float {
        return inner.atDamageGive(damage, type)
    }

    override fun atDamageReceive(damage: Float, damageType: DamageType): Float {
        return inner.atDamageReceive(damage, damageType)
    }

    override fun onPlayCard(card: AbstractCard) {
        inner.onPlayCard(card)
    }

    override fun update() {
        inner.update()
    }

    override fun updateAnimation() {
        inner.updateAnimation()
    }

    override fun render(sb: SpriteBatch) {
        inner.render(sb)
    }

    override fun stopIdleSfx() {
        inner.stopIdleSfx()
    }
}