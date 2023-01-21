package io.bindernews.thegrackle.relics;

import io.bindernews.thegrackle.GrackleMod;

public class PhoenixIdol extends BaseRelic {
    public static final String ID = GrackleMod.makeId("PhoenixIdol");

    public PhoenixIdol() {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onLoseHp(int damageAmount) {
        super.onLoseHp(damageAmount);
    }
}
