package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RepairPower;
import io.bindernews.bnsts.CardNums;

/**
 * It's Self-Repair.
 */
public class PhoenixFeather extends BaseCard {
    public static final CardConfig CFG = new CardConfig("PhoenixFeather");
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .magic(7).magicUpg(10)
            .build();

    public PhoenixFeather() {
        super(CFG, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        NUM.init(this);
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RepairPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
