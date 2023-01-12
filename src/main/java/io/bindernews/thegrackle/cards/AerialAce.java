package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.stance.StanceAloft;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

public class AerialAce extends BaseCard {
    public static final CardConfig C =
            new CardConfig("AerialAce", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1);
        c.damage(8, 12);
    });

    public AerialAce() {
        super(C, VARS);
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
}
