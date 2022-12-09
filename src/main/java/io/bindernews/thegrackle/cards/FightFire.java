package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FightFire extends BaseCard {
    public static final CardConfig C = new CardConfig("FightFire", 1);

    public FightFire() {
        super(C, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = 8;
        damageType = damageTypeForTurn = DamageInfo.DamageType.NORMAL;
        flavorText = C.strings.EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageType)));
    }

    @Override
    public void onUpgrade() {
    }
}
