package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.NeutralStance;
import io.bindernews.bnsts.CardVariables;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class Parachute extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Parachute", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.block(8, 11);
    });

    public Parachute() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID));
    }
}
