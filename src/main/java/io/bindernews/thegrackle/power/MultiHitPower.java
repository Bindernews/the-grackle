package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.helper.MultiHitManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MultiHitPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(MultiHitPower.class);

    static {
        // Add our number of extra hits as early as possible
        MultiHitManager.hitCountEvents.listen(ev -> ev.addCount(getAmountOn(ev.getSource())), 0);
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
        if (card.type == AbstractCard.CardType.ATTACK && card.hasTag(MultiHitManager.GK_MULTI_HIT)) {
            flash();
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new MultiHitPower(owner, amount);
    }

    /**
     * Returns the amount of this power that {@code source} currently has
     * @param source Creature to target
     * @return Power amount or 0 if source doesn't have the power
     */
    public static int getAmountOn(@NotNull AbstractCreature source) {
        return Optional.ofNullable(source.getPower(MultiHitPower.POWER_ID)).map(p -> p.amount).orElse(0);
    }
}
