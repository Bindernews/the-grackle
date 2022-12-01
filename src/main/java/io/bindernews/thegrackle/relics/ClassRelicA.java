package io.bindernews.thegrackle.relics;

import basemod.abstracts.CustomRelic;

public class ClassRelicA extends CustomRelic {
    public static final String ID = "Grackle:TestRelicA";
    public static final String IMG = "grackle/images/relics/testrelica.png";

    public ClassRelicA() {
        super(ID, IMG, RelicTier.COMMON, LandingSound.CLINK);
    }
}
