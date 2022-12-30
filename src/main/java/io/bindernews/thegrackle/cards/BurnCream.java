package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.EvolvePower;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.BurnCreamPower;
import lombok.val;

public class BurnCream extends BaseCard {
    public static final CardConfig C = new CardConfig("BurnCream", CardType.POWER);
    public static final CardNums NUM = CardNums.builder()
            .cost(1).build();

    public BurnCream() {
        super(C, CardRarity.UNCOMMON, CardTarget.SELF);
        NUM.init(this);
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new BurnCreamPower(p, -1), -1));
        if (upgraded) {
            addToBot(new ApplyPowerAction(p, p, new EvolvePower(p, 1)));
        }
    }

    @Override
    public void initializeDescription() {
        val st = C.getStrings();
        rawDescription = st.DESCRIPTION;
        if (upgraded) {
            rawDescription += st.EXTENDED_DESCRIPTION[0];
        }
        super.initializeDescription();
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
