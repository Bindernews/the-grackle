package net.bindernews.grackle.helper;

import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface CardUtil {

    static boolean isRightClicked(AbstractCard card) {
        return card.hb.hovered && HitboxRightClick.rightClicked.get(card.hb);
    }
}
