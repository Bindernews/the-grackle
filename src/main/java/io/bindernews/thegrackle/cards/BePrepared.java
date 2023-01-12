package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.power.MultiHitPower;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class BePrepared extends BaseCard {
    public static final CardConfig C =
            new CardConfig("BePrepared", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(0, -1);
        c.magic(1, 2);
    });

    public BePrepared() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p,
                iop().createPower(MultiHitPower.POWER_ID, p, magicNumber), magicNumber));
    }
}
