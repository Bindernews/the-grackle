package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import io.bindernews.thegrackle.GrackleMod;

public class FiredUpPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(FiredUpPower.class);

    public FiredUpPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        loadRegion("vigor");
        updateDescription();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card.cardID.equals(Burn.ID)) {
            addToBot(new ApplyPowerAction(owner, owner, new VigorPower(owner, amount)));
        }
    }

    @Override
    public void updateDescription() {
        description = formatDesc(0, amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FiredUpPower(owner, amount);
    }
}
