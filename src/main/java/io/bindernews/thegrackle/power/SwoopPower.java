package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.stance.StanceAloft;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class SwoopPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(SwoopPower.class);

    public SwoopPower(AbstractCreature owner) {
        super(POWER_ID);
        setOwnerAmount(owner, -1);
        type = PowerType.BUFF;
        isTurnBased = true;
        loadRegion("anger");
        updateDescription();
    }

    public SwoopPower(AbstractCreature owner, int ignoredAmount) {
        this(owner);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(iop().changeStance(owner, StanceAloft.STANCE_ID));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new SwoopPower(owner);
    }
}
