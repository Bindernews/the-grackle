package net.bindernews.bnsts.api;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import net.bindernews.bnsts.vfx.FlipCardEffect;

public interface FlippableCard {
    static int FRONT = 0;
    static int BACK = 1;

    int getCardDirection();
    void setCardDirection(int mode);

    default void renderFront(SpriteBatch sb) {
        FlipCardEffect.renderCardFront((AbstractCard) this, sb);
    }

    default void renderBack(SpriteBatch sb) {
        FlipCardEffect.renderCardBack((AbstractCard) this, sb);
    }
}
