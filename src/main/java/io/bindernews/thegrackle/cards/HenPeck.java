package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.cardmods.ExtraHitsMod;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;

public class HenPeck extends BaseCard implements ExtraHitsVariable.Mixin {
    public static final CardConfig C =
            new CardConfig("HenPeck", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.damage(4, -1);
        c.add(ExtraHitsVariable.inst, 3, 4);
        c.addModifier(ExtraHitsMod::new);
    });

    public HenPeck() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
        val hits = getExtraHits();
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), fx));
        }
    }
}
