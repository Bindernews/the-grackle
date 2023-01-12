package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RepairPower;
import io.bindernews.bnsts.CardVariables;

/**
 * It's Self-Repair.
 */
public class PhoenixFeather extends BaseCard {
    public static final CardConfig C =
            new CardConfig("PhoenixFeather", CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.magic(7, 10);
    });

    public PhoenixFeather() {
        super(C, VARS);
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RepairPower(p, magicNumber), magicNumber));
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        BaseCard.throwPlayerOnly();
    }
}
