package net.bindernews.bnsts.api;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.jetbrains.annotations.NotNull;

public interface IPopup extends Comparable<IPopup> {
    /**
     * Render the popup.
     * @param sb sprite batch instance
     */
    void render(SpriteBatch sb);

    /**
     * Give the popup the chance to update itself.
     */
    void update();

    /**
     * Allows the popup to report that it's no longer enabled and visible.
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Get the popup's render priority, with lower values being rendered and updated first.
     */
    default int getPriority() {
        return 0;
    }

    @Override
    default int compareTo(@NotNull IPopup o) {
        int d = getPriority() - o.getPriority();
        if (d != 0) {
            return d;
        } else {
            return hashCode() - o.hashCode();
        }
    }
}
