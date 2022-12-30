package io.bindernews.thegrackle.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.thegrackle.helper.BurnHelper;
import lombok.val;

import static io.bindernews.thegrackle.helper.ModInterop.iop;

public class FightFireAction extends AbstractGameAction {
    private final int baseDmg;

    public FightFireAction(AbstractCreature source, AbstractCreature target, int baseDmg, int damageMultiplier) {
        this.source = source;
        this.target = target;
        this.baseDmg = baseDmg;
        this.amount = damageMultiplier;
        this.attackEffect = AttackEffect.FIRE;
    }

    @Override
    public void update() {
        isDone = true;
        val discardPile = iop().getCardsByType(source, CardGroup.CardGroupType.DISCARD_PILE)
                .orElse(null);
        if (target != null && target.currentHealth > 0 && discardPile != null) {
            val dmg = baseDmg + (BurnHelper.exhaustBurnsInGroup(discardPile) * amount);
            addToTop(new DamageAction(target, new DamageInfo(source, dmg, damageType), attackEffect));
        }
    }
}
