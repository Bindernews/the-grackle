package io.bindernews.thegrackle.icons;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import io.bindernews.bnsts.Lazy;
import io.bindernews.thegrackle.GrackleMod;

public class MusicNoteIcon extends AbstractCustomIcon {
    public static final String ID = GrackleMod.makeId("MusicNote");

    public static final Lazy<MusicNoteIcon> inst = Lazy.of(MusicNoteIcon::new);

    public MusicNoteIcon() {
        super(ID, IconHelper.findRegion("musical_note"));
    }
}
