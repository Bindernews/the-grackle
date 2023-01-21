package io.bindernews.thegrackle.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.bindernews.thegrackle.GrackleMod;

public class SimmeringHeat extends BaseRelic {
    public static final String RELIC_ID = GrackleMod.makeId(SimmeringHeat.class);

    public SimmeringHeat() {
        super(RELIC_ID, RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public void atTurnStartPostDraw() {
        addToBot(new MakeTempCardInHandAction(new Burn()));
    }

    @Override
    public void onEquip() {
        getEnergyManager().energyMaster++;
    }

    @Override
    public void onUnequip() {
        getEnergyManager().energyMaster--;
    }

    protected EnergyManager getEnergyManager() {
        return AbstractDungeon.player.energy;
    }
}
