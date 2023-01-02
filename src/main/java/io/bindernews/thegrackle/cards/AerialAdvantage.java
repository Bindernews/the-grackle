package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.stance.StanceAloft;

public class AerialAdvantage extends BaseCard {
    public static final CardConfig C = new CardConfig("AerialAdvantage", CardType.SKILL);
    public static final CardNums NUM = CardNums.builder()
            .cost(0)
            .magic(2).magicUpg(3)
            .build();
    public static int CARDS_TO_DRAW = 4;

    public AerialAdvantage() {
        super(C, CardRarity.UNCOMMON, CardTarget.SELF);
        rawDescription = String.format(rawDescription, CARDS_TO_DRAW);
        exhaust = true;
        NUM.init(this);
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        // N.B Not applicable for charboss
        addToBot(iop().changeStance(p, StanceAloft.STANCE_ID));
        addToBot(iop().actionGainEnergy(p, magicNumber));
        addToBot(new DrawCardAction(p, CARDS_TO_DRAW));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
