package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.bindernews.bnsts.CardNums;
import lombok.val;

@AutoAdd.Seen
public class Strike_GK extends BaseCard {
    public static final CardConfig CFG = new CardConfig("Strike_GK");
    public static final CardNums NUM = CardNums.builder()
            .cost(1).damage(6).damageUpg(9).build();

    public Strike_GK() {
        super(CFG, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        tags.add(CardTags.STARTER_STRIKE);
        tags.add(CardTags.STRIKE);
        NUM.init(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        val fx = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), fx));
    }

    @Override
    public void upgrade() {
        NUM.upgrade(this, false);
    }
}
