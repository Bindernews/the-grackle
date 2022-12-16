package io.bindernews.thegrackle.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import io.bindernews.thegrackle.GrackleMod;
import lombok.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

public abstract class BaseRelic extends CustomRelic {

    private static final MethodHandle hGetRelicStrings;
    static {
        try {
            val m = AbstractRelic.class.getDeclaredField("relicStrings");
            m.setAccessible(true);
            hGetRelicStrings = MethodHandles.lookup().unreflectGetter(m);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public BaseRelic(String id, RelicTier tier, LandingSound sfx) {
        super(id, "", tier, sfx);
        loadImages(GrackleMod.MOD_RES + "/images/relics/" + GrackleMod.removePrefix(id));
    }

    public void loadImages(String path) {
        val tex = Objects.requireNonNull(GrackleMod.loadTexture(path + ".png"));
        var texOutline = GrackleMod.loadTexture(path + "_o.png");
        if (texOutline == null) {
            texOutline = tex;
        }
        setTextureOutline(tex, texOutline);
        largeImg = tex;
    }

    @SneakyThrows
    public RelicStrings getStrings() {
        return (RelicStrings) hGetRelicStrings.invoke(this);
    }
}
