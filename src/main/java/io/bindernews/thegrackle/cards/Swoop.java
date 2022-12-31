package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.NeutralStance;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.SwoopPower;

public class Swoop extends BaseCard {
    public static final CardConfig C = new CardConfig("Swoop", CardType.SKILL);
    static final CardNums NUM = CardNums.builder()
            .cost(2).costUpg(1)
            .build();

    public Swoop() {
        super(C, CardRarity.UNCOMMON, CardTarget.SELF);
        NUM.init(this);
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID));
        addToBot(new ApplyPowerAction(p, p, new SwoopPower(p)));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
