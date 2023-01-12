package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.EvolvePower;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.power.BurnCreamPower;
import lombok.val;

public class BurnCream extends BaseCard {
    public static final CardConfig C =
            new CardConfig("BurnCream", CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> c.cost(1, -1));

    public BurnCream() {
        super(C, VARS);
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
}
