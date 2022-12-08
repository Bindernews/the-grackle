package io.bindernews.thegrackle.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import io.bindernews.thegrackle.cards.BaseCard;

import java.lang.reflect.Field;

@SpirePatch(clz = SingleCardViewPopup.class, method = "renderTips")
public class SingleCardViewPopupPatches {

    public static void Postfix(SingleCardViewPopup __instance, SpriteBatch sb) {
        AbstractCard card = getCard(__instance);
        if (card instanceof BaseCard) {
            renderFlavorText(__instance, sb, (BaseCard) card);
        }
    }

    public static void renderFlavorText(SingleCardViewPopup inst, SpriteBatch sb, BaseCard card) {
        if (card.flavorText.isEmpty()) {
            return;
        }
        // TODO render the flavor text
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
