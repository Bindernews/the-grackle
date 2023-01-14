package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.thegrackle.GrackleMod;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class PlaguePower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(PlaguePower.class);

    public PlaguePower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        loadRegion("ai");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        addToBot(iop().actionApplyPower(owner, owner, MultiHitPower.POWER_ID, amount));
    }

    @Override
    public void updateDescription() {
        description = formatDesc(0, amount);
    }
}
