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
    private static final IField<AbstractRelic, RelicStrings> fRelicStrings =
            IField.unreflect(AbstractRelic.class, "relicStrings");


    public BaseRelic(String id, RelicTier tier, LandingSound sfx) {
        super(id, "", tier, sfx);
        loadImages(GrackleMod.MOD_RES + "/images/relics/" + GrackleMod.removePrefix(id));
    }

    public void loadImages(String path) {
        val tex = Objects.requireNonNull(GrackleMod.loadTexture(path + ".png"));
        Texture texOutline = GrackleMod.loadTexture(path + "_o.png");
        if (texOutline == null) {
            texOutline = tex;
        }
        setTextureOutline(tex, texOutline);
        largeImg = tex;
    }

    public RelicStrings getStrings() { return fRelicStrings.get(this); }
}
