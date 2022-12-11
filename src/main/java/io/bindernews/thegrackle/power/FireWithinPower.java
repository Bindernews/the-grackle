package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.thegrackle.GrackleMod;

public class FireWithinPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(FireWithinPower.class);

    public FireWithinPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new ApplyPowerAction(owner, owner, new FireheartPower(owner, amount), amount, true));
    }

    @Override
    public void updateDescription() {
        description = formatDesc(0, amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FireWithinPower(owner, amount);
    }
}
