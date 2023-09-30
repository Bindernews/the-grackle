package net.bindernews.grackle.api

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.megacrit.cardcrawl.cards.AbstractCard.CardTags

object GCardTags {
    /**
     * Cards which extend [BaseCard] and are tagged with this will have an additional tip
     * indicating the full damage amount.
     */
    @SpireEnum
    lateinit var TAG_DAMAGE_TIP: CardTags
}