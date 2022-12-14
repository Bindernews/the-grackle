package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.actions.FightFireAction;
import io.bindernews.thegrackle.helper.BurnHelper;

public class FightFire extends BaseCard {
    public static final CardConfig C = new CardConfig("FightFire", CardType.ATTACK);
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .damage(4)
            .magic(4).magicUpg(6)
            .build();

    public FightFire() {
        super(C, CardRarity.UNCOMMON, CardTarget.ENEMY);
        NUM.init(this);
        damageType = DamageInfo.DamageType.NORMAL;
        rawDescription = String.format(rawDescription, NUM.damage);
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        updateBaseDamage();
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        updateBaseDamage();
        super.calculateCardDamage(mo);
        initializeDescription();
    }

    public void updateBaseDamage() {
        int burnCount = BurnHelper.countBurnsInGroup(BurnHelper.getDiscard());
        baseDamage = NUM.damage + burnCount * magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new FightFireAction(p, m, magicNumber));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
