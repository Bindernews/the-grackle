package net.bindernews.bnsts.api;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EqualsAndHashCode
@AllArgsConstructor
public class ScvChangeCardEvent {
    @NotNull private AbstractCard card;
    @Nullable private CardGroup group;
    boolean open;

    public @NotNull AbstractCard getCard() {
        return card;
    }

    public @Nullable CardGroup getGroup() {
        return group;
    }

    public boolean getOpen() {
        return open;
    }
}
