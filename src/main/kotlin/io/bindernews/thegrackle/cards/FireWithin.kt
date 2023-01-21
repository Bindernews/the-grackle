package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.power.FireWithinPower;

public class FireWithin extends BaseCard {
    public static final CardConfig C =
            new CardConfig("FireWithin", CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1);
        c.magic(2, 3);
    });

    public FireWithin() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new FireWithinPower(p, magicNumber)));
    }
}
