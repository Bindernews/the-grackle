package io.bindernews.thegrackle.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import lombok.val;

import java.util.ArrayList;

public class FightFireAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public FightFireAction(AbstractPlayer player, AbstractCreature target, int damageMultiplier) {
        this.source = player;
        this.player = player;
        this.target = target;
        this.amount = damageMultiplier;
        this.attackEffect = AttackEffect.FIRE;
    }

    @Override
    public void update() {
        isDone = true;
        if (target != null && target.currentHealth > 0) {
            val dmg = exhaustBurnsInGroup(player.discardPile) * amount;
            addToTop(new DamageAction(target, new DamageInfo(player, dmg, damageType), attackEffect));
        }
    }

    /**
     * Exhausts all Burn cards in the given group, returns the number of cards exhausted.
     * @param group Card group
     * @return number of cards exhausted
     */
    public static int exhaustBurnsInGroup(CardGroup group) {
        val burnCards = new ArrayList<AbstractCard>();
        for (val card : group.group) {
            if (card.cardID.equals(Burn.ID)) {
                burnCards.add(card);
            }
        }
        for (val card : burnCards) {
            group.moveToExhaustPile(card);
        }
        return burnCards.size();
    }

    public static int countBurnsInGroup(CardGroup group) {
        int count = 0;
        for (val card : group.group) {
            if (card.cardID.equals(Burn.ID)) {
                count += 1;
            }
        }
        return count;
    }
}
