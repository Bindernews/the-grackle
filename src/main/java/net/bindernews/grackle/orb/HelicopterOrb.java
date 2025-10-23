package net.bindernews.grackle.orb;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lombok.val;
import net.bindernews.grackle.GrackleMod;
import net.bindernews.grackle.helper.MiscUtil;

import java.util.Arrays;

public class HelicopterOrb extends BaseWeaponOrb {
    public static final String ID = GrackleMod.makeId(HelicopterOrb.class);
    private static final OrbHelper HELPER = new OrbHelper(ID);

    public HelicopterOrb() {
        super(HELPER, 10);
    }

    @Override
    public void onEndOfTurn() {
        // Deal 'passiveAmount' damage to all enemies then decrease by 2
        val damage = damageArray(passiveAmount);
        val attackFx = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;

        HELPER.flashOrbVfx(this, 0.6F);
        MiscUtil.addToBot(
                getIop().damageAllEnemies(AbstractDungeon.player, damage, DamageInfo.DamageType.NORMAL, attackFx));
        this.evokeAmount -= this.passiveAmount;
        this.updateDescription();
    }

    @Override
    public void onEvoke() {
        
    }

    /**
     * Returns an int array with one value for each monster in the current room.
     * @param amount value for each array element
     */
    private int[] damageArray(int amount) {
        val monsters = AbstractDungeon.getMonsters().monsters;
        val damage = new int[monsters.size()];
        Arrays.fill(damage, amount);
        return damage;
    }
}
