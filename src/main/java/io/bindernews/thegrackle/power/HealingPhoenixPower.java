package io.bindernews.thegrackle.power;

import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.MiscUtil;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HealingPhoenixPower extends AbstractPower {
    public static final String POWER_ID = GrackleMod.makeId(HealingPhoenixPower.class);
    public static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public HealingPhoenixPower(AbstractCreature owner, int heal) {
        MiscUtil.powerInit(this, owner, heal, POWER_ID, STRINGS);
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        flashWithoutSound();
        addToTop(new HealAction(owner, owner, amount));
    }
}
