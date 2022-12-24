package io.bindernews.thegrackle.helper;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.bindernews.bnsts.EventEmit;
import io.bindernews.thegrackle.api.IMultiHitManager;
import lombok.val;

import java.util.Collections;

/**
 * Manager for extra multi-hit effect. May be discarded later, not sure yet.
 */
public class MultiHitManager implements IMultiHitManager {

    /**
     * Indicates that the card supports multi-hit.
     */
    @SpireEnum
    public static AbstractCard.CardTags GK_MULTI_HIT;

    public static final EventEmit<HitCountEvent> hitCountEvents = new EventEmit<>();

    @Override
    public int getExtraHits(AbstractCreature source, int initial) {
        if (source == null) {
            return 0;
        }
        val event = new HitCountEvent(source, null, Collections.emptySet(), 0);
        event.setCount(event.getBaseCount());
        hitCountEvents.publish(event);
        return Math.max(event.getCount(), -initial);
    }

    @Override
    public int getExtraHitsPlayer(AbstractCard card, int initial) {
        val source = AbstractDungeon.player;
        val event = new HitCountEvent(source, card, Collections.emptySet(), 0);
        event.setCount(event.getBaseCount());
        hitCountEvents.publish(event);
        // Limit final hit count to 0
        return Math.max(event.getCount(), -initial);
    }

    @Override
    public AbstractCard.CardTags getMultiHitTag() {
        return GK_MULTI_HIT;
    }

    @Override
    public void tagMultiHit(AbstractCard card) {
        card.tags.add(GK_MULTI_HIT);
    }
}
