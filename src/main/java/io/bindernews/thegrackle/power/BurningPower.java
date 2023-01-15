package io.bindernews.thegrackle.power;

import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.cardmods.EmbodyFireMod;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

public class BurningPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(BurningPower.class);

    /** How much this is reduced by each turn. */
    static int REDUCE_PER_TURN = 2;

    @NotNull
    public final DamageModContainer damageMods;

    public AbstractCreature source;

    @SuppressWarnings("unused")
    public BurningPower(AbstractCreature owner, int amount) {
        this(owner, null, amount);
    }

    public BurningPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        this.source = source;
        damageMods = new DamageModContainer(this, new EmbodyFireMod());
        isTurnBased = true;
        type = PowerType.DEBUFF;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }
        updateDescription();
        loadRegion("flameBarrier");
    }

    public static AbstractGameAction makeAction(
            AbstractCreature source, AbstractCreature target, int amount
    ) {
        return new ApplyPowerAction(target, source, new BurningPower(target, source, amount), amount);
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            if (!(owner instanceof AbstractPlayer)) {
                dealGroupDamage();
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (owner instanceof AbstractPlayer && isPlayer) {
            dealGroupDamage();
        }
    }

    private void dealGroupDamage() {
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

    private void applyDamage(AbstractCreature target, int dmg) {
        val info = BindingHelper.makeInfo(damageMods, owner, dmg, DamageType.THORNS);
        addToBot(new DamageAction(target, info, AttackEffect.FIRE));
    }

    @Override
    public void updateDescription() {
        if (owner instanceof AbstractPlayer) {
            description = strings.DESCRIPTIONS[0];
        } else {
            description = strings.DESCRIPTIONS[1];
        }
        description += amount + strings.DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BurningPower(owner, source, amount);
    }
}
