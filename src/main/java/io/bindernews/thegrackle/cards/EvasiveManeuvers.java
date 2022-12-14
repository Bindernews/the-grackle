package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;

public class EvasiveManeuvers extends BaseCard {
    public static final CardConfig C = new CardConfig("EvasiveManeuvers", CardType.SKILL);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.block(7, 10);
        c.magic(3, 5);
    });

    public EvasiveManeuvers() {
        super(C, CardRarity.UNCOMMON, CardTarget.SELF);
        VARS.init(this);
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ScryAction(magicNumber));
    }

    @Override
    public void upgrade() {
        VARS.upgrade(this);
    }
}
