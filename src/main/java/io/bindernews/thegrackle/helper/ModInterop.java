package io.bindernews.thegrackle.helper;

import basemod.BaseMod;
import charbosses.actions.common.EnemyGainEnergyAction;
import charbosses.actions.common.EnemyMakeTempCardInDiscardAction;
import charbosses.actions.unique.EnemyChangeStanceAction;
import charbosses.bosses.AbstractCharBoss;
import charbosses.cards.AbstractBossCard;
import charbosses.powers.general.EnemyDrawCardNextTurnPower;
import charbosses.powers.general.EnemyVigorPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import io.bindernews.bnsts.Lazy;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType;

/**
 * Provides safe interoperability with one or more optional dependency mods
 * (e.g. downfall mod).
 */
@Log4j2
public class ModInterop {

    protected ModInterop next;

    private static final Lazy<ModInterop> inst = Lazy.of(() -> {
        Function<ModInterop, ModInterop> chain = (iop) -> iop;
        if (BaseMod.hasModID("downfall")) {
            // Use a dedicated lambda, so it doesn't load DownfallInterop unless the lambda is run
            chain = chain.andThen((iop) -> {
                val v = new DownfallInterop();
                v.next = iop;
                return v;
            });
        }
        return chain.apply(new ModInterop());
    });

    public AbstractStance getStance(AbstractCreature c) {
        if (c instanceof AbstractPlayer) {
            return ((AbstractPlayer) c).stance;
        }
        return null;
    }

    public AbstractGameAction changeStance(AbstractCreature c, String stanceId) {
        if (c instanceof AbstractPlayer) {
            return new ChangeStanceAction(stanceId);
        }
        return null;
    }

    public AbstractGameAction damageAllEnemies(AbstractCreature c, int[] amount, DamageType type, AttackEffect fx) {
        if (c instanceof AbstractPlayer) {
            return new DamageAllEnemiesAction(c, amount, type, fx);
        }
        return new DamageAction(AbstractDungeon.player, new DamageInfo(c, amount[0], type), fx);
    }

    public CardGroup[] getCards(AbstractCreature c) {
        if (c instanceof AbstractPlayer) {
            val p = (AbstractPlayer) c;
            return new CardGroup[]{p.hand, p.drawPile, p.discardPile, p.exhaustPile, p.limbo, p.masterDeck};
        }
        return new CardGroup[]{};
    }

    public Optional<CardGroup> getCardsByType(AbstractCreature c, CardGroup.CardGroupType type) {
        return Arrays.stream(getCards(c)).filter(g -> g.type == type).findFirst();
    }

    /**
     * Returns the owner of the card, normally the player.
     * @param card Card to check
     * @return Card owner
     */
    public AbstractCreature getCardOwner(AbstractCard card) {
        return AbstractDungeon.player;
    }

    @Nullable
    public Class<? extends AbstractPower> getPowerClass(AbstractCreature c, String powerId) {
        if (c instanceof AbstractPlayer) {
            return BaseMod.getPowerClass(powerId);
        }
        return null;
    }


    public AbstractPower createPower(String powerId, AbstractCreature owner, int amount) {
        val clz = getPowerClass(owner, powerId);
        if (clz == null) {
            return null;
        } else {
            return instantiatePower(clz, owner, amount);
        }
    }


    public AbstractGameAction actionMakeTempCardInDiscard(AbstractCreature c, AbstractCard card, int amount) {
        if (c instanceof AbstractPlayer) {
            return new MakeTempCardInDiscardAction(card, amount);
        }
        return null;
    }

    public AbstractGameAction actionGainEnergy(AbstractCreature c, int amount) {
        if (c instanceof AbstractPlayer) {
            return new GainEnergyAction(amount);
        }
        return null;
    }

    public static ModInterop get() {
        return inst.get();
    }


    /**
     * Convenience function for static imports.
     * @return the {@link ModInterop} instance
     */
    public static ModInterop iop() {
        return inst.get();
    }


    /**
     * {@see ConsoleTargetedPower#instantiatePower}
     */
    public static AbstractPower instantiatePower(
            Class<? extends AbstractPower> powerClass,
            AbstractCreature owner,
            int amount
    ) {
        try {
            return powerClass.getConstructor(AbstractCreature.class, Integer.TYPE).newInstance(owner, amount);
        } catch (Exception ignored) {}
        try {
            return powerClass.getConstructor(AbstractCreature.class).newInstance(owner);
        } catch (Exception ignored) {}
        try {
            return powerClass.getConstructor(AbstractCreature.class, Integer.TYPE, Boolean.TYPE).newInstance(owner, amount, false);
        } catch (Exception ignored) {}
        try {
            return powerClass.getConstructor(AbstractCreature.class, AbstractCreature.class, Integer.TYPE).newInstance(owner, AbstractDungeon.player, amount);
        } catch (Exception ignored) {}
        log.info("Failed to instantiate " + powerClass);
        return null;
    }

    public static class DownfallInterop extends ModInterop {

        private final HashMap<String, Class<?>> powerReplacements = new HashMap<>();

        public DownfallInterop() {
            initPowerReplacements();
        }


        @Override
        public AbstractStance getStance(AbstractCreature c) {
            if (c instanceof AbstractCharBoss) {
                return ((AbstractCharBoss) c).stance;
            }
            return next.getStance(c);
        }

        @Override
        public AbstractGameAction changeStance(AbstractCreature c, String stanceId) {
            if (c instanceof AbstractCharBoss) {
                return new EnemyChangeStanceAction(stanceId);
            }
            return next.changeStance(c, stanceId);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<? extends AbstractPower> getPowerClass(AbstractCreature c, String powerId) {
            if (c instanceof AbstractCharBoss) {
                val altClz = powerReplacements.get(powerId);
                if (altClz != null) {
                    return (Class<? extends AbstractPower>) altClz;
                }
            }
            return next.getPowerClass(c, powerId);
        }

        @Override
        public AbstractCreature getCardOwner(AbstractCard card) {
            if (card instanceof AbstractBossCard) {
                return ((AbstractBossCard) card).owner;
            }
            return next.getCardOwner(card);
        }

        @Override
        public AbstractGameAction actionMakeTempCardInDiscard(AbstractCreature c, AbstractCard card, int amount) {
            if (c instanceof AbstractCharBoss) {
                return new EnemyMakeTempCardInDiscardAction(card, amount);
            }
            return next.actionMakeTempCardInDiscard(c, card, amount);
        }

        @Override
        public AbstractGameAction actionGainEnergy(AbstractCreature c, int amount) {
            if (c instanceof AbstractCharBoss) {
                return new EnemyGainEnergyAction((AbstractCharBoss) c, amount);
            }
            return next.actionGainEnergy(c, amount);
        }

        private void initPowerReplacements() {
            val m = powerReplacements;
            m.put("Draw Card", EnemyDrawCardNextTurnPower.class);
            m.put("Vigor", EnemyVigorPower.class);
        }
    }
}
