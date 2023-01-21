package io.bindernews.thegrackle.helper

import com.badlogic.gdx.graphics.Texture
import com.megacrit.cardcrawl.localization.RelicStrings
import com.megacrit.cardcrawl.relics.AbstractRelic
import io.bindernews.bnsts.IField
import io.bindernews.thegrackle.GrackleMod
import java.util.*


object RelicHelper {

    fun loadImages(relicId: String): Array<Texture> {
        val path = GrackleMod.MOD_RES + "/images/relics/" + GrackleMod.removePrefix(relicId)
        val tex = Objects.requireNonNull(GrackleMod.loadTexture("$path.png"))!!
        var texOutline = GrackleMod.loadTexture(path + "_o.png")
        if (texOutline == null) {
            texOutline = tex
        }
        return arrayOf(tex, texOutline, tex)
    }

    val fRelicStrings =
        IField.unreflect<AbstractRelic, RelicStrings>(AbstractRelic::class.java, "relicStrings")

    /** Access the relic's RelicStrings */
    inline val AbstractRelic.relicStrings: RelicStrings
        get() = fRelicStrings[this]
}

