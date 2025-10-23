package net.bindernews.grackle.orb;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.LightningOrbEvokeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lombok.val;
import net.bindernews.grackle.GrackleMod;
import net.bindernews.grackle.helper.MiscUtil;

/**
 * Bombs grow in power each turn like a Dark Orb, but hit ALL enemies when evoked.
 */
public class BombOrb extends BaseWeaponOrb {
    public static final String ID = GrackleMod.makeId(BombOrb.class);
    private static final OrbHelper HELPER = new OrbHelper(ID);

    public BombOrb() {
        super(HELPER, 5);
    }

    @Override
    public void onEndOfTurn() {
        HELPER.simpleEndOfTurn(this);
    }

    @Override
    public void onEvoke() {
        val source = AbstractDungeon.player;
        val damageMat = DamageInfo.createDamageMatrix(evokeAmount);
        MiscUtil.addToBot(new DamageAllEnemiesAction(
                source, damageMat, DamageInfo.DamageType.NORMAL, AttackEffect.FIRE));
    }

    @Override
    public void applyFocus() {
        AbstractPower power = AbstractDungeon.player.getPower("Focus");
        if (power != null) {
            this.passiveAmount = Math.max(0, this.basePassiveAmount + power.amount);
        } else {
            this.passiveAmount = this.basePassiveAmount;
        }
    }
}
