package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.power.BurningPower;

public class FireTouch extends BaseCard {
    public static final CardConfig C =
            new CardConfig("FireTouch", CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1);
        c.magic(6, 10);
    });

    public FireTouch() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(m, p, new BurningPower(m, p, magicNumber), magicNumber));
    }
}
