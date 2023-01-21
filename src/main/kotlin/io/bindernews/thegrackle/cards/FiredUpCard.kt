package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.power.FiredUpPower;

public class FiredUpCard extends BaseCard {
    public static final CardConfig C =
            new CardConfig("FiredUpCard", CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(2, 1);
        c.magic(8, -1);
    });

    public FiredUpCard() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new FiredUpPower(p, magicNumber)));
    }
}
