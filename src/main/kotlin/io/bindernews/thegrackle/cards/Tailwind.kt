package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DrawPower;
import io.bindernews.bnsts.CardVariables;
import lombok.val;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class Tailwind extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Tailwind", CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.magic(1, -1);
        c.onUpgrade(card -> card.isInnate = true);
    });

    public Tailwind() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val power = iop().createPower(DrawPower.POWER_ID, p, magicNumber);
        addToBot(new ApplyPowerAction(p, p, power, magicNumber));
    }
}
