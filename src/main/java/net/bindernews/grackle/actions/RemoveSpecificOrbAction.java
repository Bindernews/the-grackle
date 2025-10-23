package net.bindernews.grackle.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class RemoveSpecificOrbAction extends AbstractGameAction {
    private final AbstractOrb orb;

    public RemoveSpecificOrbAction(AbstractOrb orb) {
        this.orb = orb;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            removeSpecificOrb(orb);
        }
        tickDuration();
    }

    /**
     * Removes the given orb instance from the player's orb list, moving remaining orbs to the next slot.
     * @param orb the orb to remove
     * @return true on success, false if the orb wasn't found
     */
    public static boolean removeSpecificOrb(@NotNull AbstractOrb orb) {
        final AbstractPlayer p = AbstractDungeon.player;
        final int ix = p.orbs.indexOf(orb);
        if (ix < 0) {
            return false;
        }
        return removeOrbAtIndex(ix);
    }

    /**
     * Remove the orb at slot {@code index} (counting right-to-left) and shift all remaining orbs to the next slot.
     * @param index index of the orb to remove
     * @return true on success, false if there was no orb there or the slot was out of range
     */
    public static boolean removeOrbAtIndex(int index) {
        final AbstractPlayer p = AbstractDungeon.player;
        // We cannot remove an empty or non-existing orb slot.
        if (p.orbs.size() <= index || p.orbs.get(index) instanceof EmptyOrbSlot) {
            return false;
        }
        final int orbCount = p.orbs.size();
        final AbstractOrb orbSlot = new EmptyOrbSlot(p.orbs.get(index).cX, p.orbs.get(index).cY);
        for(int i = index; i < orbCount; ++i) {
            Collections.swap(p.orbs, i, i - 1);
        }
        p.orbs.set(orbCount - 1, orbSlot);
        for(int i = 0; i < orbCount; ++i) {
            p.orbs.get(i).setSlot(i, p.maxOrbs);
        }
        return true;
    }
}
