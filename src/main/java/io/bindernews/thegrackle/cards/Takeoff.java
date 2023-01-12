package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.stance.StanceAloft;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class Takeoff extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Takeoff", CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.block(6, 10);
    });

    public Takeoff() {
        super(C, VARS);
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(iop().changeStance(p, StanceAloft.STANCE_ID));
    }
}
