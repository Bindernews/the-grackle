package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PowerR extends BaseCard {
    public static final CardConfig C = new CardConfig("PowerR", 1);

    public PowerR() {
        super(C, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        block = baseBlock = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void onUpgrade() {
    }
}
