package io.bindernews.thegrackle.cardmods

import basemod.abstracts.AbstractCardModifier
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.monsters.AbstractMonster
import io.bindernews.thegrackle.helper.ModInterop.iop
import io.bindernews.thegrackle.stance.StanceAloft
import java.util.Optional

class AloftDmgUnaffectedMod : AbstractCardModifier() {

    private var stance: Optional<StanceAloft> = Optional.empty()

    override fun modifyBaseDamage(damage: Float, type: DamageInfo.DamageType, card: AbstractCard, target: AbstractMonster?): Float {
        stance = iop().getCardOwner(card).let { StanceAloft.getInstanceOn(it) }
        stance.ifPresent { it.enabled = false }
        return damage
    }

    override fun modifyDamageFinal(damage: Float, type: DamageInfo.DamageType?, card: AbstractCard?, target: AbstractMonster?): Float {
        stance.ifPresent { it.enabled = true }
        stance = Optional.empty()
        return damage
    }

    override fun makeCopy(): AbstractCardModifier = AloftDmgUnaffectedMod()
}