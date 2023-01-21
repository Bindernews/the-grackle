package io.bindernews.thegrackle.vfx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect
import io.bindernews.thegrackle.helper.ModInterop.iop

class UpgradeRandomCardEffect(cards: CardGroup, private val isPlayer: Boolean = true) : AbstractGameEffect() {
    private val group: CardGroup = CardGroup(CardGroupType.UNSPECIFIED)
    private var showEffect: ShowCardBrieflyEffect? = null

    init {
        group.group.addAll(cards.group)
        group.group.removeIf { !it.canUpgrade() }
        group.shuffle()
    }

    constructor(owner: AbstractCreature) : this(
        iop().getMasterDeck(owner) ?: CardGroup(CardGroupType.UNSPECIFIED)
    )

    override fun update() {
        if (showEffect == null && group.size() > 0) {
            val card0 = group.group[0]
            card0.upgrade()
            card0.superFlash()
            if (isPlayer) {
                AbstractDungeon.player.bottledCardUpgradeCheck(card0)
            }
            showEffect = ShowCardBrieflyEffect(card0.makeStatEquivalentCopy())
            val fxList = AbstractDungeon.combatRewardScreen.effects
            fxList.add(showEffect)
            fxList.add(UpgradeShineEffect(Settings.WIDTH.toFloat() / 2.0f, Settings.HEIGHT.toFloat() / 2.0f))
        }
        isDone = showEffect?.isDone ?: true
    }

    override fun render(sb: SpriteBatch) {}
    override fun dispose() {}


}