package io.bindernews.thegrackle.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.WhirlwindAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Blizzard;
import com.megacrit.cardcrawl.cards.blue.ThunderStrike;
import com.megacrit.cardcrawl.cards.green.DaggerSpray;
import com.megacrit.cardcrawl.cards.green.Eviscerate;
import com.megacrit.cardcrawl.cards.red.SwordBoomerang;
import com.megacrit.cardcrawl.cards.red.Whirlwind;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.api.IMultiHitManager;
import io.bindernews.thegrackle.helper.ExtraHitModifier;
import lombok.val;

import java.util.HashMap;

@SuppressWarnings("unused")
public class MultiHitPatches {

    public static final String CTOR = "<ctor>";

    private static final HashMap<Class<?>, ExtraHitModifier> hitModifers = new HashMap<>();
    static {
        val m = hitModifers;
        m.put(SwordBoomerang.class, new ExtraHitModifier.SwordBoomerangMod());
        m.put(Eviscerate.class, new ExtraHitModifier.SimpleExtraHits(AbstractGameAction.AttackEffect.SLASH_HEAVY));
        m.put(DaggerSpray.class, new ExtraHitModifier.DaggerSprayMod());
    }

    public static IMultiHitManager manager() {
        return GrackleMod.getMultiHitManager();
    }

    @SpirePatch2(clz = WhirlwindAction.class, method = "update")
    public static class patchWhirlwind {
        @SpireInsertPatch(rloc = 42 - 37, localvars = {"effect"})
        public static void Insert(WhirlwindAction __instance, @ByRef int[] effect) {
            AbstractPlayer p = ReflectionHacks.getPrivate(__instance, WhirlwindAction.class, "p");
            effect[0] += manager().getExtraHits(p, effect[0]);
        }
    }

    @SpirePatch2(clz = Blizzard.class, method = "use")
    public static class patchBlizzard {
        @SpireInsertPatch(rloc = 49 - 47, localvars = {"frostCount"})
        public static void Insert(Blizzard __instance, @ByRef int[] frostCount) {
            frostCount[0] += manager().getExtraHitsCard(__instance, frostCount[0]);
        }
    }

    @SpirePatch2(clz = ThunderStrike.class, method = "use")
    public static class patchThunderStrikeUse {
        @SpireInsertPatch(rloc = 37 - 36)
        public static void Insert(ThunderStrike __instance) {
            __instance.baseMagicNumber += manager().getExtraHitsCard(__instance, __instance.baseMagicNumber);
        }
    }

    @SpirePatches({
            @SpirePatch(clz = Blizzard.class, method = CTOR),
            @SpirePatch(clz = ThunderStrike.class, method = CTOR),
            @SpirePatch(clz = Whirlwind.class, method = CTOR),
    })
    public static class TagMultiHitInternal {
        public static void Postfix(AbstractCard __instance) {
            manager().makeMultiHit(__instance);
        }
    }

    @SpirePatches({
            @SpirePatch(clz = SwordBoomerang.class, method = CTOR),
            @SpirePatch(clz = Eviscerate.class, method = CTOR),
    })
    public static class TagMultiHitExternal {
        public static void Postfix(AbstractCard __instance) {
            manager().makeMultiHit(__instance);
            DamageModifierManager.addModifier(__instance, hitModifers.get(__instance.getClass()));
        }
    }
}
