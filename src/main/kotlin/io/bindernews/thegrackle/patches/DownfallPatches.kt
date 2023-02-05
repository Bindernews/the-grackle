package io.bindernews.thegrackle.patches;

import charbosses.stances.AbstractEnemyStance;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import io.bindernews.thegrackle.downfall.stances.EnStanceAloft;
import io.bindernews.thegrackle.downfall.stances.EnStancePhoenix;

@SuppressWarnings("unused")
public class DownfallPatches {
    public static final String MOD = "downfall";

    @SpirePatch(clz = AbstractEnemyStance.class, method = "getStanceFromName", requiredModId = MOD)
    public static class patchEnemyStances {
        public static SpireReturn<AbstractEnemyStance> Prefix(String name) {
            if (name.equals(EnStancePhoenix.STANCE_ID)) {
                return SpireReturn.Return(new EnStancePhoenix());
            } else if (name.equals(EnStanceAloft.STANCE_ID)) {
                return SpireReturn.Return(new EnStanceAloft());
            }
            return SpireReturn.Continue();
        }
    }
}
