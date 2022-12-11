package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.power.HealingPhoenixPower;

@AutoAdd.Ignore
public class PhoenixForm extends BaseCard {
    public static final CardConfig CFG = new CardConfig("PhoenixForm");
    public static final CardNums NUM = CardNums.builder()
            .cost(3).costUpg(2)
            .magic(6)
            .build();

    public PhoenixForm() {
        super(CFG, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        tags.add(BaseModCardTags.FORM);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new HealingPhoenixPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
