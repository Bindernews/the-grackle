package io.bindernews.thegrackle.icons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.bindernews.bnsts.Lazy;
import io.bindernews.thegrackle.GrackleMod;

public class IconHelper {
    public static final Lazy<TextureAtlas> atlas = GrackleMod.lazyAtlas("/icons.atlas");

    public static TextureAtlas.AtlasRegion findRegion(String name) {
        return atlas.get().findRegion(name);
    }
}
