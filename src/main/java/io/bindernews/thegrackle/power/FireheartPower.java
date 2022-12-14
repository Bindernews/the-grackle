package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.thegrackle.CreatureStats;
import io.bindernews.thegrackle.GrackleMod;

public class FireheartPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(FireheartPower.class);
    public static final int FIREHEART_REQUIRED = 10;

    public FireheartPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        isTurnBased = true;
        type = PowerType.BUFF;
        CreatureStats.mgr.get(owner).fireheartGained += amount;
        loadRegion("flameBarrier");
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= FIREHEART_REQUIRED) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new ApplyPowerAction(owner, owner, new PhoenixStancePower(owner)));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new FireheartPower(owner, amount);
    }
}
