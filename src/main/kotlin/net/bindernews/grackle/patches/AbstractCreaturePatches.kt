package net.bindernews.grackle.patches

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.core.AbstractCreature

@SpirePatch(clz =  AbstractCreature::class, method = "<class>")
object Fields {
    @JvmField var fireheartGained = SpireField { 0 }
}

