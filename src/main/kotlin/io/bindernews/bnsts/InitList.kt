package io.bindernews.bnsts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import lombok.val;

import java.util.ArrayList;
import java.util.function.Consumer;

public class InitList implements ICardInitializer {

    private final ArrayList<Consumer<AbstractCard>> doInit = new ArrayList<>();
    private final ArrayList<Consumer<AbstractCard>> doUpgrade = new ArrayList<>();

    public void onInit(Consumer<AbstractCard> action) {
        doInit.add(action);
    }

    public void onUpgrade(Consumer<AbstractCard> action) {
        doUpgrade.add(action);
    }

    @Override
    public void init(AbstractCard card) {
        for (val c : doInit) {
            c.accept(card);
        }
    }

    @Override
    public void upgrade(AbstractCard card) {
        for (val c : doUpgrade) {
            c.accept(card);
        }
    }
}
