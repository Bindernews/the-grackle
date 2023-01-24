@file:Suppress("FunctionName", "unused")

package io.bindernews.thegrackle.patches

import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.status.Burn
import io.bindernews.thegrackle.cardmods.EmbodyFireMod

@SpirePatch(clz = Burn::class, method = SpirePatch.CONSTRUCTOR)
object BurnPatches {
    @JvmStatic fun Postfix(__instance: Burn) {
        DamageModifierManager.addModifier(__instance, EmbodyFireMod())
    }
}