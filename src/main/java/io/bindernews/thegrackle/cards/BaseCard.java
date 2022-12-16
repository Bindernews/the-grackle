package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardPredicate;
import io.bindernews.bnsts.Lazy;
import io.bindernews.thegrackle.Grackle;
import io.bindernews.thegrackle.GrackleMod;

@AutoAdd.Ignore
public abstract class BaseCard extends CustomCard {
    private static final Lazy<TextureAtlas> cards = Lazy.of(() ->
            new TextureAtlas(Gdx.files.classpath(GrackleMod.CO.RES_IMAGES + "/cards/cards.atlas")));

    /**
     * Used in {@link BaseCard#canUse} as a simple way for subclasses to customize canUse behaviour.
     */
    protected CardPredicate<BaseCard> canUseTest = (c,p,m) -> true;

    public BaseCard(CardConfig opts, CardRarity rarity, CardTarget target) {
        super(opts.ID, opts.getStrings().NAME, new RegionName(""), 1, opts.getStrings().DESCRIPTION,
                opts.type, Grackle.En.COLOR_BLACK, rarity, target);
        opts.loadImages(getCards());
        portrait = opts.getPortrait();
        jokePortrait = opts.getBetaPortrait();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && canUseTest.test(this, p, m);
    }

    public static TextureAtlas getCards() {
        return cards.get();
    }
}
