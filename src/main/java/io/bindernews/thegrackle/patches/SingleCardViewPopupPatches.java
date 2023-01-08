package io.bindernews.thegrackle.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import io.bindernews.bnsts.EventEmit;
import lombok.*;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class SingleCardViewPopupPatches {

    @Data
    public static class ChangeEvent {
        private final AbstractCard card;
        private final CardGroup group;
        private final boolean open;
    }

    @Getter
    private static final EventEmit<ChangeEvent> onCardChange = new EventEmit<>();

    @SpirePatch(clz = SingleCardViewPopup.class, method = "open", paramtypez = {AbstractCard.class, CardGroup.class})
    public static class CatchOpen {
        public static void Postfix(SingleCardViewPopup __instance, AbstractCard card, CardGroup group) {
            getOnCardChange().emit(new ChangeEvent(card, group, true));
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "close")
    public static class CatchClose {
        public static void Prefix(SingleCardViewPopup __instance) {
            //noinspection UnnecessaryLocalVariable
            val scv = __instance;
            val card = (AbstractCard) ReflectionHacks.getPrivate(scv, scv.getClass(), "card");
            val group = (CardGroup) ReflectionHacks.getPrivate(scv, scv.getClass(), "group");
            getOnCardChange().emit(new ChangeEvent(card, group, false));
        }
    }

    public static AbstractCard getCard(SingleCardViewPopup inst) {
        try {
            Field fld = ReflectionHacks.getCachedField(SingleCardViewPopup.class, "card");
            return (AbstractCard)fld.get(inst);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
