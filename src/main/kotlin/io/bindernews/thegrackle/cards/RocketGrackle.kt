package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import io.bindernews.bnsts.CardVariables;
import lombok.val;

public class RocketGrackle extends BaseCard {
    public static final CardConfig C =
            new CardConfig("RocketGrackle", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.damage(8, 12);
        c.magic(1, 2);
    });

    public RocketGrackle() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
        val isMonster = p instanceof AbstractMonster;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), fx));
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, isMonster), magicNumber));
    }
}
