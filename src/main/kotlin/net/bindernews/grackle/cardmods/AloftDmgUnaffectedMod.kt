package net.bindernews.grackle.cardmods

import basemod.abstracts.AbstractCardModifier
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.stance.StanceAloft

class AloftDmgUnaffectedMod : AbstractCardModifier() {

    private var stance: StanceAloft? = null

    override fun modifyBaseDamage(damage: Float, type: DamageInfo.DamageType, card: AbstractCard, target: AbstractMonster?): Float {
        stance = iop().getCardOwner(card)?.let { StanceAloft.getInstanceOn(it).orElse(null) }
        stance?.enabled = false
        return damage
    }

    override fun modifyDamageFinal(damage: Float, type: DamageInfo.DamageType?, card: AbstractCard?, target: AbstractMonster?): Float {
        stance?.enabled = true
        stance = null
        return damage
    }

    override fun makeCopy(): AbstractCardModifier = AloftDmgUnaffectedMod()
}