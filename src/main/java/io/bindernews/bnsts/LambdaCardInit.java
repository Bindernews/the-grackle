package io.bindernews.bnsts;


import com.megacrit.cardcrawl.cards.AbstractCard;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor
public class LambdaCardInit implements ICardInitializer {
    @NotNull
    public final Consumer<AbstractCard> fInit;
    @NotNull
    public final Consumer<AbstractCard> fUpgrade;

    @Override
    public void init(AbstractCard card) {
        fInit.accept(card);
    }

    @Override
    public void upgrade(AbstractCard card) {
        fUpgrade.accept(card);
    }
}
