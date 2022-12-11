package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.TargetingComputerPower;

public class TargetingComputer extends BaseCard {
    public static final CardConfig CFG = new CardConfig("TargetingComputer");
    public static final CardNums NUM = CardNums.builder()
            .cost(1).magic(6).magicUpg(10).build();

    public TargetingComputer() {
        super(CFG, CardType.POWER, CardRarity.COMMON, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TargetingComputerPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
