package io.bindernews.thegrackle.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.TipHelper;
import io.bindernews.thegrackle.GrackleMod;

@SuppressWarnings("unused")
public class TipHelperPatches {

    @SpirePatch(clz = TipHelper.class, method = "render")
    public static class OnRender {
        public static void Postfix(SpriteBatch sb) {
            GrackleMod.onPopupRender.emit(sb);
        }
    }
}
