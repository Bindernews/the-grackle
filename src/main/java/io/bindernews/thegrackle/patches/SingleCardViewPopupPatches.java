package io.bindernews.thegrackle.patches;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import java.lang.reflect.Field;

public class SingleCardViewPopupPatches {

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
