package net.bindernews.grackle.downfall.stances

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType

interface EnemyStanceDelegate {
    fun atStartOfTurn()
    fun onEndOfTurn()
    fun onEnterStance()
    fun onExitStance()
    fun atDamageGive(damage: Float, type: DamageType): Float
    fun atDamageReceive(damage: Float, damageType: DamageType): Float
    fun onPlayCard(card: AbstractCard?)
    fun update()
    fun updateAnimation()
    fun updateDescription()
    fun render(sb: SpriteBatch)
    fun stopIdleSfx()
    val description: String
}