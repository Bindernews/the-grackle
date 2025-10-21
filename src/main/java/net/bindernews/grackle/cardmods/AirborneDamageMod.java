package net.bindernews.grackle.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import lombok.Getter;
import lombok.val;
import net.bindernews.grackle.helper.ModInterop;

/**
 * When added to a card, this makes it so that the card can only be played the target
 * has the listed power.
 */
public class AirborneDamageMod extends AbstractCardModifier {
    @Getter
    private final int baseDamage;
    @Getter
    private final float multDamage;

    public AirborneDamageMod(int baseDamage, float multDamage) {
        this.baseDamage = baseDamage;
        this.multDamage = multDamage;
    }

    public boolean canPlayCard(AbstractCard card) {
        val owner = ModInterop.iop().getCardOwner(card);
        if (owner == null) {
            return false;
        }
        return true;
//        ModInterop.iop().c
//        val stanceId = iop.getCardOwner(card)
//                ?.let { iop.getStance(it)?.ID } ?: NeutralStance.STANCE_ID
//        return if (stanceId == NeutralStance.STANCE_ID) {
//            card.cantUseMessage = GrackleMod.miscUI["must_be_stance_err"]
//            false
//        } else {
//            true
//        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AirborneDamageMod(baseDamage, multDamage);
    }
//
//    override fun modifyDescription(rawDescription: String, card: AbstractCard): String {
//        val mustBe = GrackleMod.miscUI["must_be_stance"]
//        return String.format("%s NL %s", mustBe, rawDescription)
//    }
}
