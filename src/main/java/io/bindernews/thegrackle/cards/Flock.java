package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.actions.AddHitsAction;
import io.bindernews.thegrackle.cardmods.ExtraHitsMod;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class Flock extends BaseCard implements ExtraHitsVariable.Mixin {
    public static final CardConfig C =
            new CardConfig("Flock", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.damage(2, 4);
        c.magic(1, -1);
        c.add(ExtraHitsVariable.inst, 1, 2);
        c.addModifier(new ExtraHitsMod());
    });

    public Flock() {
        super(C, VARS);
    }

    @Override
    public void apply(@NotNull AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
        val hits = getExtraHits();
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), fx));
        }
        addToBot(new AddHitsAction(this, magicNumber, AddHitsAction.getPlayerCardGroups()));
    }
}
