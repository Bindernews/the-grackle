package net.bindernews.grackle.helper;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.Nullable;

public interface PowerUtil {

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
