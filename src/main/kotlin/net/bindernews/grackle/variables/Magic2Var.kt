package net.bindernews.grackle.variables

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard
import net.bindernews.grackle.GrackleMod.Companion.MOD_ID

class Magic2Var private constructor() : AbstractSimpleVariable(Fields.grackleMagic2, VariableInst()) {
    override fun key(): String {
        return KEY
    }

    @SpirePatch(clz = AbstractCard::class, method = SpirePatch.CLASS)
    object Fields {
        @JvmField var grackleMagic2 = SpireField<VariableInst?> { null }
    }

    companion object {
        const val KEY = "$MOD_ID:magic2"
        @JvmField
        val inst = Magic2Var()
    }
}