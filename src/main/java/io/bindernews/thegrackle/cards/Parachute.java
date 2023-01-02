package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.NeutralStance;
import io.bindernews.bnsts.CardNums;

public class Parachute extends BaseCard {
    public static final CardConfig C = new CardConfig("Parachute", CardType.SKILL);
    static final CardNums NUM = CardNums.builder()
            .cost(1)
            .block(8).blockUpg(11)
            .build();

    public Parachute() {
        super(C, CardRarity.UNCOMMON, CardTarget.SELF);
        NUM.init(this);
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
