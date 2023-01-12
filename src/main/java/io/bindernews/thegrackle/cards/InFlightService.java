package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.potions.RegenPotion;
import com.megacrit.cardcrawl.powers.RegenPower;
import io.bindernews.bnsts.CardVariables;
import lombok.val;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class InFlightService extends BaseCard {
    public static final CardConfig C =
            new CardConfig("InFlightService", CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(2, 1);
        c.magic(5, -1);
        c.exhaust(true, true);
    });

    public InFlightService() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        if (p instanceof AbstractPlayer) {
            addToBot(new ObtainPotionAction(new RegenPotion()));
        } else {
            val power = iop().createPower(RegenPower.POWER_ID, p, magicNumber);
            addToBot(new ApplyPowerAction(p, p, power, magicNumber));
        }
    }
}
