package io.bindernews.thegrackle.power;

import io.bindernews.thegrackle.MiscUtil;
import io.bindernews.thegrackle.stance.StancePhoenix;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;

import static io.bindernews.thegrackle.Const.GK_ID;

public class CoolingPhoenixPower extends AbstractPower {

    public static final String POWER_ID = GK_ID + ":CoolingPhoenixPower";
    public static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public CoolingPhoenixPower(AbstractCreature owner, int amount) {
        MiscUtil.powerInit(this, owner, amount, POWER_ID, STRINGS);
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        this.updateDescription();
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (StancePhoenix.is(newStance)) {
            addToTop(new ChangeStanceAction(oldStance));
        }
    }

    @Override
    public void atEndOfRound() {
        addToBot(MiscUtil.getLowerPowerAction(this, 1));
    }


}
