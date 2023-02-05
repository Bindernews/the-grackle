package io.bindernews.bnsts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import org.jetbrains.annotations.NotNull;

public interface IVariable {

    int value(@NotNull AbstractCard card);
    int baseValue(@NotNull AbstractCard card);
    boolean upgraded(@NotNull AbstractCard card);
    boolean isModified(@NotNull AbstractCard card);

    void setValue(@NotNull AbstractCard card, int amount);
    void setBaseValue(@NotNull AbstractCard card, int amount);
    void upgrade(@NotNull AbstractCard card, int amount);
}
