package io.bindernews.thegrackle.power;

import basemod.AutoAdd;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import io.bindernews.bnsts.Lazy;
import io.bindernews.thegrackle.GrackleMod;
import lombok.val;

@AutoAdd.Ignore
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

    @Override
    protected void loadRegion(String fileName) {
        val k48 = "48/" + fileName;
        val k128 = "128/" + fileName;

        region48 = powerAtlas.get().findRegion(k48);
        if (region48 == null) {
            region48 = AbstractPower.atlas.findRegion(k48);
        }
        region128 = powerAtlas.get().findRegion(k128);
        if (region128 == null) {
            region128 = AbstractPower.atlas.findRegion(k128);
        }
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

    @Override
    public AbstractPower makeCopy() {
        try {
            return getClass().getConstructor(AbstractCreature.class, int.class).newInstance(owner, amount);
        } catch (Exception ex) {
            throw new UnsupportedOperationException("cannot automatically clone power");
        }
    }
}
