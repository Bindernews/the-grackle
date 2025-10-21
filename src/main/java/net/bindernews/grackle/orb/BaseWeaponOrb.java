package net.bindernews.grackle.orb;

import basemod.abstracts.CustomOrb;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import lombok.val;
import net.bindernews.grackle.helper.ModInterop;

public abstract class BaseWeaponOrb extends CustomOrb {

    public BaseWeaponOrb(OrbHelper helper, int basePassive) {
        super(helper.getOrbId(),
                helper.getName(), basePassive, basePassive,
                helper.getPassiveDescription(),
                helper.getEvokeDescription(),
                helper.getImagePath());
    }

    @Override
    public AbstractOrb makeCopy() {
        try {
            val newInst = this.getClass().newInstance();
            this.copyStats(newInst);
            return newInst;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void playChannelSFX() {
    }

    public ModInterop getIop() {
        return ModInterop.iop();
    }

    /**
     * Copy the stats from this orb to the <c>dest</c> instance.
     * @param dest an orb instance that can be cast to this subclass's type
     */
    public void copyStats(BaseWeaponOrb dest) {
        dest.evokeAmount = evokeAmount;
        dest.passiveAmount = passiveAmount;
    }
}
