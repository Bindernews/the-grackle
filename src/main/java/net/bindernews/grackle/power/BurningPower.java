package net.bindernews.grackle.power;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import net.bindernews.grackle.GrackleMod;
import net.bindernews.grackle.helper.PowerUtil;
import org.jetbrains.annotations.NotNull;

public class BurningPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(BurningPower.class);

    public BurningPower(AbstractCreature owner, int amount) {
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
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage + (float)this.amount : damage;
    }

    @Override
    public int onAttacked(@NotNull DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            this.flashWithoutSound();
        }

        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        if (PowerUtil.isStartOfMonsterTurn(owner)) {
            actionReducePower();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (PowerUtil.isEndOfPlayerTurn(owner, isPlayer)) {
            actionReducePower();
        }
    }

    private void actionReducePower() {
        int reduceAmount = Math.max(2, this.amount / 2);
        addToBot(new ReducePowerAction(owner, owner, POWER_ID, reduceAmount));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
