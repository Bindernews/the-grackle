package net.bindernews.grackle.orb;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lombok.val;
import net.bindernews.grackle.GrackleMod;
import net.bindernews.grackle.actions.RemoveSpecificOrbAction;
import net.bindernews.grackle.helper.MiscUtil;
import net.bindernews.grackle.power.MultiHitPower;

public class SupportFireOrb extends BaseWeaponOrb {
    public static final String ID = GrackleMod.makeId(SupportFireOrb.class);
    private static final OrbHelper HELPER = new OrbHelper(ID);

    public SupportFireOrb() {
        super(HELPER, 3);
        baseEvokeAmount = 4;
        evokeAmount = baseEvokeAmount;
    }

    @Override
    public void onEvoke() {
        val owner = getOwner();
        MiscUtil.addToBot(getIop().actionApplyPower(owner, owner, MultiHitPower.POWER_ID, evokeAmount));
    }

    @Override
    public void onStartOfTurn() {
        val owner = getOwner();
        MiscUtil.addToBot(getIop().actionApplyPower(owner, owner, MultiHitPower.POWER_ID, passiveAmount));
        passiveAmount -= 1;
        if (passiveAmount <= 0) {
            MiscUtil.addToBot(new RemoveSpecificOrbAction(this));
        }
    }

    public AbstractCreature getOwner() {
        return AbstractDungeon.player;
    }
}
