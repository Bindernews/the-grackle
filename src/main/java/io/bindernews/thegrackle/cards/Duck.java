package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.BlurPower;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.actions.SoundAction;

public class Duck extends BaseCard {
    public static final CardConfig C = new CardConfig("Duck", CardType.SKILL);
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .block(7).blockUpg(11)
            .magic(1)
            .build();

    /**
     * Set to false to disable quack sound.
     */
    public boolean playSound = true;

    public Duck() {
        super(C, CardRarity.COMMON, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new BlurPower(p, magicNumber), magicNumber));
        if (playSound) {
            addToBot(new SoundAction(GrackleMod.CO.SFX_QUACK));
        }
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
