package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;

@AutoAdd.Seen
public class Defend_GK extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Defend_GK", CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.block(5, 8);
    });

    public Defend_GK() {
        super(C, VARS);
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new GainBlockAction(p, p, block));
    }
}
