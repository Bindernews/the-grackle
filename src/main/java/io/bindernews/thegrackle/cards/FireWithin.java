package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.FireWithinPower;

public class FireWithin extends BaseCard {
    public static final CardConfig C = new CardConfig("FireWithin");
    public static final CardNums NUM = CardNums.builder().cost(1).magic(2).magicUpg(3).build();

    public FireWithin() {
        super(C, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FireWithinPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
