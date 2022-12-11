package io.bindernews.thegrackle.cards;

import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import io.bindernews.bnsts.Lazy;
import io.bindernews.thegrackle.Const;

/**
 * Card configuration class. Reduces the repetitiveness of creating a card.
 */
public class CardConfig {
    public final String name;
    public final String ID;
    public final String image;
    private final Lazy<CardStrings> strings;

    public CardConfig(String name) {
        this.name = name;
        ID = Const.MOD_ID + ":" + name;
        image = Const.MOD_ID + "/cards/" + name;
        strings = Lazy.of(() -> CardCrawlGame.languagePack.getCardStrings(ID));
    }

    public CardStrings getStrings() {
        return strings.get();
    }

    public String getFlavorText() {
        return FlavorText.CardStringsFlavorField.flavor.get(getStrings());
    }
}
