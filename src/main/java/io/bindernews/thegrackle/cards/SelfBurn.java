package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import lombok.val;

public class SelfBurn extends BaseCard {
    public static final CardConfig C = new CardConfig("SelfBurn", CardType.SKILL);
    public static final CardNums NUM = CardNums.builder()
            .cost(0).magic(1).magicUpg(2).build();
    static final int BURN_COUNT = 1;

    /**
     * If true, draws cards immediately, otherwise applies a power to draw extra cards next turn.
     * This is intended to be used by boss characters.
     */
    public boolean drawNow = true;

    public SelfBurn() {
        super(C, CardRarity.COMMON, CardTarget.NONE);
        NUM.init(this);
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

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
