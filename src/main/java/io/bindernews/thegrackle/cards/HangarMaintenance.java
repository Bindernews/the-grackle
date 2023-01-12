package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.RegenPower;
import io.bindernews.bnsts.CardVariables;
import lombok.val;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class HangarMaintenance extends BaseCard {
    public static final CardConfig C =
            new CardConfig("HangarMaintenance", CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(2, -1);
        c.magic(5, 8);
    });

    public HangarMaintenance() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val power = iop().createPower(RegenPower.POWER_ID, p, magicNumber);
        addToBot(new ApplyPowerAction(p, p, power, magicNumber));
    }
}
