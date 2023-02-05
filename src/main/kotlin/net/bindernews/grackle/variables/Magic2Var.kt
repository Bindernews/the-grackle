package net.bindernews.grackle.variables

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard
import net.bindernews.grackle.GrackleMod

class Magic2Var private constructor() : AbstractSimpleVariable(Fields.magic2, VariableInst()) {
    override fun key(): String {
        return KEY
    }

    @SpirePatch(clz = AbstractCard::class, method = SpirePatch.CLASS)
    object Fields {
        @JvmField var magic2 = SpireField<VariableInst?> { null }
    }

    companion object {
        val KEY = GrackleMod.makeId("magic2")
        @JvmField
        val inst = Magic2Var()
    }
}