package io.bindernews.thegrackle.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SvcChangeCardEvent {
    private final AbstractCard card;
    private final CardGroup group;
    private final boolean open;

    public AbstractCard getCard() {
        return card;
    }

    public CardGroup getGroup() {
        return group;
    }

    public boolean isOpen() {
        return open;
    }
}
