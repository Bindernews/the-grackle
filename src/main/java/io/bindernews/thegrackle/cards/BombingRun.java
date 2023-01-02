package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;

public class BombingRun extends BaseCard {
    public static final CardConfig C = new CardConfig("BombingRun", CardType.ATTACK);
    static final CardNums NUM = CardNums.builder()
            .cost(1).damage(6).damageUpg(10).extraHits(1).build();

    public BombingRun() {
        super(C, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        NUM.init(this);
        isMultiDamage = true;
        GrackleMod.multiHitManager.tagMultiHit(this);
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
        val hits = ExtraHitsVariable.inst.value(this);
        for (int i = 0; i < hits; i++) {
            addToBot(iop().damageAllEnemies(p, multiDamage, damageTypeForTurn, fx));
        }
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
