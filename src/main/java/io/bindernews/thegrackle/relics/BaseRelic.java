package io.bindernews.thegrackle.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import io.bindernews.bnsts.IField;
import io.bindernews.thegrackle.GrackleMod;
import lombok.val;

import java.util.Objects;

public abstract class BaseRelic extends CustomRelic {
    public static final IField<AbstractRelic, RelicStrings> fRelicStrings =
            IField.unreflect(AbstractRelic.class, "relicStrings");

    public BaseRelic(String id, RelicTier tier, LandingSound sfx) {
        super(id, "", tier, sfx);
        loadImages(this);
    }

    public static void loadImages(AbstractRelic relic) {
        val path = GrackleMod.MOD_RES + "/images/relics/" + GrackleMod.removePrefix(relic.relicId);
        val tex = Objects.requireNonNull(GrackleMod.loadTexture(path + ".png"));
        Texture texOutline = GrackleMod.loadTexture(path + "_o.png");
        if (texOutline == null) {
            texOutline = tex;
        }
        relic.img = tex;
        relic.outlineImg = texOutline;
        relic.largeImg = tex;
    }

    public RelicStrings getStrings() {
        return fRelicStrings.get(this);
    }
}
