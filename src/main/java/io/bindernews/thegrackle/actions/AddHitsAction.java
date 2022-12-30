package io.bindernews.thegrackle.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;

import java.util.Arrays;
import java.util.function.Consumer;

public class AddHitsAction extends AbstractGameAction {

    private final AbstractCard card;
    private final int amount;
    private final CardGroup[] cardGroups;

    public AddHitsAction(AbstractCard card, int amount, CardGroup[] cardGroups) {
        this.card = card;
        this.amount = amount;
        this.cardGroups = cardGroups;
    }

    @Override
    public void update() {
        val clz = card.getClass();
        val hv = ExtraHitsVariable.inst;

        Consumer<AbstractCard> applyToCard = card -> {
            hv.setBaseValue(card, hv.baseValue(card) + amount);
            card.applyPowers();
        };

        applyToCard.accept(card);
        Arrays.stream(cardGroups)
                .flatMap(a -> a.group.stream())
                .parallel()
                .filter(clz::isInstance)
                .forEach(applyToCard);
        isDone = true;
    }

    public static CardGroup[] getPlayerCardGroups() {
        val p = AbstractDungeon.player;
        return new CardGroup[]{p.hand, p.discardPile, p.exhaustPile, p.drawPile};
    }
}
