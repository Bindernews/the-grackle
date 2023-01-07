package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;

public class PeckingOrderPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(PeckingOrderPower.class);

    static {
        ExtraHitsVariable.getOnApplyPowers().on(-5, ExtraHitsVariable.addPowerAmount(POWER_ID));
    }

    public PeckingOrderPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        isTurnBased = true;
        loadRegion("ai");
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }

    @Override
    public void updateDescription() {
        description = formatDesc(0, amount);
    }
}
