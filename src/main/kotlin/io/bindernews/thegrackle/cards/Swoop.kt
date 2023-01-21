package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.NeutralStance;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.power.SwoopPower;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class Swoop extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Swoop", CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> c.cost(2, 1));

    public Swoop() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID));
        addToBot(new ApplyPowerAction(p, p, new SwoopPower(p)));
    }
}
