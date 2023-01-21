package io.bindernews.thegrackle.downfall.relics;

import charbosses.actions.common.EnemyMakeTempCardInHandAction;
import charbosses.relics.AbstractCharbossRelic;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import io.bindernews.thegrackle.downfall.DfUtil;
import io.bindernews.thegrackle.relics.SimmeringHeat;

public class CBR_SimmeringHeat extends AbstractCharbossRelic {

    public CBR_SimmeringHeat() {
        super(new SimmeringHeat());
        DfUtil.setImagesFromBase(this);
    }

    @Override
    public void atTurnStartPostDraw() {
        addToBot(new EnemyMakeTempCardInHandAction(new Burn()));
    }

    @Override
    public void onEquip() {
        owner.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        owner.energy.energyMaster--;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CBR_SimmeringHeat();
    }
}
