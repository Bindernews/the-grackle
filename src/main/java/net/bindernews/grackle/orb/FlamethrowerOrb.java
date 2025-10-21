package net.bindernews.grackle.orb;

import net.bindernews.grackle.GrackleMod;

public class FlamethrowerOrb extends BaseWeaponOrb {
    public static final String ID = GrackleMod.makeId(FlamethrowerOrb.class);
    private static final OrbHelper HELPER = new OrbHelper(ID);

    public FlamethrowerOrb() {
        super(HELPER, 2);
    }

    @Override
    public void onEvoke() {

    }
}
