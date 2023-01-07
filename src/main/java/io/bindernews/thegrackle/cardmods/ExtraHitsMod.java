package io.bindernews.thegrackle.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;

public class ExtraHitsMod extends AbstractCardModifier {

    /**
     * Convenience method to add this modifier to a card.
     * @param card Card to add this modifier to
     */
    public static void applyTo(AbstractCard card) {
        CardModifierManager.addModifier(card, new ExtraHitsMod());
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.tags.add(ExtraHitsVariable.GK_MULTI_HIT);
        // Make sure to initialize the base value to 0 so applyPowers works properly.
        if (ExtraHitsVariable.inst.baseValue(card) == -1) {
            ExtraHitsVariable.inst.setBaseValue(card, 0);
        }
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        ExtraHitsVariable.inst.applyPowers(card);
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        ExtraHitsVariable.inst.resetAttributes(card);
        return false;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ExtraHitsMod();
    }
}
