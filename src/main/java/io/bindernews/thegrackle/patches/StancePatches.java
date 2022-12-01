package io.bindernews.thegrackle.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import io.bindernews.thegrackle.stance.StanceAloft;
import io.bindernews.thegrackle.stance.StancePhoenix;
import com.megacrit.cardcrawl.stances.AbstractStance;

@SpirePatch(clz = AbstractStance.class, method = "getStanceFromName")
public class StancePatches {
    public static SpireReturn<AbstractStance> Prefix(String name) {
        if (name.equals(StancePhoenix.STANCE_ID)) {
            return SpireReturn.Return(new StancePhoenix());
        } else if (name.equals(StanceAloft.STANCE_ID)) {
            return SpireReturn.Return(new StanceAloft());
        }
        return SpireReturn.Continue();
    }
}
