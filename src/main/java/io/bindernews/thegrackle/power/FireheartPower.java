package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.thegrackle.CreatureStats;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.stance.StancePhoenix;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class FireheartPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(FireheartPower.class);
    public static int fireheartRequired = 10;

    public FireheartPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        isTurnBased = true;
        type = PowerType.BUFF;
        CreatureStats.mgr.get(owner).fireheartGained += amount;
        loadRegion("flameBarrier");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        // Yes, this means you can build fireheart even while in stance.
        if (amount >= fireheartRequired && canEnterPhoenixStance()) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(iop().changeStance(owner, StancePhoenix.STANCE_ID));
        }
    }

    public boolean canEnterPhoenixStance() {
        return !StancePhoenix.is(iop().getStance(owner)) && !owner.hasPower(CoolingPhoenixPower.POWER_ID);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FireheartPower(owner, amount);
    }
}
