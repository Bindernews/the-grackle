package io.bindernews.thegrackle.helper

import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.metrics.Metrics
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.cards.BaseCard
import io.bindernews.thegrackle.patches.Fields
import io.bindernews.thegrackle.variables.ExtraHitsVariable
import io.bindernews.thegrackle.variables.Magic2Var
import kotlin.reflect.KClass

fun CardVariables.hits(base: Int, upg: Int) {
    add(ExtraHitsVariable.inst, base, upg)
}
fun CardVariables.magic2(base: Int, upg: Int) {
    add(Magic2Var.inst, base, upg)
}

inline val CardStrings.flavorText: String
    get() = FlavorText.CardStringsFlavorField.flavor[this]

inline fun <reified T: Any> makeId(clazz: KClass<T>): String = "grackle:" + clazz.simpleName

inline var BaseCard.extraHits: Int
    get() = ExtraHitsVariable.inst.value(this)
    set(value) = ExtraHitsVariable.inst.setValue(this, value)
inline var AbstractCard.baseExtraHits: Int
    get() = ExtraHitsVariable.inst.baseValue(this)
    set(value) = ExtraHitsVariable.inst.setBaseValue(this, value)

inline var BaseCard.magic2: Int
    get() = Magic2Var.inst.value(this)
    set(value) = Magic2Var.inst.setValue(this, value)
inline var BaseCard.baseMagic2: Int
    get() = Magic2Var.inst.baseValue(this)
    set(value) = Magic2Var.inst.setBaseValue(this, value)

/**
 * Total amount of Fireheart gained this combat.
 */
inline var AbstractCreature.fireheartGained: Int
    get() = Fields.fireheartGained.get(this)
    set(value) = Fields.fireheartGained.set(this, value)

/**
 * Upload the metrics to the passed url in JSON format.
  * @param url URL to upload data to
 */
fun Metrics.sendPost(url: String) {
    try {
        val m = Metrics::class.java.getDeclaredMethod("sendPost", String::class.java, String::class.java)
        m.isAccessible = true
        m.invoke(this, url, null)
    } catch (ex: ReflectiveOperationException) {
        GrackleMod.log.error("Exception while sending metrics to $url", ex)
    }
}
