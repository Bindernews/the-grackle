package io.bindernews.thegrackle.patches;

import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.status.Burn;
import io.bindernews.thegrackle.cardmods.EmbodyFireMod;

@SuppressWarnings("unused")
@SpirePatch(clz = Burn.class, method = SpirePatch.CONSTRUCTOR)
public class BurnPatches {
    public static void Postfix(Burn __instance) {
        DamageModifierManager.addModifier(__instance, new EmbodyFireMod());
    }
}
