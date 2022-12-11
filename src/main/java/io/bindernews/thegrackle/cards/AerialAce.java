package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.actions.LazyAction;
import io.bindernews.thegrackle.stance.StanceAloft;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

public class AerialAce extends BaseCard {
    public static final CardConfig CFG = new CardConfig("AerialAce");
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .damage(8).damageUpg(12)
            .build();

    public AerialAce() {
        super(CFG, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        NUM.init(this);
        damageType = damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
        return tmp;
    }

    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        addToBot(new LazyAction(() -> {
            int damageEf = damage * (StanceAloft.isAloft(p) ? 2 : 1);
            return new DamageAction(m, new DamageInfo(p, damageEf, damageType), AttackEffect.SLASH_DIAGONAL);
        }));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
