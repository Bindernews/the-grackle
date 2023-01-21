package io.bindernews.thegrackle.api

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup

data class SvcChangeCardEvent(
    val card: AbstractCard,
    val group: CardGroup,
    val open: Boolean,
)
