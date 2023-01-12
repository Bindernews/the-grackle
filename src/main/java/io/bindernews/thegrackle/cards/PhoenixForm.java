package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.power.HealingPhoenixPower;

@AutoAdd.Ignore
public class PhoenixForm extends BaseCard {
    public static final CardConfig C =
            new CardConfig("PhoenixForm", CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(3, 2);
        c.magic(6, -1);
    });

    public PhoenixForm() {
        super(C, VARS);
        tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new HealingPhoenixPower(p, magicNumber), magicNumber));
    }
}
