package io.bindernews.thegrackle.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;

public class SuplexMod extends AbstractCardModifier {

    public SuplexMod() {
        // Decrease priority
        priority = 1;
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        card.damage += card.baseMagicNumber * ExtraHitsVariable.inst.value(card);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SuplexMod();
    }
}
