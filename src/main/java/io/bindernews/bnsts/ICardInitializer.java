package io.bindernews.bnsts;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface ICardInitializer {
    void init(AbstractCard card);
    void upgrade(AbstractCard card);
}
