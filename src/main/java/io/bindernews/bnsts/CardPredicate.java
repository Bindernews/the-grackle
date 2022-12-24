package io.bindernews.bnsts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@FunctionalInterface
public interface CardPredicate<T extends AbstractCard> {
    boolean test(T card, AbstractPlayer p, AbstractMonster m);

    default CardPredicate<T> negate() {
        final CardPredicate<T> self = this;
        return (card, p, m) -> (!self.test(card, p, m));
    }

    default CardPredicate<T> and(CardPredicate<T> other) {
        final CardPredicate<T> self = this;
        return (card, p, m) -> (self.test(card, p, m) && other.test(card, p, m));
    }

    default CardPredicate<T> or(CardPredicate<T> other) {
        final CardPredicate<T> self = this;
        return (card, p, m) -> (self.test(card, p, m) || other.test(card, p, m));
    }

    static <T extends AbstractCard> CardPredicate<T> of(CardPredicate<T> p) {
        return p;
    }
}
