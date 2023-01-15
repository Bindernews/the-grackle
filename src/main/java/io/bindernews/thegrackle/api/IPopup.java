package io.bindernews.thegrackle.api;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IPopup {

    void render(SpriteBatch sb);
    void update();

    boolean isEnabled();
}
