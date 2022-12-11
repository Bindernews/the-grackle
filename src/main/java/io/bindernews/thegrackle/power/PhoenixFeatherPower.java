package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.RepairPower;
import io.bindernews.thegrackle.GrackleMod;

public class PhoenixFeatherPower extends RepairPower {
    public static final String POWER_ID = GrackleMod.makeId(PhoenixFeatherPower.class);

    public PhoenixFeatherPower(AbstractCreature owner, int amount) {
        super(owner, amount);
        ID = POWER_ID;
    }
}
