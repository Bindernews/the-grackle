package io.bindernews.thegrackle.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import io.bindernews.thegrackle.GrackleMod;
import lombok.Data;

import static io.bindernews.thegrackle.GrackleMod.getEventBus;

@FunctionalInterface
public interface OnScvChangeCard {
    void accept(Event ev);

    @Data
    class Event {
        private final AbstractCard card;
        private final CardGroup group;
        private final boolean open;


        public void emit() {
            getEventBus().emit(OnScvChangeCard.class, this);
        }
    }
}
