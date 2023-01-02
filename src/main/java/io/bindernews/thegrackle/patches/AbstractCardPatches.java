package io.bindernews.thegrackle.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;

@SuppressWarnings("unused")
public class AbstractCardPatches {

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    static class patchApplyPowers {
        public static void Postfix(AbstractCard __instance) {
            ExtraHitsVariable.inst.applyPowers(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "resetAttributes")
    static class patchResetAttributes {
        public static void Postfix(AbstractCard __instance) {
            ExtraHitsVariable.inst.resetAttributes(__instance);
        }
    }
}
