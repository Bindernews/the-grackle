package io.bindernews.thegrackle.helper

import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.cards.BaseCard
import io.bindernews.thegrackle.variables.ExtraHitsVariable
import io.bindernews.thegrackle.variables.Magic2Var

fun CardVariables.hits(base: Int, upg: Int) {
    add(ExtraHitsVariable.inst, base, upg)
}
fun CardVariables.magic2(base: Int, upg: Int) {
    add(Magic2Var.inst, base, upg)
}

val BaseCard.extraHits: Int
    get() = ExtraHitsVariable.inst.value(this)
val BaseCard.magic2: Int
    get() = Magic2Var.inst.value(this)
