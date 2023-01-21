package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import io.bindernews.bnsts.CardVariables;

public class Cackle extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Cackle", CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(0, -1);
        c.magic(1, 2);
    });

    public Cackle() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
    }
}
