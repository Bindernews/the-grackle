package net.bindernews.grackle.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import net.bindernews.grackle.GrackleMod;

public class SpeedFormPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(SpeedFormPower.class);

    public SpeedFormPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        type = PowerType.BUFF;
        loadRegion("slow");
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        this.amount = 0;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + FontHelper.colorString(this.owner.name, "y") + DESCRIPTIONS[1];
        if (this.amount != 0) {
            this.description = this.description + DESCRIPTIONS[2] + this.amount * 10 + DESCRIPTIONS[3];
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.addToBot(new ApplyPowerAction(owner, owner, new SpeedFormPower(owner, 1), 1));
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * (1.0F + (float)this.amount * 0.1F) : damage;
    }
}
