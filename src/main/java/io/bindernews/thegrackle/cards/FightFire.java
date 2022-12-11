package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.actions.FightFireAction;

public class FightFire extends BaseCard {
    public static final CardConfig CFG = new CardConfig("FightFire");
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .magic(3).magicUpg(6)
            .build();

    public FightFire() {
        super(CFG, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        NUM.init(this);
        damageType = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void applyPowers() {
        int burnCount = FightFireAction.countBurnsInGroup(AbstractDungeon.player.discardPile);
        baseDamage = burnCount * magicNumber;
        super.applyPowers();
        initializeDescription();
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
