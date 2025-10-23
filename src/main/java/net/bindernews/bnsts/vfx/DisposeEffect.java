package net.bindernews.bnsts.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DisposeEffect extends AbstractGameEffect {
    private final ArrayList<Disposable> disposables;

    public DisposeEffect(@NotNull Disposable disposable) {
        this(Collections.singleton(disposable));
    }

    public DisposeEffect(@NotNull Collection<Disposable> disposables) {
        this.duration = 0.01f;
        this.startingDuration = 0.01f;
        this.disposables = new ArrayList<>(disposables);
    }

    public void enqueue() {
        AbstractDungeon.topLevelEffectsQueue.add(this);
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0.0f) {
            this.dispose();
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {}

    @Override
    public void dispose() {
        for (Disposable d : this.disposables) {
            d.dispose();
        }
        // clear the list so that we don't accidentally dispose of things twice
        disposables.clear();
        // done here
        isDone = true;
    }
}
