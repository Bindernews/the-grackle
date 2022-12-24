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

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<Integer> extraHits = new SpireField<>(() -> -1);
        public static SpireField<Integer> baseExtraHits = new SpireField<>(() -> -1);
        public static SpireField<Boolean> extraHitsUpgraded = new SpireField<>(() -> false);
    }

    @SuppressWarnings("unused")
    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    static class patchApplyPowers {
        public static void Postfix(AbstractCard __instance) {
            if (inst.baseValue(__instance) != -1) {
                val extra = GrackleMod.multiHitManager.getExtraHitsPlayer(__instance, 0);
                inst.setValue(__instance, inst.baseValue(__instance) + extra);
            }
        }
    }

    @SuppressWarnings("unused")
    @SpirePatch(clz = AbstractCard.class, method = "resetAttributes")
    static class patchResetAttributes {
        public static void Postfix(AbstractCard __instance) {
            inst.setValue(__instance, inst.baseValue(__instance));
        }
    }

    public interface Mixin {
        default int getExtraHits() {
            return inst.value((AbstractCard) this);
        }
    }
}
