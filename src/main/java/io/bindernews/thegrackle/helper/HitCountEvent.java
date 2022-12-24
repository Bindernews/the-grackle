package io.bindernews.thegrackle.helper;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import lombok.Data;

import java.util.Set;

@Data
public class HitCountEvent {
    final AbstractCreature source;

    /**
     * The card that created this event.
     */
    final AbstractCard card;

    /**
     * Tags indicating damage type, source mod, etc.
     */
    final Set<String> tags;
    final int baseCount;
    int count;

    public void addCount(int deltaCount) {
        count += deltaCount;
    }
}
