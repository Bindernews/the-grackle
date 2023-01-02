package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import io.bindernews.bnsts.CardNums;
import lombok.val;

public class RocketGrackle extends BaseCard {
    public static final CardConfig C = new CardConfig("RocketGrackle", CardType.ATTACK);
    static final CardNums NUM = CardNums.builder()
            .cost(1)
            .damage(8).damageUpg(12)
            .magic(1).magicUpg(2)
            .build();

    public RocketGrackle() {
        super(C, CardRarity.COMMON, CardTarget.ENEMY);
        NUM.init(this);
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
        val isMonster = p instanceof AbstractMonster;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), fx));
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, isMonster), magicNumber));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
