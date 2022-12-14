package io.bindernews.thegrackle.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import lombok.val;

import static io.bindernews.thegrackle.helper.BurnHelper.exhaustBurnsInGroup;

public class FightFireAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int baseDmg;

    public FightFireAction(AbstractPlayer player, AbstractCreature target, int baseDmg, int damageMultiplier) {
        this.source = player;
        this.player = player;
        this.target = target;
        this.baseDmg = baseDmg;
        this.amount = damageMultiplier;
        this.attackEffect = AttackEffect.FIRE;
    }

    @Override
    public void update() {
        isDone = true;
        if (target != null && target.currentHealth > 0) {
            val dmg = baseDmg + (exhaustBurnsInGroup(player.discardPile) * amount);
            addToTop(new DamageAction(target, new DamageInfo(player, dmg, damageType), attackEffect));
        }
    }
}
