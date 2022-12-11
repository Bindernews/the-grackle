package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.thegrackle.GrackleMod;

public class HealingPhoenixPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(HealingPhoenixPower.class);

    public HealingPhoenixPower(AbstractCreature owner, int heal) {
        super(POWER_ID);
        setOwnerAmount(owner, heal);
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        flashWithoutSound();
        addToTop(new HealAction(owner, owner, amount));
    }

    @Override
    public AbstractPower makeCopy() {
        return new HealingPhoenixPower(owner, amount);
    }
}
