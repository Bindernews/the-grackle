package net.bindernews.bnsts.hook;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import net.bindernews.bnsts.api.FlippableCard;
import net.bindernews.bnsts.api.ScvChangeCardEvent;

@SuppressWarnings("unused")
public class AllPatches {

    @SpirePatch(clz = AbstractCard.class, method = "update")
    public static class CardRightClick {
        public static void Postfix(AbstractCard __instance) {
            if (__instance instanceof FlippableCard
                    && __instance.hb.hovered
                    && HitboxRightClick.rightClicked.get(__instance.hb)) {
                FlippableCard flipper = (FlippableCard) __instance;
                int mode = (flipper.getCardDirection() + 1) % 2;
                flipper.setCardDirection(mode);
            }
        }
    }

    @SpirePatch(clz = TipHelper.class, method = "render")
    public static class TipHelperOnRender {
        public static void Postfix(SpriteBatch sb) {
            BnStsMain.INSTANCE.publishPopupRender(sb);
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "open", paramtypez = {AbstractCard.class, CardGroup .class})
    public static class CatchScvOpen {
        public static void Postfix(SingleCardViewPopup __instance, AbstractCard card, CardGroup group) {
            BnStsMain.INSTANCE.getSingleCardViewChanged().emit(new ScvChangeCardEvent(card, group, true));
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "close")
    public static class CatchScvClose {
        public static void Prefix(SingleCardViewPopup __instance) {
            AbstractCard card = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "card");
            CardGroup group = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "group");
            BnStsMain.INSTANCE.getSingleCardViewChanged().emit(new ScvChangeCardEvent(card, group, false));
        }
    }
}
