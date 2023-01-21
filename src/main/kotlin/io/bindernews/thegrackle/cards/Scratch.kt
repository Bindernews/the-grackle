package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class Scratch extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Scratch", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(0, -1);
        c.damage(6, 10);
    });

    public Scratch() {
        super(C, VARS);
    }

    @Override
    public void apply(@NotNull AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageType), fx));
    }
}
