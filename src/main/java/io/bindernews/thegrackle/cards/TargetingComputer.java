package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.TargetingComputerPower;

public class TargetingComputer extends BaseCard {
    public static final CardConfig CFG = new CardConfig("TargetingComputer", CardType.POWER);
    public static final CardNums NUM = CardNums.builder()
            .cost(1).magic(6).magicUpg(10).build();

    public TargetingComputer() {
        super(CFG, CardRarity.COMMON, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new TargetingComputerPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
