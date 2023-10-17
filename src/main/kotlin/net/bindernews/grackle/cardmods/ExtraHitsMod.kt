package net.bindernews.grackle.cardmods

import basemod.abstracts.AbstractCardModifier
import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.cards.HenPeck
import net.bindernews.grackle.helper.baseExtraHits
import net.bindernews.grackle.helper.baseMagic2
import net.bindernews.grackle.helper.extraHits
import net.bindernews.grackle.helper.magic2
import net.bindernews.grackle.variables.ExtraHitsVariable

/**
 * Updates the extraHits field of cards. This should always be added during a card's initialization,
 * because it will NOT be saved externally.
 */
@AbstractCardModifier.SaveIgnore
class ExtraHitsMod(private val showTotalDamage: Boolean = true) : AbstractCardModifier() {
    override fun onInitialApplication(card: AbstractCard) {
        card.tags.add(ExtraHitsVariable.GK_MULTI_HIT)
        // Make sure to initialize the base value to 0 so applyPowers works properly.
        if (ExtraHitsVariable.inst.baseValue(card) == -1) {
            ExtraHitsVariable.inst.setBaseValue(card, 0)
        }
        if (showTotalDamage) {
            card.baseMagic2 = 0
            card.magic2 = 0
        }
    }

    override fun isInherent(card: AbstractCard): Boolean = true

    override fun onApplyPowers(card: AbstractCard) {
        ExtraHitsVariable.inst.applyPowers(card)
        if (showTotalDamage) {
            card.baseMagic2 = card.baseDamage * card.baseExtraHits
            card.magic2 = card.damage * card.extraHits
        }
    }

    override fun removeAtEndOfTurn(card: AbstractCard): Boolean {
        ExtraHitsVariable.inst.resetAttributes(card)
        return false
    }

    override fun onCalculateCardDamage(card: AbstractCard, mo: AbstractMonster) {
        if (showTotalDamage) {
            card.magic2 = card.damage * card.extraHits
        } else {
            card.magic2 = 0
        }
    }

    override fun modifyDescription(rawDescription: String, card: AbstractCard): String {
        return if (showTotalDamage && card.magic2 > 0) {
            rawDescription + " NL " + GrackleMod.miscUI["total_damage_desc"]
        } else {
            rawDescription
        }
    }

    override fun makeCopy(): AbstractCardModifier {
        return ExtraHitsMod(showTotalDamage)
    }
}