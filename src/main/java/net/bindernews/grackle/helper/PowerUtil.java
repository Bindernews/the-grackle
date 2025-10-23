package net.bindernews.grackle.helper;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PowerUtil {

    /**
     * Should be called in {@link AbstractPower#atEndOfRound()} for turn-based powers to reduce or remove
     * themselves from their "owner".
     * @param power power to reduce
     * @param amount amount to reduce by
     */
    static void endOfRoundReduceOrRemove(@NotNull AbstractPower power, int amount) {
        final AbstractCreature owner = power.owner;
        if (power.amount <= 0) {
            MiscUtil.addToBot(new RemoveSpecificPowerAction(owner, owner, power.ID));
        } else {
            MiscUtil.addToBot(new ReducePowerAction(owner, owner, power.ID, amount));
        }
    }

    static boolean isStartOfMonsterTurn(@Nullable AbstractCreature owner) {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT
                || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return false;
        }
        if (owner == null) {
            return true;
        }
        // owner != null, so make sure it's not a player
        return !(owner instanceof AbstractPlayer);
    }

    static boolean isEndOfPlayerTurn(@Nullable AbstractCreature owner, boolean isPlayer) {
        return (owner == null || owner instanceof AbstractPlayer) && isPlayer;
    }
}
