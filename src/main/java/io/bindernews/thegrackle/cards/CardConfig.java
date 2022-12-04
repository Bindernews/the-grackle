package io.bindernews.thegrackle.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import io.bindernews.thegrackle.Const;

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
        image = Const.MOD_ID + "/cards/" + name;
        strings = CardCrawlGame.languagePack.getCardStrings(ID);
        this.cost = cost;
    }
}
