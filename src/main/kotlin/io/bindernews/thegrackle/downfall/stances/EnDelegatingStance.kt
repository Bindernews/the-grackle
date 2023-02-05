package io.bindernews.thegrackle.downfall.stances;

import charbosses.stances.AbstractEnemyStance;
import io.bindernews.thegrackle.stance.StanceAloft;
import lombok.experimental.Delegate;

public class EnStanceAloft extends AbstractEnemyStance {
    public static final String STANCE_ID = StanceAloft.STANCE_ID;

    @Delegate(types = EnemyStanceDelegate.class)
    private final StanceAloft inner = new StanceAloft();

    public EnStanceAloft() {
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
