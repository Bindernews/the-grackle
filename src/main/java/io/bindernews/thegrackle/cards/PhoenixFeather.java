package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RepairPower;

/**
 * It's Self-Repair.
 */
public class PhoenixFeather extends BaseCard {
    public static final CardConfig C = new CardConfig("PhoenixFeather", 1);

    public PhoenixFeather() {
        super(C, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = 7;
        magicNumber = baseMagicNumber;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RepairPower(p, magicNumber), magicNumber));
    }

    @Override
    public void onUpgrade() {
        upgradeMagicNumber(3);
    }
}
