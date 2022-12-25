package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.FireWithinPower;

public class FireWithin extends BaseCard {
    public static final CardConfig C = new CardConfig("FireWithin", CardType.POWER);
    public static final CardNums NUM = CardNums.builder().cost(1).magic(2).magicUpg(3).build();

    public FireWithin() {
        super(C, CardRarity.UNCOMMON, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new FireWithinPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
