package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.BlurPower;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.actions.SoundAction;

public class Duck extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Duck", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.block(7, 11);
        c.magic(1, -1);
    });

    /**
     * Set to false to disable quack sound.
     */
    public boolean playSound = true;

    public Duck() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new BlurPower(p, magicNumber), magicNumber));
        if (playSound) {
            addToBot(new SoundAction(GrackleMod.CO.SFX_QUACK));
        }
    }
}
