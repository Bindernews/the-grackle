@file:Suppress("UNUSED_PARAMETER", "FunctionName", "unused")

package net.bindernews.grackle.patches

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.TipHelper
import com.megacrit.cardcrawl.metrics.Metrics
import com.megacrit.cardcrawl.screens.GameOverScreen
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.Events.metricsRun
import net.bindernews.grackle.Events.popups
import net.bindernews.grackle.cardmods.EmbodyFireMod
import net.bindernews.grackle.stance.StanceAloft
import net.bindernews.grackle.stance.StanceEagle
import net.bindernews.grackle.stance.StancePhoenix

@SpirePatch(clz = Metrics::class, method = "run")
object MetricsRunPatch {
    @JvmStatic fun Postfix(metrics: Metrics?) {
        metricsRun.emit(metrics)
    }
}

@SpirePatch(clz = GameOverScreen::class, method = "shouldUploadMetricData")
object ShouldUploadMetricData {
    @JvmStatic fun Postfix(returnValue: Boolean): Boolean {
        return Settings.UPLOAD_DATA
    }
}

@SpirePatch(clz = TipHelper::class, method = "render")
object TipHelperOnRender {
    @JvmStatic fun Postfix(sb: SpriteBatch) {
        popups.handlers.forEach { p ->
            if (p.isEnabled()) p.render(sb)
        }
    }
}

@SpirePatch(clz = AbstractStance::class, method = "getStanceFromName")
object GetStanceFromName {
    val stances = mapOf<String, Class<out AbstractStance>>(
        StanceAloft.STANCE_ID to StanceAloft::class.java,
        StanceEagle.STANCE_ID to StanceEagle::class.java,
        StancePhoenix.STANCE_ID to StancePhoenix::class.java,
    )

    @JvmStatic fun Prefix(name: String): SpireReturn<AbstractStance> {
        val stanceRet = stances[name]?.getConstructor()?.newInstance()
        return if (stanceRet != null) {
            SpireReturn.Return(stanceRet)
        } else {
            SpireReturn.Continue()
        }
    }
}

@SpirePatch(clz = Burn::class, method = SpirePatch.CONSTRUCTOR)
object BurnCtor {
    @JvmStatic fun Postfix(__instance: Burn) {
        DamageModifierManager.addModifier(__instance, EmbodyFireMod())
    }
}
