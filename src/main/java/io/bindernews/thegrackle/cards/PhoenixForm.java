package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.thegrackle.power.HealingPhoenixPower;

@AutoAdd.Ignore
public class PhoenixForm extends BaseCard {
    public static final CardConfig C = new CardConfig("PhoenixForm", 3);
    public static final int UPGRADE_COST = 2;
    public static final int HEAL = 6;

    public PhoenixForm() {
        super(C, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        tags.add(BaseModCardTags.FORM);
        baseMagicNumber = HEAL;
        magicNumber = baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new HealingPhoenixPower(p, magicNumber), magicNumber));
    }

    @Override
    public void onUpgrade() {
        upgradeBaseCost(UPGRADE_COST);
    }
}
