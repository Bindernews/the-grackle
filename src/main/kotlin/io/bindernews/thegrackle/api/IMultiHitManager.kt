package io.bindernews.thegrackle.api

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature

/**
 *
 *
 * Interface that other mods may use to more easily integrate with this mod.
 * Client code should create a default instance so cards can always use the
 * methods even if the Grackle mod is not installed.
 *
 *
 * Example usage:
 * ```
 * // Declare a static instance and initialize it with a default implementation that does nothing.
 * public static IMultiHitManager multiHitManager = new IMultiHitManager() {};
 * public static void initialize() {
 *     // ...
 *     if (BaseMod.hasModId("grackle")) {
 *         // Have to use an inner-class so Java doesn't try to load classes that don't exist.
 *         Runnable r = ()->{ multiHitManager = GrackleMod.getMultiHitManager(); };
 *         r.run();
 *     }
 * }
 * ```
 */
interface IMultiHitManager {
    fun getExtraHits(source: AbstractCreature?, initial: Int): Int {
        return 0
    }

    fun getExtraHitsCard(card: AbstractCard?, initial: Int): Int {
        return 0
    }

    /**
     * Apply a modifier to make the card support multi hit.
     * @param card Card to tag as multi-hit
     */
    fun makeMultiHit(card: AbstractCard?) {}
}