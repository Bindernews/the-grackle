package net.bindernews.grackle.downfall

import charbosses.relics.AbstractCharbossRelic
import com.megacrit.cardcrawl.relics.AbstractRelic
import net.bindernews.bnsts.IField


object DfUtil {
    val fBaseRelic = IField.unreflect(AbstractCharbossRelic::class.java, AbstractRelic::class.java, "baseRelic")

    fun setImagesFromBase(relic: AbstractCharbossRelic) {
        val base = fBaseRelic.get(relic)
        relic.img = base.img
        relic.largeImg = base.largeImg
        relic.outlineImg = base.outlineImg
    }
}