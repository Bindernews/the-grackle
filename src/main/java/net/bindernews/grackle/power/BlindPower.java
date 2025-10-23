package net.bindernews.grackle.power;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BeforeRenderIntentPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import net.bindernews.grackle.GrackleMod;
import net.bindernews.grackle.helper.PowerUtil;

public class BlindPower extends BasePower implements BeforeRenderIntentPower {
    public static final String POWER_ID = GrackleMod.makeId(BlindPower.class);

    public BlindPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        setOwnerAmount(owner, amount);
        loadRegion("blind");
    }

    @Override
    public void atEndOfRound() {
        PowerUtil.endOfRoundReduceOrRemove(this, 1);
    }

    @Override
    public boolean beforeRenderIntent(AbstractMonster abstractMonster) {
        // Disable rendering intent
        return false;
    }
}
