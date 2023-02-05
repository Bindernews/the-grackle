package io.bindernews.thegrackle.downfall.stances;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

@SuppressWarnings("unused")
public interface EnemyStanceDelegate {
    void atStartOfTurn();
    void onEndOfTurn();
    void onEnterStance();
    void onExitStance();
    float atDamageGive(float damage, DamageInfo.DamageType type);
    float atDamageReceive(float damage, DamageInfo.DamageType damageType);
    void onPlayCard(AbstractCard card);
    void update();
    void updateAnimation();
    void render(SpriteBatch sb);
    void stopIdleSfx();

}
