package net.bindernews.grackle.orb;

import net.bindernews.grackle.GrackleMod;

public class MissileOrb extends BaseWeaponOrb {
    public static final String ID = GrackleMod.makeId(MissileOrb.class);
    private static final OrbHelper HELPER = new OrbHelper(ID);

    public MissileOrb() {
        super(HELPER, 3);
    }

    @Override
    public void onEvoke() {
    }
}
