package io.bindernews.thegrackle.helper;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import io.bindernews.thegrackle.GrackleMod;
import lombok.val;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

public abstract class ExtraHitModifier extends AbstractDamageModifier {
    public AttackEffect attackEffect;
    public boolean applied;
    public int extraHits = 0;

    public boolean checkApplied() {
        boolean b = applied;
        applied = true;
        return b;
    }

    public static class SimpleExtraHits extends ExtraHitModifier {

        public SimpleExtraHits(AttackEffect effect) {
            this.attackEffect = effect;
        }

        @Override
        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
            if (checkApplied()) {
                return;
            }
            for (int i = 0; i < extraHits; i++) {
                addToBot(new DamageAction(target, info, attackEffect));
            }
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new SimpleExtraHits(attackEffect);
        }
    }

    public static class SwordBoomerangMod extends ExtraHitModifier {
        private AbstractCard card;

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
            this.card = card;
            this.applied = false;
            this.extraHits = GrackleMod.multiHitManager.getExtraHitsPlayer(card, 4);
            return damage;
        }

        @Override
        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
            if (checkApplied()) {
                return;
            }
            for (int i = 0; i < extraHits; i++) {
                this.addToBot(new AttackDamageRandomEnemyAction(card, AttackEffect.SLASH_HORIZONTAL));
            }
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new SwordBoomerangMod();
        }
    }

    public static class DaggerSprayMod extends ExtraHitModifier {

        @Override
        public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
            val flipVfx = AbstractDungeon.getMonsters().shouldFlipVfx();
            val extraHits = GrackleMod.multiHitManager.getExtraHitsPlayer(card, 0);
            val p = AbstractDungeon.player;
            for (int i = 0; i < extraHits; i++) {
                addToBot(new VFXAction(new DaggerSprayEffect(flipVfx), 0.0F));
                addToBot(new DamageAllEnemiesAction(p, card.multiDamage, card.damageTypeForTurn, AttackEffect.NONE));
            }
            return damage;
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new DaggerSprayMod();
        }
    }
}
