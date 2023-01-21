package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.bnsts.CardVariables;
import lombok.val;

@AutoAdd.Seen
public class Strike_GK extends BaseCard {
    public static final CardConfig C =
            new CardConfig("Strike_GK", CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
    static final CardVariables VARS = CardVariables.config(c -> {
        c.cost(1, -1);
        c.damage(6, 9);
    });

    public Strike_GK() {
        super(C, VARS);
        tags.add(CardTags.STARTER_STRIKE);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void apply(AbstractCreature p, AbstractCreature m) {
        val fx = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), fx));
    }
}
