package io.bindernews.thegrackle.icons;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import io.bindernews.thegrackle.GrackleMod;
import lombok.Getter;

public class MusicNoteIcon extends AbstractCustomIcon {
    public static final String ID = GrackleMod.makeId("MusicNote");

    @Getter(lazy = true) private static final MusicNoteIcon inst = new MusicNoteIcon();

    public MusicNoteIcon() {
        super(ID, IconHelper.findRegion("musical_note"));
    }
}
