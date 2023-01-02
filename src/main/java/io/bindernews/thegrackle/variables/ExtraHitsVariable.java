package io.bindernews.thegrackle.variables;

import basemod.abstracts.DynamicVariable;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.bindernews.thegrackle.GrackleMod;
import lombok.val;

public class ExtraHitsVariable extends DynamicVariable {
    public static final String KEY = GrackleMod.makeId("hits");
    public static ExtraHitsVariable inst;

    public ExtraHitsVariable() {
        super();
        inst = this;
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return value(card) != baseValue(card);
    }

    @Override
    public int value(AbstractCard card) {
        return Fields.extraHits.get(card);
    }

    @Override
    public int baseValue(AbstractCard card) {
        return Fields.baseExtraHits.get(card);
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return Fields.extraHitsUpgraded.get(card);
    }

    public void setBaseValue(AbstractCard card, int amount) {
        Fields.extraHits.set(card, amount);
        Fields.baseExtraHits.set(card, amount);
        card.initializeDescription();
    }

    public void setValue(AbstractCard card, int amount) {
        Fields.extraHits.set(card, amount);
    }

    public void upgrade(AbstractCard card, int amount) {
        Fields.extraHitsUpgraded.set(card, true);
        setBaseValue(card, baseValue(card) + amount);
    }

    /**
     * Callback for {@link AbstractCard#applyPowers()}
     */
    public void applyPowers(AbstractCard card) {
        if (baseValue(card) != -1) {
            val extra = GrackleMod.multiHitManager.getExtraHitsPlayer(card, 0);
            setValue(card, value(card) + extra);
        }
    }

    public void resetAttributes(AbstractCard card) {
        setValue(card, baseValue(card));
    }

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<Integer> extraHits = new SpireField<>(() -> -1);
        public static SpireField<Integer> baseExtraHits = new SpireField<>(() -> -1);
        public static SpireField<Boolean> extraHitsUpgraded = new SpireField<>(() -> false);
    }



    public interface Mixin {
        default int getExtraHits() {
            return inst.value((AbstractCard) this);
        }
    }
}
