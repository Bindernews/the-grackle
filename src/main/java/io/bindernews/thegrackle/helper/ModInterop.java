package io.bindernews.thegrackle.helper;

import basemod.BaseMod;
import charbosses.actions.unique.EnemyChangeStanceAction;
import charbosses.bosses.AbstractCharBoss;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.AbstractStance;
import io.bindernews.bnsts.Lazy;
import lombok.val;

import java.util.function.Function;

/**
 * Provides safe interoperability with one or more optional dependency mods
 * (e.g. downfall mod).
 */
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

    public AbstractGameAction changeStance(AbstractCreature c, AbstractStance stance) {
        if (c instanceof AbstractPlayer) {
            return new ChangeStanceAction(stance);
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

    public static class DownfallInterop extends ModInterop {
        @Override
        public AbstractStance getStance(AbstractCreature c) {
            if (c instanceof AbstractCharBoss) {
                return ((AbstractCharBoss) c).stance;
            }
            return next.getStance(c);
        }

        @Override
        public AbstractGameAction changeStance(AbstractCreature c, AbstractStance stance) {
            if (c instanceof AbstractCharBoss) {
                return new EnemyChangeStanceAction(stance.ID);
            }
            return next.changeStance(c, stance);
        }
    }
}
