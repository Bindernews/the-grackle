package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EvolvePower;
import io.bindernews.bnsts.CardNums;

public class BurnCream extends BaseCard {
    public static final CardConfig C = new CardConfig("BurnCream", CardType.POWER);
    public static final CardNums NUM = CardNums.builder()
            .cost(1).magic(1).magicUpg(2).build();

    public BurnCream() {
        super(C, CardRarity.UNCOMMON, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new EvolvePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
