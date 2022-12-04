package io.bindernews.thegrackle.cards;

import basemod.AutoAdd;
import io.bindernews.thegrackle.Grackle;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@AutoAdd.Seen
public class Strike_GK extends BaseCard {
    public static final CardConfig C = new CardConfig("Strike", 1);
    public static final int DAMAGE = 6;
    public static final int UPGRADE_PLUS_DAMAGE = 3;

    public Strike_GK() {
        super(C, Grackle.En.COLOR_BLACK, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        tags.add(CardTags.STARTER_STRIKE);
        tags.add(CardTags.STRIKE);
        baseDamage = DAMAGE;
        damage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
        ));
    }

    @Override
    public void onUpgrade() {
        upgradeDamage(UPGRADE_PLUS_DAMAGE);
    }
}
