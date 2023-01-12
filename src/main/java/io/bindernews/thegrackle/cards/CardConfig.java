package io.bindernews.thegrackle.cards;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import io.bindernews.bnsts.Lazy;
import io.bindernews.thegrackle.GrackleMod;
import lombok.Getter;
import lombok.val;

/**
 * Card configuration class. Reduces the repetitiveness of creating a card.
 */
public class CardConfig {
    public final String name;
    public final String ID;
    public final AbstractCard.CardType type;
    public final AbstractCard.CardRarity rarity;
    public final AbstractCard.CardTarget target;
    private final Lazy<CardStrings> strings;
    @Getter private TextureAtlas.AtlasRegion portrait;
    @Getter private TextureAtlas.AtlasRegion betaPortrait;

    public CardConfig(
            String name,
            AbstractCard.CardType type,
            AbstractCard.CardRarity rarity,
            AbstractCard.CardTarget target
    ) {
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.target = target;
        ID = GrackleMod.MOD_ID + ":" + name;
        strings = Lazy.of(() -> CardCrawlGame.languagePack.getCardStrings(ID));
    }

    public void loadImages(TextureAtlas atlas) {
        if (portrait == null) {
            portrait = findFirstRegion(atlas, makeImagePaths(""));
            betaPortrait = findFirstRegion(atlas, makeImagePaths("_b"));
            if (betaPortrait == null) {
                betaPortrait = portrait;
            }
        }
    }

    private String[] makeImagePaths(String suffix) {
        return new String[]{
                name + suffix,
                typeToName(type) + suffix,
        };
    }
    
    private static TextureAtlas.AtlasRegion findFirstRegion(TextureAtlas atlas, String[] names) {
        for (val name : names) {
            val r = atlas.findRegion(name);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    private static String typeToName(AbstractCard.CardType type) {
        switch (type) {
            case ATTACK:
                return "Attack";
            case SKILL:
                return "Skill";
            case POWER:
                return "Power";
            case STATUS:
                return "Status";
            case CURSE:
                return "Curse";
            default:
                return "";
        }
    }

    public CardStrings getStrings() {
        return strings.get();
    }

    public String getFlavorText() {
        return FlavorText.CardStringsFlavorField.flavor.get(getStrings());
    }
}
