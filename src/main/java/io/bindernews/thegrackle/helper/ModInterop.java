package io.bindernews.thegrackle.helper;

import basemod.BaseMod;
import charbosses.actions.unique.EnemyChangeStanceAction;
import charbosses.bosses.AbstractCharBoss;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.AbstractStance;
import io.bindernews.bnsts.Lazy;
import lombok.val;

import java.util.function.Function;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType;

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
        public AbstractGameAction changeStance(AbstractCreature c, String stanceId) {
            if (c instanceof AbstractCharBoss) {
                return new EnemyChangeStanceAction(stanceId);
            }
            return next.changeStance(c, stanceId);
        }
    }
}
