package io.bindernews.thegrackle.power;

import io.bindernews.thegrackle.CreatureStats;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.MiscUtil;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FireheartPower extends AbstractPower {
    public static final String POWER_ID = GrackleMod.makeId(FireheartPower.class);
    public static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final int FIREHEART_REQUIRED = 10;

    public FireheartPower(AbstractCreature owner, int amount) {
        MiscUtil.powerInit(this, owner, amount, POWER_ID, STRINGS);
        isTurnBased = true;
        type = PowerType.BUFF;
        CreatureStats.mgr.get(owner).fireheartGained += amount;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= FIREHEART_REQUIRED) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new ApplyPowerAction(owner, owner, new PhoenixStancePower(owner)));
        }
    }
}
