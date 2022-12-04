package io.bindernews.thegrackle.relics;

import basemod.abstracts.CustomRelic;
import io.bindernews.thegrackle.Const;
import io.bindernews.thegrackle.GrackleMod;

public class PhoenixIdol extends CustomRelic {

    public static final String RELIC_ID = GrackleMod.makeId("PhoenixIdol");
    public static final String IMG = Const.RES_IMAGES + "/relics/PhoenixIdol.png";

    public PhoenixIdol() {
        super(RELIC_ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onLoseHp(int damageAmount) {
        super.onLoseHp(damageAmount);
    }
}
