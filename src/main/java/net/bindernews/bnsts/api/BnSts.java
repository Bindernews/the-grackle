package net.bindernews.bnsts.api;

import net.bindernews.bnsts.hook.BnStsMain;
import net.bindernews.eventbus.IEventEmit;

import java.util.SortedSet;

public interface BnSts {
    /**
     * Set of popup instances, ordered by priority.
     */
    SortedSet<IPopup> getPopups();

    /**
     * Event for when {@link com.megacrit.cardcrawl.screens.SingleCardViewPopup] changes cards.
     */
    IEventEmit<ScvChangeCardEvent> getSingleCardViewChanged();

    static BnSts getInstance() {
        return BnStsMain.INSTANCE;
    }
}
