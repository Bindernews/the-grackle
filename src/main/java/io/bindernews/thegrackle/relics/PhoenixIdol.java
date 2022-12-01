package io.bindernews.thegrackle.relics;

import basemod.abstracts.CustomRelic;
import io.bindernews.thegrackle.Const;

public class PhoenixIdol extends CustomRelic {

    public static final String RELIC_ID = Const.GK_ID + ":PhoenixIdol";
    public static final String IMG = "grackle/images/relics/testrelic.png";

    public PhoenixIdol() {
        super(RELIC_ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onLoseHp(int damageAmount) {
        super.onLoseHp(damageAmount);
    }
}
