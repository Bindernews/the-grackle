package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import io.bindernews.bnsts.CardVariables;
import io.bindernews.thegrackle.cardmods.ExtraHitsMod;
import io.bindernews.thegrackle.cardmods.RequireAloftMod;
import io.bindernews.thegrackle.stance.StanceAloft;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

/**
 * Crash-Landing, but better.
 */
public class Paratrooper extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Paratrooper", CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.damage(7, 12);
        c.add(ExtraHitsVariable.inst, 1, -1);
        c.addModifier(ExtraHitsMod::new);
        c.addModifier(RequireAloftMod::new);
    });

    /** For damage display calculation */
    public AbstractCreature owner = AbstractDungeon.player;

    public Paratrooper() {
        super(C, VARS);
    }

    @Override
    public void calculateDamageDisplay(AbstractMonster mo) {
        val stance = StanceAloft.getInstanceOn(owner);
        stance.ifPresent(st -> st.enabled = false);
        calculateCardDamage(mo);
        stance.ifPresent(st -> st.enabled = true);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        val hits = ExtraHitsVariable.inst.value(this);
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID));
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), fx));
        }
    }
}
