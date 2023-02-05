@file:Suppress("ClassName", "FunctionName")

package net.bindernews.grackle.patches

import charbosses.stances.AbstractEnemyStance
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import net.bindernews.grackle.downfall.stances.EnStanceAloft
import net.bindernews.grackle.downfall.stances.EnStancePhoenix

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