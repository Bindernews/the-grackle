package io.bindernews.thegrackle.power;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.stance.StancePhoenix;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

/**
 * Implements most of the features of PhoenixStance.
 */
public class PhoenixStancePower extends BasePower implements InvisiblePower {
    public static final String POWER_ID = GrackleMod.makeId(PhoenixStancePower.class);

    public PhoenixStancePower(AbstractCreature owner) {
        super(POWER_ID);
        setOwnerAmount(owner, -1);
        type = PowerType.BUFF;
        isTurnBased = false;
        loadRegion("ai");
    }

    @Override
    public void onInitialApplication() {
        addToBot(iop().changeStance(owner, new StancePhoenix()));
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (!StancePhoenix.is(newStance)) {
            addToBot(iop().changeStance(owner, new StancePhoenix()));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new ApplyPowerAction(owner, owner, new CoolingPhoenixPower(owner, 1)));
            addToBot(iop().changeStance(owner, new NeutralStance()));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new PhoenixStancePower(owner);
    }
}
