package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.actions.AddHitsAction;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;

public class Flock extends BaseCard implements ExtraHitsVariable.Mixin {
    public static final CardConfig C = new CardConfig("Flock", CardType.ATTACK);
    static final CardNums NUM = CardNums.builder()
            .cost(1)
            .damage(2).damageUpg(4)
            .extraHits(1).extraHitsUpg(2)
            .magic(1)
            .build();

    public Flock() {
        super(C, CardRarity.UNCOMMON, CardTarget.ENEMY);
        GrackleMod.multiHitManager.tagMultiHit(this);
        NUM.init(this);
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
        val hits = getExtraHits();
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), fx));
        }
        addToBot(new AddHitsAction(this, magicNumber, AddHitsAction.getPlayerCardGroups()));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
