package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.stance.StanceAloft;

public class Takeoff extends BaseCard {
    public static final CardConfig CFG = new CardConfig("Takeoff", CardType.SKILL);
    public static final CardNums NUM = CardNums.builder().cost(1).block(6).blockUpg(10).build();

    public Takeoff() {
        super(CFG, CardRarity.BASIC, CardTarget.SELF);
        tags.add(CardTags.STARTER_DEFEND);
        NUM.init(this);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(iop().changeStance(p, StanceAloft.STANCE_ID));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
