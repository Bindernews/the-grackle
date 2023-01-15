package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import io.bindernews.bnsts.CardVariables;
import org.jetbrains.annotations.NotNull;

public class TargetingComputer extends BaseCard {
    public static final CardConfig C =
            new CardConfig("TargetingComputer", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1);
        c.magic(3, 6);
    });

    public TargetingComputer() {
        super(C, VARS);
    }

    @Override
    public void apply(@NotNull AbstractCreature p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber), magicNumber));
    }
}
