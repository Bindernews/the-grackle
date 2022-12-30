package io.bindernews.thegrackle.power;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.helper.BurnHelper;

public class BurnCreamPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(BurnCreamPower.class);

    public BurnCreamPower(AbstractCreature owner) {
        super(POWER_ID);
        setOwnerAmount(owner, -1);
        isTurnBased = false;
        type = PowerType.BUFF;
        loadRegion("evolve");
        updateDescription();
    }

    public BurnCreamPower(AbstractCreature owner, int ignoredAmount) {
        this(owner);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (BurnHelper.isBurn(card)) {
            CardModifierManager.addModifier(card, new ExhaustMod());
        }
    }

    @Override
    public void updateDescription() {
        description = formatDesc(0);
    }

    @Override
    public AbstractPower makeCopy() {
        return new BurnCreamPower(owner);
    }
}
