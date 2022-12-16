package io.bindernews.thegrackle.power;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.bnsts.Lazy;
import io.bindernews.thegrackle.GrackleMod;

public abstract class BasePower extends AbstractPower implements CloneablePowerInterface {

    public static final Lazy<TextureAtlas> powerAtlas = GrackleMod.lazyAtlas("/powers/powers.atlas");

    public final PowerStrings strings;

    public BasePower(String id) {
        ID = id;
        strings = CardCrawlGame.languagePack.getPowerStrings(ID);
        name = strings.NAME;
    }

    protected void setOwnerAmount(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
    }

    /**
     * Convenience method to use the indexed {@code DESCRIPTIONS} string
     * in {@link String#format}, passing the rest of the arguments.
     * @param index Index of the description string
     * @param args Format arguments
     * @return Formatted string
     */
    public String formatDesc(int index, Object... args) {
        return String.format(strings.DESCRIPTIONS[index], args);
    }
}
