package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import io.bindernews.bnsts.CardNums;

public class Cackle extends BaseCard {
    public static final CardConfig C = new CardConfig("Cackle");
    static final CardNums NUM = CardNums.builder()
            .cost(0)
            .magic(1).magicUpg(2)
            .build();

    public Cackle() {
        super(C, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
