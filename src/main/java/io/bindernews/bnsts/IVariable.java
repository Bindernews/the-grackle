package io.bindernews.bnsts;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface IVariable {

    int value(AbstractCard card);
    int baseValue(AbstractCard card);
    boolean upgraded(AbstractCard card);
    boolean isModified(AbstractCard card);

    void setValue(AbstractCard card, int amount);
    void setBaseValue(AbstractCard card, int amount);
    void upgrade(AbstractCard card, int amount);
}
