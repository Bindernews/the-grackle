package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.helper.MultiHitManager;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;

public class Grenenade extends BaseCard implements ExtraHitsVariable.Mixin {
    public static final CardConfig C = new CardConfig("Grenenade", CardType.ATTACK);
    public static final CardNums NUM = CardNums.builder()
            .cost(2).damage(4).extraHits(1).build();

    public Grenenade() {
        super(C, CardRarity.RARE, CardTarget.ALL_ENEMY);
        NUM.init(this);
        isMultiDamage = true;
        tags.add(MultiHitManager.GK_MULTI_HIT);
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.FIRE;
        val hits = getExtraHits();
        for (int i = 0; i < hits; i++) {
            addToBot(iop().damageAllEnemies(p, multiDamage, damageTypeForTurn, fx));
        }
    }

    @Override
    protected void upgradeName() {
        super.upgradeName();
        val nameParts = C.getStrings().EXTENDED_DESCRIPTION;
        val sb = new StringBuilder();
        sb.append(nameParts[0]);
        for (int i = 0; i < timesUpgraded; i++) {
            sb.append(nameParts[1]);
        }
        sb.append(nameParts[2]);
        name = sb.toString();
    }

    @Override
    public void upgrade() {
        upgradeName();
        // Odd upgrades (1st, 3rd, etc.) will upgrade hit count
        // Even upgrades will upgrade damage amount
        if (timesUpgraded % 2 == 0) {
            upgradeDamage(1);
        } else {
            ExtraHitsVariable.inst.upgrade(this, 1);
        }
    }
}
