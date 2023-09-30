package net.bindernews.grackle.vfx

import com.megacrit.cardcrawl.vfx.combat.GiantTextEffect
import net.bindernews.bnsts.IField
import net.bindernews.grackle.helper.DelegField

/**
 * Just [GiantTextEffect] but with the ability to change the string.
 */
class MyGiantTextEffect(x: Float, y: Float, targetString: String) : GiantTextEffect(x, y) {
    var targetString: String by fTargetString

    init {
        this.targetString = targetString
    }

    companion object {
        private val fTargetString =
            DelegField(IField.unreflect(GiantTextEffect::class.java, String::class.java, "targetString"))
    }
}


