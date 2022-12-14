package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import io.bindernews.thegrackle.GrackleMod;
import lombok.val;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

public class BurningPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(BurningPower.class);
    /** How much this is reduced by each turn. */
    static int REDUCE_PER_TURN = 2;


    public AbstractCreature source;

    public BurningPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        isTurnBased = true;
        type = PowerType.DEBUFF;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }
        updateDescription();
        loadRegion("flameBarrier");
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flashWithoutSound();
            // Damage the owner
            applyDamage(owner, amount);
            // If the owner is a monster, damage all monsters
            if (!(owner instanceof AbstractPlayer)) {
                int aoeDamage = amount / 2;
                for (val monster : AbstractDungeon.getMonsters().monsters) {
                    if (monster != owner) {
                        applyDamage(monster, aoeDamage);
                    }
                }
            }
            // Reduce power
            addToBot(new ReducePowerAction(owner, owner, POWER_ID, REDUCE_PER_TURN));
        }
    }

    private void applyDamage(AbstractCreature target, int dmg) {
//        addToBot(new LoseHPAction(target, source, dmg, AttackEffect.FIRE));
        val info = new DamageInfo(source, dmg, DamageInfo.DamageType.THORNS);
        addToBot(new DamageAction(target, info, AttackEffect.FIRE));
    }

    @Override
    public AbstractPower makeCopy() {
        return new BurningPower(owner, source, amount);
    }
}
