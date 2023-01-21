package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.power.MultiHitPower;
import org.jetbrains.annotations.NotNull;

public class BufferInputs extends BaseCard {
    public static final CardConfig C =
            new CardConfig("BufferInputs", CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1);
        c.magic(2, 4);
    });

    public BufferInputs() {
        super(C, VARS);
    }

    @Override
    public void apply(@NotNull AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new MultiHitPower(p, magicNumber), magicNumber));
    }
}
