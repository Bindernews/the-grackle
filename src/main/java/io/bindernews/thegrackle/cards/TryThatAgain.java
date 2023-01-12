package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.unique.CalculatedGambleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardVariables;

public class TryThatAgain extends BaseCard {
    public static final CardConfig C =
            new CardConfig("TryThatAgain", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
    static final CardVariables VARS = CardVariables.config(c -> c.cost(1, 0));

    public TryThatAgain() {
        super(C, VARS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CalculatedGambleAction(true));
    }
}
