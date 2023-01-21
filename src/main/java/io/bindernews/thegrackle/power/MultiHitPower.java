package io.bindernews.thegrackle.power;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;

public class MultiHitPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(MultiHitPower.class);

    @SpireEnum
    public static AbstractCard.CardTags GK_MULTI_HIT_PRESERVE;

    static {
        ExtraHitsVariable.getOnApplyPowers().on(-5, ExtraHitsVariable.addPowerAmount(POWER_ID));
    }

    public MultiHitPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        loadRegion("vigor");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = formatDesc(0, amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK
                && card.hasTag(ExtraHitsVariable.GK_MULTI_HIT)
                && !card.hasTag(GK_MULTI_HIT_PRESERVE)
        ) {
            flash();
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new MultiHitPower(owner, amount);
    }
}
