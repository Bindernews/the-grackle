package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.stance.StanceAloft;
import io.bindernews.thegrackle.variables.Magic2Var;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class AerialAdvantage extends BaseCard {
    public static final CardConfig C =
            new CardConfig("AerialAdvantage", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(0);
        c.magic(2, 3);
        c.add(Magic2Var.inst, 4, -1);
        c.exhaust(true, true);
    });

    public AerialAdvantage() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        // N.B Not applicable for charboss
        addToBot(iop().changeStance(p, StanceAloft.STANCE_ID));
        addToBot(iop().actionGainEnergy(p, magicNumber));
        addToBot(new DrawCardAction(p, Magic2Var.inst.value(this)));
    }
}
