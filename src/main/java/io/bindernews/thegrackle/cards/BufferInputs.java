package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.MultiHitPower;

public class BufferInputs extends BaseCard {
    public static final CardConfig CFG = new CardConfig("BufferInputs", CardType.SKILL);
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .magic(3).magicUpg(6)
            .build();

    public BufferInputs() {
        super(CFG, CardRarity.RARE, CardTarget.SELF);
        NUM.init(this);
        exhaust = true;
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new MultiHitPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
