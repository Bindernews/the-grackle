package io.bindernews.thegrackle.power;

import io.bindernews.thegrackle.GrackleMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.RepairPower;

public class PhoenixFeatherPower extends RepairPower {
    public static final String POWER_ID = GrackleMod.makeId(PhoenixFeatherPower.class);
    public static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public PhoenixFeatherPower(AbstractCreature owner, int amount) {
        super(owner, amount);
        ID = POWER_ID;
    }
}
