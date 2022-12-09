package io.bindernews.thegrackle.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.function.Supplier;

public class LazyAction extends AbstractGameAction {

    private final Supplier<AbstractGameAction> ctor;

    public LazyAction(Supplier<AbstractGameAction> ctor) {
        this.ctor = ctor;
    }

    @Override
    public void update() {
        AbstractGameAction delegate = ctor.get();
        if (delegate != null) {
            this.addToTop(delegate);
        }
        this.isDone = true;
    }
}
