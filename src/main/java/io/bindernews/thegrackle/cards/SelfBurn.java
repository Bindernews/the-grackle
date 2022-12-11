package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;

public class SelfBurn extends BaseCard {
    public static final CardConfig CFG = new CardConfig("SelfBurn");
    public static final CardNums NUM = CardNums.builder()
            .cost(0).magic(1).magicUpg(2).build();
    static final int BURN_COUNT = 1;

    public SelfBurn() {
        super(CFG, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new MakeTempCardInDiscardAction(new Burn(), BURN_COUNT));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
