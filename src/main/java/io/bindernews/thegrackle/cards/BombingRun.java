package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.cardmods.ExtraHitsMod;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class BombingRun extends BaseCard {
    public static final CardConfig C =
            new CardConfig("BombingRun", CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1);
        c.damage(6, 10);
        c.add(ExtraHitsVariable.inst, 1, -1);
        c.addModifier(new ExtraHitsMod());
        c.multiDamage(true, true);
    });

    public BombingRun() {
        super(C, VARS);
    }

    @Override
    public void apply(@NotNull AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
        val hits = ExtraHitsVariable.inst.value(this);
        for (int i = 0; i < hits; i++) {
            addToBot(iop().damageAllEnemies(p, multiDamage, damageTypeForTurn, fx));
        }
    }
}
