package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import io.bindernews.bnsts.CardNums;

public class TargetingComputer extends BaseCard {
    public static final CardConfig C = new CardConfig("TargetingComputer", CardType.POWER);
    public static final CardNums NUM = CardNums.builder()
            .cost(1).magic(4).magicUpg(8).build();

    public TargetingComputer() {
        super(C, CardRarity.COMMON, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
