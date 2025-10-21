package net.bindernews.grackle.power;

import com.megacrit.cardcrawl.core.AbstractCreature;
import net.bindernews.grackle.GrackleMod;
import org.jetbrains.annotations.NotNull;

public class FallingPower extends BasePower {
    public static final String ID = GrackleMod.makeId(FallingPower.class);

    public FallingPower(@NotNull AbstractCreature owner, int amount) {
        super(ID);
        setOwnerAmount(owner, amount);
        isTurnBased = true;
        type = PowerType.DEBUFF;
        loadRegion("falling");
        updateDescription();
    }
}
