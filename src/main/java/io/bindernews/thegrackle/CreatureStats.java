package io.bindernews.thegrackle;

import basemod.BaseMod;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class CreatureStats {

    public static Manager mgr = new Manager();

    /**
     * ID of creature these stats track.
     */
    public String id;

    /**
     * Total amount of Fireheart gained this combat.
     */
    public int fireheartGained;

    public boolean isPlayer;

    public CreatureStats(String id) {
        this.id = id;
        fireheartGained = 0;
        isPlayer = false;
    }

    public void onBattleStart() {
        fireheartGained = 0;
    }

    public static class Manager implements
            OnStartBattleSubscriber, PostBattleSubscriber, StartGameSubscriber
    {
        private final HashMap<String, CreatureStats> stats = new HashMap<>();

        public Manager() {
            BaseMod.subscribe(this);
        }

        @Override
        public void receiveOnBattleStart(AbstractRoom room) {
            // Add all monsters and reset
            for (AbstractMonster m : room.monsters.monsters) {
                get(m);
            }
            for (CreatureStats st : stats.values()) {
                st.onBattleStart();
            }
        }

        @Override
        public void receivePostBattle(AbstractRoom room) {
            // Remove all non-player stats
            ArrayList<CreatureStats> keep = new ArrayList<>();
            for (CreatureStats st : stats.values()) {
                if (st.isPlayer) {
                    keep.add(st);
                }
            }
            stats.clear();
            for (CreatureStats st : keep) {
                stats.put(st.id, st);
            }
        }

        @Override
        public void receiveStartGame() {
            // On a new game clear all stats and add the player
            stats.clear();
            get(AbstractDungeon.player).isPlayer = true;
        }

        @NotNull
        public CreatureStats get(AbstractCreature c) {
            return stats.computeIfAbsent(c.id, CreatureStats::new);
        }
    }
}
