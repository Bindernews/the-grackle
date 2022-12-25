package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.BurningPower;

public class FireTouch extends BaseCard {
    public static final CardConfig C = new CardConfig("FireTouch", CardType.SKILL);
    public static final CardNums NUM = CardNums.builder()
            .cost(1).magic(6).magicUpg(10).build();

    public FireTouch() {
        super(C, CardRarity.COMMON, CardTarget.ENEMY);
        NUM.init(this);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(m, p, new BurningPower(m, p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
