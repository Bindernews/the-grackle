package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardNums;
import io.bindernews.thegrackle.cardmods.ExtraHitsMod;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;

public class HenPeck extends BaseCard implements ExtraHitsVariable.Mixin {
    public static final CardConfig C = new CardConfig("HenPeck", CardType.ATTACK);
    public static final CardNums NUM = CardNums.builder()
            .cost(1)
            .damage(4).extraHits(3).extraHitsUpg(4).build();

    public HenPeck() {
        super(C, CardRarity.COMMON, CardTarget.ENEMY);
        NUM.init(this);
        ExtraHitsMod.applyTo(this);
        initializeDescription();
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
        val hits = getExtraHits();
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), fx));
        }
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
