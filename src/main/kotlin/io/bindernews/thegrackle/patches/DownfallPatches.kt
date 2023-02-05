@file:Suppress("ClassName", "FunctionName")

package io.bindernews.thegrackle.patches

import charbosses.stances.AbstractEnemyStance
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import io.bindernews.thegrackle.downfall.stances.EnStanceAloft
import io.bindernews.thegrackle.downfall.stances.EnStancePhoenix

@Suppress("unused")
object DownfallPatches {
    const val MOD = "downfall"

    @SpirePatch(clz = AbstractEnemyStance::class, method = "getStanceFromName", requiredModId = MOD)
    object patchEnemyStances {
        @JvmStatic fun Prefix(name: String): SpireReturn<AbstractEnemyStance> {
            if (name == EnStancePhoenix.STANCE_ID) {
                return SpireReturn.Return(EnStancePhoenix())
            } else if (name == EnStanceAloft.STANCE_ID) {
                return SpireReturn.Return(EnStanceAloft())
            }
            return SpireReturn.Continue()
        }
    }
}