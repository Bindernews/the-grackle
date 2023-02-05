@file:Suppress("FunctionName", "ClassName")

package io.bindernews.thegrackle.patches

import basemod.ReflectionHacks
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.unique.WhirlwindAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.blue.Blizzard
import com.megacrit.cardcrawl.cards.blue.ThunderStrike
import com.megacrit.cardcrawl.cards.green.DaggerSpray
import com.megacrit.cardcrawl.cards.green.Eviscerate
import com.megacrit.cardcrawl.cards.red.SwordBoomerang
import com.megacrit.cardcrawl.cards.red.Whirlwind
import com.megacrit.cardcrawl.characters.AbstractPlayer
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.api.IMultiHitManager
import io.bindernews.thegrackle.helper.ExtraHitModifier
import io.bindernews.thegrackle.helper.ExtraHitModifier.*

@Suppress("unused")
object MultiHitPatches {
    const val CTOR = "<ctor>"
    private val hitModifiers = HashMap<Class<*>, ExtraHitModifier>(mapOf(
        SwordBoomerang::class.java to SwordBoomerangMod(),
        Eviscerate::class.java to SimpleExtraHits(AbstractGameAction.AttackEffect.SLASH_HEAVY),
        DaggerSpray::class.java to DaggerSprayMod(),
    ))

    @JvmStatic fun manager(): IMultiHitManager {
        return GrackleMod.getMultiHitManager()
    }

    @SpirePatch2(clz = WhirlwindAction::class, method = "update")
    object patchWhirlwind {
        @SpireInsertPatch(rloc = 42 - 37, localvars = ["effect"])
        @JvmStatic fun Insert(__instance: WhirlwindAction, @ByRef effect: IntArray) {
            val p = ReflectionHacks.getPrivate<AbstractPlayer>(__instance, WhirlwindAction::class.java, "p")
            effect[0] += manager().getExtraHits(p, effect[0])
        }
    }

    @SpirePatch2(clz = Blizzard::class, method = "use")
    object patchBlizzard {
        @SpireInsertPatch(rloc = 49 - 47, localvars = ["frostCount"])
        @JvmStatic fun Insert(__instance: Blizzard, @ByRef frostCount: IntArray) {
            frostCount[0] += manager().getExtraHitsCard(__instance, frostCount[0])
        }
    }

    @SpirePatch2(clz = ThunderStrike::class, method = "use")
    object patchThunderStrikeUse {
        @SpireInsertPatch(rloc = 37 - 36)
        @JvmStatic fun Insert(__instance: ThunderStrike) {
            __instance.baseMagicNumber += manager().getExtraHitsCard(__instance, __instance.baseMagicNumber)
        }
    }

    @SpirePatches(
        SpirePatch(clz = Blizzard::class, method = CTOR),
        SpirePatch(clz = ThunderStrike::class, method = CTOR),
        SpirePatch(clz = Whirlwind::class, method = CTOR)
    )
    object TagMultiHitInternal {
        @JvmStatic fun Postfix(__instance: AbstractCard) {
            manager().makeMultiHit(__instance)
        }
    }

    @SpirePatches(
        SpirePatch(clz = SwordBoomerang::class, method = CTOR),
        SpirePatch(clz = Eviscerate::class, method = CTOR)
    )
    object TagMultiHitExternal {
        @JvmStatic fun Postfix(__instance: AbstractCard) {
            manager().makeMultiHit(__instance)
            DamageModifierManager.addModifier(__instance, hitModifiers[__instance.javaClass])
        }
    }
}