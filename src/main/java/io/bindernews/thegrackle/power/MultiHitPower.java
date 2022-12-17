package io.bindernews.thegrackle.power;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.helper.MultiHitManager;

public class MultiHitPower extends BasePower {
    public static final String POWER_ID = GrackleMod.makeId(MultiHitPower.class);

    public MultiHitPower(AbstractCreature owner, int amount) {
        super(POWER_ID);
        setOwnerAmount(owner, amount);
        updateDescription();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        MultiHitManager.inst().handleCardUse(card, amount, AbstractDungeon.player, m);
    }

    @Override
    public AbstractPower makeCopy() {
        return new MultiHitPower(owner, amount);
    }
}
