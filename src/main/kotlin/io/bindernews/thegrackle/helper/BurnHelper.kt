package io.bindernews.thegrackle.helper;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Burn;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.ArrayList;
import java.util.stream.Stream;

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

    public static int countBurns(CardGroup group) {
        return (int)getBurns(group).count();
    }

    public static Stream<AbstractCard> getBurns(CardGroup group) {
        return group.group.stream().filter(BurnHelper::isBurn);
    }

    public static boolean isBurn(AbstractCard card) {
        return card.cardID.equals(Burn.ID);
    }
}
