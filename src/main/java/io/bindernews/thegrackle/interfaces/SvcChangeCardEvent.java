package io.bindernews.thegrackle.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import lombok.Data;

@Data
public
class SvcChangeCardEvent {
    private final AbstractCard card;
    private final CardGroup group;
    private final boolean open;
}
