@file:Suppress("UNUSED_PARAMETER", "FunctionName", "unused")

package net.bindernews.grackle.patches

import basemod.ReflectionHacks.getPrivate
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.screens.SingleCardViewPopup
import net.bindernews.grackle.Events.scvCardChange
import net.bindernews.grackle.api.ScvChangeCardEvent


class SingleCardViewPopupPatches {
    @SpirePatch(clz = SingleCardViewPopup::class, method = "open", paramtypez = [AbstractCard::class, CardGroup::class])
    object CatchOpen {
        @JvmStatic fun Postfix(__instance: SingleCardViewPopup, card: AbstractCard, group: CardGroup) {
            scvCardChange.emit(ScvChangeCardEvent(card, group, true))
        }
    }

    @SpirePatch(clz = SingleCardViewPopup::class, method = "close")
    object CatchClose {
        @JvmStatic fun Prefix(__instance: SingleCardViewPopup) {
            val card = getPrivate<AbstractCard>(__instance, __instance.javaClass, "card")
            val group = getPrivate<CardGroup>(__instance, __instance.javaClass, "group")
            scvCardChange.emit(ScvChangeCardEvent(card, group, false))
        }
    }
}