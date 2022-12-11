package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.MiscUtil;
import io.bindernews.thegrackle.stance.StancePhoenix;

public class CoolingPhoenixPower extends BasePower {

    public static final String POWER_ID = GrackleMod.makeId(CoolingPhoenixPower.class);

    public CoolingPhoenixPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (StancePhoenix.is(newStance)) {
            addToTop(new ChangeStanceAction(oldStance));
        }
    }

    @Override
    public void atEndOfRound() {
        addToBot(MiscUtil.getLowerPowerAction(this, 1));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new CoolingPhoenixPower(owner, amount);
    }
}
