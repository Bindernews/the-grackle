package io.bindernews.thegrackle.cards;

import io.bindernews.thegrackle.Const;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import static io.bindernews.thegrackle.Const.GK_ID;

/**
 * Card configuration class. Reduces the repetitiveness of creating a card.
 */
public class CardConfig {
    public final String name;
    public final String ID;
    public final String image;
    public final CardStrings strings;
    public final int cost;

    public CardConfig(String name, int cost) {
        this.name = name;
        ID = GK_ID + ":" + name;
        image = Const.RES_IMAGES + "/card/" + name + ".png";
        strings = CardCrawlGame.languagePack.getCardStrings(ID);
        this.cost = cost;
    }
}
