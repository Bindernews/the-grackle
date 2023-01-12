package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.NeutralStance;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.cardmods.RequireAloftMod;
import lombok.val;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class CrashLanding extends BaseCard {
    public static final CardConfig C =
            new CardConfig("CrashLanding", CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.damage(12, 20);
        c.addModifier(RequireAloftMod::new);
    });

    public CrashLanding() {
        super(C, VARS);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageType), fx));
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID));
    }
}
