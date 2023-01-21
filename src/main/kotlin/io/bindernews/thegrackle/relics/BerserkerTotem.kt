package io.bindernews.thegrackle.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;

public class BerserkerTotem extends BaseRelic {
    public static final String ID = GrackleMod.makeId(BerserkerTotem.class);

    public BerserkerTotem() {
        super(ID, RelicTier.COMMON, LandingSound.SOLID);
    }
    
    static {
        ExtraHitsVariable.getOnApplyPowers().on(-4,
                e -> e.addCount(AbstractDungeon.player.hasRelic(ID) ? 1 : 0));
    }
}
