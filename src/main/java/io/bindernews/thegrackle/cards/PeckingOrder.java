package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.PeckingOrderPower;

public class PeckingOrder extends BaseCard {
    public static final CardConfig C = new CardConfig("PeckingOrder", CardType.SKILL);
    static final CardNums NUM = CardNums.builder()
            .cost(1).magic(4).magicUpg(6).build();

    public PeckingOrder() {
        super(C, CardRarity.UNCOMMON, CardTarget.SELF);
        NUM.init(this);
        exhaust = true;
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new PeckingOrderPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
