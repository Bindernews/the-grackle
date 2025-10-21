package net.bindernews.grackle.orb;

import basemod.abstracts.CustomOrb;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class OrbHelper {
    @Getter
    private final String orbId;
    private OrbStrings strings = null;

    public OrbHelper(String orbId) {
        this.orbId = orbId;
    }

    @Nullable
    public String getImagePath() {
        return null;
    }

    public String getName() {
        return getOrbStrings().NAME;
    }

    public String getPassiveDescription() {
        return getOrbStrings().DESCRIPTION[0];
    }

    public String getEvokeDescription() {
        return getOrbStrings().DESCRIPTION[1];
    }

    public OrbStrings getOrbStrings() {
        if (strings == null) {
            strings = CardCrawlGame.languagePack.getOrbString(this.orbId);
        }
        return strings;
    }

    public void flashOrbVfx(AbstractOrb orb, float baseSpeed) {
        float speedTime = baseSpeed / (float)AbstractDungeon.player.orbs.size();
        if (Settings.FAST_MODE) {
            speedTime = 0.0F;
        }
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(orb, OrbFlareEffect.OrbFlareColor.DARK), speedTime));
    }

    public void simpleEndOfTurn(CustomOrb orb) {
        float speedTime = 0.6F / (float) AbstractDungeon.player.orbs.size();
        if (Settings.FAST_MODE) {
            speedTime = 0.0F;
        }

        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(orb, OrbFlareEffect.OrbFlareColor.DARK), speedTime));
        orb.evokeAmount += orb.passiveAmount;
        orb.updateDescription();
    }

}
