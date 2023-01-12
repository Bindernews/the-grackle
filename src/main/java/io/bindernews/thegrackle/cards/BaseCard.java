package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.*;
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

    protected ICardInitializer cardInitializer;

    public BaseCard(CardConfig opts, ICardInitializer initializer) {
        this(opts);
        this.cardInitializer = initializer;
        this.cardInitializer.init(this);
    }

    public BaseCard(CardConfig opts) {
        super(opts.ID, opts.getStrings().NAME, new RegionName(""), 1, opts.getStrings().DESCRIPTION,
                opts.type, Grackle.En.COLOR_BLACK, opts.rarity, opts.target);
        opts.loadImages(getCards());
        portrait = opts.getPortrait();
        jokePortrait = opts.getBetaPortrait();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && canUseTest.test(this, p, m);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        apply(abstractPlayer, abstractMonster);
    }

    @Override
    public void upgrade() {
        if (cardInitializer != null) {
            cardInitializer.upgrade(this);
        }
    }

    /**
     * A more generic version of {@link BaseCard#use} which will be useful
     * when integrating with the Downfall mod.
     * @param p Player or CharBoss
     * @param m Monster or Player target
     */
    public void apply(AbstractCreature p, AbstractCreature m) {}

    public static TextureAtlas getCards() {
        return cards.get();
    }

    /**
     * Should be used in {@link BaseCard#apply} to indicate that the card is only applicable to players,
     * not downfall enemy bosses.
     */
    public static void throwPlayerOnly() {
        throw new UnsupportedOperationException("FightFire only works with AbstractPlayer");
    }
}
