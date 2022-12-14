package io.bindernews.thegrackle.helper;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.ArrayList;

@UtilityClass
public class BurnHelper {

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

    public static CardGroup getHand() {
        return AbstractDungeon.player.hand;
    }

    public static CardGroup getDiscard() {
        return AbstractDungeon.player.discardPile;
    }

    public static CardGroup getDraw() {
        return AbstractDungeon.player.drawPile;
    }
}
