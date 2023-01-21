package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.vfx.UpgradeRandomCardEffect;

public class ResearchAndDevPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(ResearchAndDevPower.class);

    public ResearchAndDevPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        loadRegion("ai");
        updateDescription();
    }


    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void onVictory() {
        for (int i = 0; i < amount; i++) {
            AbstractDungeon.effectsQueue.add(new UpgradeRandomCardEffect(owner));
        }
    }

    @Override
    public void updateDescription() {
        description = formatDesc(0);
    }
}
