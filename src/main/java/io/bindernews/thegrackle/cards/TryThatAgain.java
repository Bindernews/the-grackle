package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.unique.CalculatedGambleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;

public class TryThatAgain extends BaseCard {
    public static final CardConfig C = new CardConfig("TryThatAgain", CardType.SKILL);
    public static final CardNums NUM = CardNums.builder().cost(1).costUpg(0).build();

    public TryThatAgain() {
        super(C, CardRarity.UNCOMMON, CardTarget.NONE);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CalculatedGambleAction(true));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
