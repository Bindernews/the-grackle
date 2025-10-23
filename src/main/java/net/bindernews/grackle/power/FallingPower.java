package net.bindernews.grackle.power;

import com.megacrit.cardcrawl.core.AbstractCreature;
import net.bindernews.grackle.GrackleMod;
import net.bindernews.grackle.helper.PowerUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Applied to enemies to make them count as "falling". Falling enemies take more damage from certain cards.
 */
public class FallingPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(FallingPower.class);

    public FallingPower(@NotNull AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        isTurnBased = true;
        type = PowerType.DEBUFF;
        loadRegion("falling");
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        PowerUtil.endOfRoundReduceOrRemove(this, 1);
    }
}
