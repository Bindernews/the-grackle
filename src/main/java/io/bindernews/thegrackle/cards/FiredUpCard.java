package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.FiredUpPower;

public class FiredUpCard extends BaseCard {
    public static final CardConfig C = new CardConfig("FiredUpCard", CardType.POWER);
    public static final CardNums NUM = CardNums.builder()
            .cost(2).costUpg(1).magic(8).build();

    public FiredUpCard() {
        super(C, CardRarity.RARE, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FiredUpPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
