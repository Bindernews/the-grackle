package io.bindernews.thegrackle.power;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.stance.StanceAloft;

public class AloftPower extends BasePower implements InvisiblePower {
    public static final String POWER_ID = GrackleMod.makeId(AloftPower.class);

    public AloftPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, -1);
        type = PowerType.BUFF;
        loadRegion("ai");
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (!StanceAloft.isAloft(newStance)) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new AloftPower(owner, amount);
    }
}
