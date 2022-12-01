package io.bindernews.thegrackle.cards;

import io.bindernews.thegrackle.Grackle;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Cackle extends BaseCard {
    public static final CardConfig C = new CardConfig("Cackle", 0);

    public Cackle() {
        super(C, Grackle.En.COLOR_BLACK, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseMagicNumber = 1;
        magicNumber = baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
    }

    @Override
    public void onUpgrade() {
        upgradeMagicNumber(1);
    }
}
