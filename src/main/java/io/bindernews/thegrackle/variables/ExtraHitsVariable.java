package io.bindernews.thegrackle.variables;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.EventEmit;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.MiscUtil;
import io.bindernews.thegrackle.api.IMultiHitManager;
import io.bindernews.thegrackle.cardmods.ExtraHitsMod;
import io.bindernews.thegrackle.helper.HitCountEvent;
import lombok.Getter;
import lombok.val;

import java.util.Collections;
import java.util.function.Consumer;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class ExtraHitsVariable extends AbstractSimpleVariable implements IMultiHitManager {
    public static final String KEY = GrackleMod.makeId("hits");

    /**
     * Indicates that the card supports multi-hit.
     */
    @SpireEnum
    public static AbstractCard.CardTags GK_MULTI_HIT;

    public static ExtraHitsVariable inst;

    @Getter
    private static final EventEmit<HitCountEvent> onApplyPowers = new EventEmit<>();

    public ExtraHitsVariable() {
        super(Fields.extraHits, new VariableInst());
        inst = this;
    }

    @Override
    public String key() {
        return KEY;
    }

    /**
     * Returns the number of extra hits for a creature. This MAY be the same
     * value as calculated by {@link #applyPowers}, but not always.
     * @param source Creature emitting the event
     * @param initial Initial hit amount
     * @return number of extra hits
     */
    @Override
    public int getExtraHits(AbstractCreature source, int initial) {
        if (source == null) {
            return 0;
        }
        val event = new HitCountEvent(source, null, Collections.emptySet(), 0);
        event.setCount(event.getBaseCount());
        getOnApplyPowers().emit(event);
        // Limit to 0
        return Math.max(event.getCount(), -initial);
    }

    @Override
    public int getExtraHitsCard(AbstractCard card, int initial) {
        // Limit final hit count to 0
        return Math.max(ExtraHitsVariable.inst.value(card), -initial);
    }

    @Override
    public void makeMultiHit(AbstractCard card) {
        ExtraHitsMod.applyTo(card);
    }

    /**
     * Callback for {@link AbstractCard#applyPowers()}
     */
    public void applyPowers(AbstractCard card) {
        if (baseValue(card) != -1) {
            val source = iop().getCardOwner(card);
            val event = new HitCountEvent(source, card, Collections.emptySet(), 0);
            event.setCount(event.getBaseCount());
            onApplyPowers.emit(event);
            // Limit final hit count to 0
            val extra = Math.max(event.getCount(), 0);
            setValue(card, baseValue(card) + extra);
        }
    }

    public void resetAttributes(AbstractCard card) {
        setValue(card, baseValue(card));
    }

    /**
     * Utility function to create a listener that will add hits based on
     * how much of a certain power the source creature has.
     * @param powerId Power ID to use
     * @return Event handler
     */
    public static Consumer<HitCountEvent> addPowerAmount(final String powerId) {
        return ev -> ev.addCount(MiscUtil.getPowerAmount(ev.getSource(), powerId, 0));
    }

    public interface Mixin {
        default int getExtraHits() {
            return inst.value((AbstractCard) this);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<VariableInst> extraHits = new SpireField<>(() -> null);
    }
}
