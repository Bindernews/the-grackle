package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.cardmods.ExtraHitsMod;
import io.bindernews.thegrackle.cardmods.RequireAloftMod;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;

public class AirToGroundMissiles extends BaseCard {
    public static final CardConfig C =
            new CardConfig("AirToGroundMissiles", CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(2);
        c.damage(16, 20);
        c.add(ExtraHitsVariable.inst, 1, -1);
        c.multiDamage(true, true);
        c.addModifier(ExtraHitsMod::new);
        c.addModifier(RequireAloftMod::new);
    });

    public AirToGroundMissiles() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
        val hits = ExtraHitsVariable.inst.value(this);
        for (int i = 0; i < hits; i++) {
            if (p instanceof AbstractPlayer) {
                addToBot(new DamageAllEnemiesAction(p, multiDamage, damageType, fx));
            } else {
                addToBot(new DamageAction(m, new DamageInfo(p, damage, damageType), fx));
            }
        }
    }
}
