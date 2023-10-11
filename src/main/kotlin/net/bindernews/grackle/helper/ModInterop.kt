package net.bindernews.grackle.helper

import basemod.BaseMod
import com.evacipated.cardcrawl.modthespire.Loader
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.*
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.EnergyManager
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.eventbus.Lazy
import net.bindernews.grackle.cards.BaseCard
import net.bindernews.grackle.downfall.DownfallInterop
import java.util.*
import java.util.function.Function
import java.util.stream.Stream

/**
 * Provides safe interoperability with one or more optional dependency mods
 * (e.g. downfall mod).
 */
open class ModInterop {
    protected var next: ModInterop? = null

    open fun getStance(c: AbstractCreature): AbstractStance? {
        return if (c is AbstractPlayer) {
            c.stance
        } else null
    }

    open fun changeStance(c: AbstractCreature, stanceId: String): AbstractGameAction? {
        return if (c is AbstractPlayer) {
            ChangeStanceAction(stanceId)
        } else null
    }

    open fun getRelics(c: AbstractCreature): Stream<AbstractRelic> {
        return if (c is AbstractPlayer) {
            c.relics.stream()
        } else Stream.empty()
    }

    fun hasRelic(c: AbstractCreature, relicId: String): Boolean {
        return getRelics(c).anyMatch { r -> r.relicId == relicId }
    }

    fun damageAllEnemies(
        c: AbstractCreature,
        amount: IntArray,
        type: DamageType,
        fx: AttackEffect?
    ): AbstractGameAction {
        return (c as? AbstractPlayer)?.let { DamageAllEnemiesAction(it, amount, type, fx) }
            ?: DamageAction(AbstractDungeon.player, DamageInfo(c, amount[0], type), fx)
    }

    fun getCards(c: AbstractCreature): Array<CardGroup> {
        if (c is AbstractPlayer) {
            return arrayOf(c.hand, c.drawPile, c.discardPile, c.exhaustPile, c.limbo, c.masterDeck)
        }
        return arrayOf()
    }

    fun getMasterDeck(c: AbstractCreature): CardGroup? {
        return if (c is AbstractPlayer) {
            c.masterDeck
        } else null
    }

    fun getCardsByType(c: AbstractCreature, type: CardGroupType): Optional<CardGroup> {
        return Arrays.stream(getCards(c)).filter { g: CardGroup -> g.type == type }.findFirst()
    }

    /**
     * Returns the owner of the card, normally the player.
     * @param card Card to check
     * @return Card owner
     */
    open fun getCardOwner(card: AbstractCard?): AbstractCreature? {
        return if (card is BaseCard) {
            card.owner
        } else AbstractDungeon.player
    }

    /**
     * Returns a stream of all creatures that are "friendly" with `c`.
     */
    fun getFriends(c: AbstractCreature): Stream<out AbstractCreature> {
        return if (c is AbstractPlayer) {
            Stream.of<AbstractCreature>(c)
        } else {
            AbstractDungeon.getMonsters().monsters.stream()
        }
    }

    fun getEnemies(c: AbstractCreature): Stream<out AbstractCreature> {
        return if (c is AbstractPlayer) {
            AbstractDungeon.getMonsters().monsters.stream()
        } else {
            Stream.of(AbstractDungeon.player)
        }
    }

    open fun getPowerClass(c: AbstractCreature, powerId: String): Class<out AbstractPower>? {
        return BaseMod.getPowerClass(powerId)
    }

    fun createPower(powerId: String, owner: AbstractCreature, amount: Int): AbstractPower? {
        val clz = getPowerClass(owner, powerId)
        return if (clz == null) {
            null
        } else {
            instantiatePower(clz, owner, amount)
        }
    }

    open fun actionMakeTempCardInDiscard(c: AbstractCreature?, card: AbstractCard?, amount: Int): AbstractGameAction? {
        return if (c is AbstractPlayer) {
            MakeTempCardInDiscardAction(card, amount)
        } else null
    }

    open fun actionGainEnergy(c: AbstractCreature?, amount: Int): AbstractGameAction? {
        return if (c is AbstractPlayer) {
            GainEnergyAction(amount)
        } else null
    }

    fun actionApplyPower(
        source: AbstractCreature?,
        target: AbstractCreature,
        powerId: String,
        amount: Int
    ): AbstractGameAction {
        return ApplyPowerAction(target, source, createPower(powerId, target, amount), amount)
    }

    /**
     * Returns the energy manager for the given creature.
     */
    open fun getEnergy(c: AbstractCreature): EnergyManager? {
        return if (c is AbstractPlayer) {
            c.energy
        } else null
    }

    companion object {
        private val log = org.apache.logging.log4j.LogManager.getLogger(ModInterop::class.java)

        private val inst = Lazy.of {
            var chain = Function { iop: ModInterop -> iop }
            // Use a dedicated lambda, so it doesn't load DownfallInterop unless the lambda is run
            val downfallLoader = Function { iop: ModInterop ->
                val v = DownfallInterop()
                v.next = iop
                v
            }
            if (Loader.isModLoaded("downfall")) {
                chain = chain.andThen(downfallLoader)
            }
            // Add more loader chains here for other interop
            chain.apply(ModInterop())
        }

        /**
         * Convenience function for static imports.
         * @return the [ModInterop] instance
         */
        @JvmStatic fun iop(): ModInterop {
            return inst.get()
        }

        /**
         * {@see ConsoleTargetedPower#instantiatePower}
         */
        fun instantiatePower(
            powerClass: Class<out AbstractPower>,
            owner: AbstractCreature?,
            amount: Int
        ): AbstractPower? {
            try {
                return powerClass.getConstructor(AbstractCreature::class.java, Integer.TYPE).newInstance(owner, amount)
            } catch (ignored: Exception) {
            }
            try {
                return powerClass.getConstructor(AbstractCreature::class.java).newInstance(owner)
            } catch (ignored: Exception) {
            }
            try {
                return powerClass.getConstructor(AbstractCreature::class.java, Integer.TYPE, java.lang.Boolean.TYPE)
                    .newInstance(owner, amount, false)
            } catch (ignored: Exception) {
            }
            try {
                return powerClass.getConstructor(
                    AbstractCreature::class.java,
                    AbstractCreature::class.java,
                    Integer.TYPE
                ).newInstance(owner, AbstractDungeon.player, amount)
            } catch (ignored: Exception) {
            }
            log.info("Failed to instantiate $powerClass")
            return null
        }
    }
}