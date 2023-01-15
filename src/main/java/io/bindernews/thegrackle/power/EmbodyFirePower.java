package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.thegrackle.GrackleMod;

public class EmbodyFirePower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(EmbodyFirePower.class);

    public EmbodyFirePower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        loadRegion("flameBarrier");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = formatDesc(0);
    }
}
