package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AttackR extends BaseCard {
    public static final CardConfig C = new CardConfig("AttackR", 1);

    public AttackR() {
        super(C, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        damage = baseDamage = 8;
        damageType = damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageType)));
    }

    @Override
    public void onUpgrade() {
    }
}
