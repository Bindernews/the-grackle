package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import io.bindernews.thegrackle.GrackleMod;

public class TargetingComputerPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(TargetingComputerPower.class);

    public TargetingComputerPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        loadRegion("lockon");
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new ApplyPowerAction(owner, owner, new VigorPower(owner, amount)));
    }

    @Override
    public void updateDescription() {
        description = formatDesc(0, amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new TargetingComputerPower(owner, amount);
    }
}
