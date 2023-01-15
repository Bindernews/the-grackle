package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.power.MultiHitPower;
import org.jetbrains.annotations.NotNull;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class BePrepared extends BaseCard {
    public static final CardConfig C =
            new CardConfig("BePrepared", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, 0);
        c.magic(1, -1);
    });

    public BePrepared() {
        super(C, VARS);
    }

    @Override
    public void apply(@NotNull AbstractCreature p, AbstractCreature m) {
        addToBot(iop().actionApplyPower(p, p, MultiHitPower.POWER_ID, magicNumber));
    }
}
