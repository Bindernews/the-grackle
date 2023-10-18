package net.bindernews.grackle.helper

import basemod.ReflectionHacks
import basemod.abstracts.AbstractCardModifier
import basemod.helpers.CardModifierManager
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText.CardStringsFlavorField
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText.PotionStringsFlavorField
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.localization.PotionStrings
import com.megacrit.cardcrawl.metrics.Metrics
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.patches.Fields
import net.bindernews.grackle.variables.ExtraHitsVariable
import net.bindernews.grackle.variables.Magic2Var
import net.bindernews.grackle.variables.SpeedBoostVar
import kotlin.reflect.KClass

fun CardVariables.hits(base: Int, upg: Int) {
    add(ExtraHitsVariable.inst, base, upg)
}
fun CardVariables.magic2(base: Int, upg: Int) {
    add(Magic2Var.inst, base, upg)
}
fun CardVariables.speedBoost(base: Int, upg: Int) {
    add(SpeedBoostVar.inst, base, upg)
}

inline fun <reified T: Any> makeId(clazz: KClass<T>): String = "grackle:" + clazz.simpleName

inline var AbstractCard.extraHits: Int
    get() = ExtraHitsVariable.inst.value(this)
    set(value) = ExtraHitsVariable.inst.setValue(this, value)
inline var AbstractCard.baseExtraHits: Int
    get() = ExtraHitsVariable.inst.baseValue(this)
    set(value) = ExtraHitsVariable.inst.setBaseValue(this, value)

inline var AbstractCard.magic2: Int
    get() = Magic2Var.inst.value(this)
    set(value) = Magic2Var.inst.setValue(this, value)
inline var AbstractCard.baseMagic2: Int
    get() = Magic2Var.inst.baseValue(this)
    set(value) = Magic2Var.inst.setBaseValue(this, value)

inline var CardStrings.FLAVOR: String
    get() = CardStringsFlavorField.flavor.get(this)
    set(value) = CardStringsFlavorField.flavor.set(this, value)
inline var PotionStrings.FLAVOR: String
    get() = PotionStringsFlavorField.flavor.get(this)
    set(value) = PotionStringsFlavorField.flavor.set(this, value)

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

fun AbstractCard.addModifier(modifier: AbstractCardModifier) {
    CardModifierManager.addModifier(this, modifier)
}

/**
 * Add data to the metrics.
 */
fun Metrics.addData(key: String, value: Any) {
    val fnAddData = ReflectionHacks.privateMethod(
        Metrics::class.java, "addData", Any::class.java, Any::class.java)
    fnAddData.invoke<Void>(this, key, value)
}

