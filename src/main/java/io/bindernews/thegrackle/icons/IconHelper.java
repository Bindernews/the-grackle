package io.bindernews.thegrackle.icons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import io.bindernews.bnsts.Lazy;
import io.bindernews.thegrackle.Const;
import lombok.val;

public class IconHelper {
    public static final Lazy<TextureAtlas> atlas = Lazy.of(() ->
            new TextureAtlas(Gdx.files.classpath(Const.RES_IMAGES + "/icons.atlas")));

    public static TextureAtlas.AtlasRegion findRegion(String name) {
        return atlas.get().findRegion(name);
    }

    public static void registerAll() {
        val list = new AbstractCustomIcon[] {
                MusicNoteIcon.inst.get()
        };
        for (val icon : list) {
            CustomIconHelper.addCustomIcon(icon);
        }
    }
}
