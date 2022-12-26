package io.bindernews.thegrackle.downfall.stances;

import charbosses.stances.AbstractEnemyStance;
import io.bindernews.thegrackle.stance.StancePhoenix;
import lombok.experimental.Delegate;

public class EnStancePhoenix extends AbstractEnemyStance {
    public static final String STANCE_ID = StancePhoenix.STANCE_ID;

    @Delegate(types = EnemyStanceDelegate.class)
    private final StancePhoenix inner = new StancePhoenix();

    public EnStancePhoenix() {
        ID = inner.ID;
        name = inner.name;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        inner.updateDescription();
        description = inner.description;
    }
}
