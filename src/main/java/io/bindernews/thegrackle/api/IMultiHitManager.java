package io.bindernews.thegrackle.api;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import io.bindernews.thegrackle.helper.MultiHitManager;

/**
 * <p>
 * Interface that other mods may use to more easily integrate with {@link MultiHitManager}.
 * Client code should create a default instance so cards can always use the methods even if the Grackle
 * mod is not installed.
 * </p>
 *
 * Example usage:
 * <pre>
 * // Declare a static instance and initialize it with a default implementation that does nothing.
 * public static IMultiHitManager multiHitManager = new IMultiHitManager() {};
 * public static void initialize() {
 *     // ...
 *     if (BaseMod.hasModId("grackle")) {
 *         // Have to use an inner-class so Java doesn't try to load classes that don't exist.
 *         Runnable r = ()->{
 *             multiHitManager = GrackleMod.multiHitManager;
 *         };
 *         r.run();
 *     }
 * }
 * </pre>
 */
public interface IMultiHitManager {
    default int getExtraHits(AbstractCreature source, int initial) { return 0; }

    default int getExtraHitsPlayer(AbstractCard card, int initial) { return 0; }

    default AbstractCard.CardTags getMultiHitTag() { return null; }

    /**
     * Convenience method to tag the given card as supporting multi-hit.
     * @param card Card to tag as multi-hit
     */
    default void tagMultiHit(AbstractCard card) {}
}
