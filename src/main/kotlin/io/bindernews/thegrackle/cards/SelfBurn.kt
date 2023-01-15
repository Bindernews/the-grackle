package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import lombok.val;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class SelfBurn extends BaseCard {
    public static final CardConfig C =
            new CardConfig("SelfBurn", CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(0);
        c.magic(1, 2);
    });

    static final int BURN_COUNT = 1;

    /**
     * If true, draws cards immediately, otherwise applies a power to draw extra cards next turn.
     * This is intended to be used by boss characters.
     */
    public boolean drawNow = true;

    public SelfBurn() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        if (drawNow) {
            addToBot(new DrawCardAction(p, magicNumber));
        } else {
            val power = iop().createPower("Draw Card", p, magicNumber);
            addToBot(new ApplyPowerAction(p, p, power, magicNumber));
        }
        addToBot(iop().actionMakeTempCardInDiscard(p, new Burn(), BURN_COUNT));
    }
}
