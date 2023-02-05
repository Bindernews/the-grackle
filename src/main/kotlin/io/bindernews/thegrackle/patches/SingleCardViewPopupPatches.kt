@file:Suppress("UNUSED_PARAMETER", "FunctionName", "unused")

package io.bindernews.thegrackle.patches

import basemod.ReflectionHacks.getPrivate
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.screens.SingleCardViewPopup
import io.bindernews.thegrackle.Events.svcCardChange
import io.bindernews.thegrackle.api.SvcChangeCardEvent


class SingleCardViewPopupPatches {
    @SpirePatch(clz = SingleCardViewPopup::class, method = "open", paramtypez = [AbstractCard::class, CardGroup::class])
    object CatchOpen {
        @JvmStatic fun Postfix(__instance: SingleCardViewPopup, card: AbstractCard, group: CardGroup) {
            svcCardChange.emit(SvcChangeCardEvent(card, group, true))
        }
    }

    @SpirePatch(clz = SingleCardViewPopup::class, method = "close")
    object CatchClose {
        @JvmStatic fun Prefix(__instance: SingleCardViewPopup) {
            val card = getPrivate<AbstractCard>(__instance, __instance.javaClass, "card")
            val group = getPrivate<CardGroup>(__instance, __instance.javaClass, "group")
            svcCardChange.emit(SvcChangeCardEvent(card, group, false))
        }
    }
}