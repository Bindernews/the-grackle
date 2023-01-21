package io.bindernews.thegrackle.relics;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import io.bindernews.thegrackle.GrackleMod;
import io.bindernews.thegrackle.stance.StanceAloft;

public class LoftwingFeather extends BaseRelic {
    public static final String ID = GrackleMod.makeId(LoftwingFeather.class);

    public LoftwingFeather() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT);
    }


    @Override
    public void onPlayerEndTurn() {
        addToBot(new ChangeStanceAction(StanceAloft.STANCE_ID));
    }
}
