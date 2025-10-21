package net.bindernews.grackle.orb;

import com.megacrit.cardcrawl.actions.defect.LightningOrbEvokeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lombok.val;
import net.bindernews.grackle.GrackleMod;

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
        val dmgInfo = new DamageInfo(AbstractDungeon.player, this.evokeAmount, DamageInfo.DamageType.THORNS);
//        new DarkOrbEvokeAction(, AbstractGameAction.AttackEffect.FIRE)
        AbstractDungeon.actionManager.addToTop(new LightningOrbEvokeAction(dmgInfo, true));
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
