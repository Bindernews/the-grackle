package net.bindernews.sts.slots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import lombok.Data;
import net.bindernews.eventbus.EventEmit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class SlotHelper {

    public static final Vector2 DEFAULT_SLOT_SIZE = new Vector2(75f, 75f);

    public final EventEmit<CountChangedEvent> onCountChanged = new EventEmit<>();

    protected TextureRegion texBright;
    protected TextureRegion texDark;
    protected TextureRegion texOverlay;


    /**
     * From left-to-right, which technique slots are enabled.
     */
    protected boolean[] slotEnabled = new boolean[0];

    /**
     * From left-to-right, data about how to display each slot.
     */
    protected float[] slotDisplay = new float[0];

    /**
     * Left to right, (x, y) pairs denoting positions of each slot relative to character.
     */
    protected float[] slotPositions = new float[0];

    /**
     * Calculate the positions of each slot, returning an array of {@code count * 2} size,
     * containing pairs of positions.
     *
     * @param count How many slots there are
     * @return Array of (x, y) position pairs
     */
    protected abstract float[] calculateSlotPositions(int count);

    /**
     * The number of slots.
     */
    public void setCount(int value) {
        if (slotEnabled.length != value) {
            int oldCount = slotEnabled.length;
            slotEnabled = new boolean[value];
            slotDisplay = new float[value];
            slotPositions = calculateSlotPositions(value);
            onCountChanged.emit(new CountChangedEvent(value, oldCount));
        }
    }

    /**
     *
     */
    public int getCount() {
        return slotEnabled.length;
    }

    /**
     *
     * @return the number of remaining slots that are enabled
     */
    public int getSlotsRemaining() {
        int count = 0;
        for (boolean b : slotEnabled) {
            if (b) {
                count++;
            }
        }
        return count;
    }

    /**
     * @return the size of each slot
     */
    public Vector2 getSlotSize() {
        return DEFAULT_SLOT_SIZE;
    }

    /**
     * Reset {@link SlotHelper#slotEnabled} and {@link SlotHelper#slotDisplay} values.
     */
    public void resetSlots() {
        Arrays.fill(slotEnabled, true);
        Arrays.fill(slotDisplay, -1f);
    }

    /**
     * Spend a slot, returning true if the slot was spent and
     * false if there were no slots left to spend.
     *
     * @return true if a slot was spent, false if there were none left
     */
    public boolean spendSlot() {
        for (int i = getCount() - 1; i >= 0; i--) {
            if (slotEnabled[i]) {
                slotEnabled[i] = false;
                slotDisplay[i] = 0.4f;
                onSlotSpend(i);
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    public void update() {
        final int count = getCount();
        final float dt = Gdx.graphics.getDeltaTime();
        for (int i = 0; i < count; i++) {
            if (slotDisplay[i] > 0f) {
                slotDisplay[i] = Math.max(slotDisplay[i] - dt, 0f);
            }
        }
    }

    /**
     * Render a tip near the mouse with the given header and body,
     * adjusting for left and right side of the screen.
     * @param header Tip header
     * @param body Tip body
     */
    public static void renderTip(String header, String body) {
        final float mx = (float) InputHelper.mX;
        final float tipY = (float) InputHelper.mY - (50f * Settings.scale);
        if (mx < 1400.0F * Settings.scale) {
            TipHelper.renderGenericTip(mx + 60.0F * Settings.scale, tipY, header, body);
        } else {
            TipHelper.renderGenericTip(mx - 350.0F * Settings.scale, tipY, header, body);
        }
    }

    /**
     * Called when a slot is spent.
     * @param index slot index
     */
    protected void onSlotSpend(int index) {}

    public void render(@NotNull SpriteBatch sb) {
        Vector2 origin = new Vector2(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY);
        final int count = getCount();
        for (int i = 0; i < count; i++) {
            Vector2 orbPos = new Vector2(slotPositions[i * 2], slotPositions[i * 2 + 1]).add(origin);
            renderSlot(sb, orbPos, i);
        }
        // Reset color
        sb.setColor(1f, 1f, 1f, 1f);
    }

    protected void renderSlot(@NotNull SpriteBatch sb, @NotNull Vector2 pos, int index) {
        final float displayValue = slotDisplay[index];
        Vector2 sz = getSlotSize();

        // Draw basic texture
        TextureRegion tex0 = displayValue < 0f ? texBright : texDark;
        sb.setColor(1f, 1f, 1f, 0.9f);
        drawCenteredTex(sb, tex0, pos, sz);

        // Draw overlay effect texture
        if (displayValue > 0f) {
            float alpha;
            if (displayValue < 0.2f) {
                alpha = Interpolation.linear.apply(0.0f, 0.95f, displayValue / 0.2f);
            } else {
                alpha = Interpolation.linear.apply(0.0f, 0.95f, (displayValue - 0.2f) / 0.2f);
            }
            sb.setColor(1f, 1f, 1f, alpha);
            drawCenteredTex(sb, texOverlay, pos, sz);
        }
    }

    public static void drawCenteredTex(
            @NotNull SpriteBatch sb, @NotNull TextureRegion tex, @NotNull Vector2 pos, @NotNull Vector2 size
    ) {
        final float scale = Settings.scale;
        sb.draw(tex, pos.x, pos.y, size.x/2f, size.y/2f, size.x, size.y, scale, scale, 0f);
    }

    @Data
    public static class CountChangedEvent {
        private final int count;
        private final int oldCount;
    }
}
