package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;

public class PowerR extends BaseCard {
    public static final CardConfig C = new CardConfig("PowerR");
    public static final CardNums NUM = CardNums.builder().cost(1).block(6).blockUpg(10).build();

    public PowerR() {
        super(C, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
