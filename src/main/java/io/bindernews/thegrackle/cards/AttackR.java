package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;

public class AttackR extends BaseCard {
    public static final CardConfig C = new CardConfig("AttackR", CardType.ATTACK);
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .damage(8).damageUpg(16)
            .build();

    public AttackR() {
        super(C, CardRarity.RARE, CardTarget.ENEMY);
        NUM.init(this);
        damageType = damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageType)));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
