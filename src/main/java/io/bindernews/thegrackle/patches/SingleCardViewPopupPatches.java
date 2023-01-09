package io.bindernews.thegrackle.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import io.bindernews.thegrackle.Events;
import io.bindernews.thegrackle.interfaces.SvcChangeCardEvent;
import lombok.val;

@SuppressWarnings("unused")
public class SingleCardViewPopupPatches {

    @SpirePatch(clz = SingleCardViewPopup.class, method = "open", paramtypez = {AbstractCard.class, CardGroup.class})
    public static class CatchOpen {
        public static void Postfix(SingleCardViewPopup __instance, AbstractCard card, CardGroup group) {
            Events.svcCardChange().emit(new SvcChangeCardEvent(card, group, true));
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "close")
    public static class CatchClose {
        public static void Prefix(SingleCardViewPopup __instance) {
            //noinspection UnnecessaryLocalVariable
            val scv = __instance;
            val card = (AbstractCard) ReflectionHacks.getPrivate(scv, scv.getClass(), "card");
            val group = (CardGroup) ReflectionHacks.getPrivate(scv, scv.getClass(), "group");
            Events.svcCardChange().emit(new SvcChangeCardEvent(card, group, false));
        }
    }
}
