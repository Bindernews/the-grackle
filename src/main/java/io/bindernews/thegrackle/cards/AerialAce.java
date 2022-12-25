package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.stance.StanceAloft;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

public class AerialAce extends BaseCard {
    public static final CardConfig CFG = new CardConfig("AerialAce", CardType.ATTACK);
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .damage(8).damageUpg(12)
            .build();

    public AerialAce() {
        super(CFG, CardRarity.COMMON, CardTarget.ENEMY);
        NUM.init(this);
        damageType = damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
        if (StanceAloft.isAloft(player.stance)) {
            tmp *= 2.f;
        }
        return tmp;
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageType), AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
