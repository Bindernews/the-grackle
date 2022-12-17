package io.bindernews.thegrackle.helper;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.DaggerSpray;
import com.megacrit.cardcrawl.cards.green.Eviscerate;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import io.bindernews.thegrackle.MiscUtil;
import lombok.val;

import java.util.HashMap;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

/**
 * Manager for extra multi-hit effect. May be discarded later, not sure yet.
 */
public class MultiHitManager {
    private static MultiHitManager instance;

    private final HashMap<Class<?>, HitHandler> hitRegistry = new HashMap<>();

    private MultiHitManager() {
        initRegistry();
    }

    private void initRegistry() {
        val r = hitRegistry;
        r.put(AbstractCard.class, (c,x,p,m) -> {});
        r.put(Eviscerate.class, simpleDamageAction(AttackEffect.SLASH_HEAVY));
        r.put(DaggerSpray.class, MultiHitManager::handlerDaggerSpray);
    }

    public void handleCardUse(AbstractCard card, int extraHits, AbstractPlayer p, AbstractMonster m) {
        val handler = getHandlerForCard(card);
        handler.accept(card, extraHits, p, m);
    }

    public HitHandler getHandlerForCard(AbstractCard card) {
        Class<?> c = card.getClass();
        // Fast path when we have the handler cached
        val handler0 = hitRegistry.get(c);
        if (handler0 != null) {
            return handler0;
        }
        // Handler is not cached, so we add cache entry
        val baseClass = c;
        while (c != null && c != AbstractCard.class) {
            val handler = hitRegistry.get(c);
            if (handler != null) {
                hitRegistry.put(baseClass, handler);
                return handler;
            }
            c = c.getSuperclass();
        }
        throw new RuntimeException("Class " + card.getClass() + " has no multi-hit handler");
    }


    public static MultiHitManager inst() {
        if (instance == null) {
            instance = new MultiHitManager();
        }
        return instance;
    }

    public static void handlerDaggerSpray(AbstractCard card, int extra, AbstractPlayer p, AbstractMonster m) {
        val flipVfx = AbstractDungeon.getMonsters().shouldFlipVfx();
        for (int i = 0; i < extra; i++) {
            MiscUtil.addToBot(new VFXAction(new DaggerSprayEffect(flipVfx), 0.0F));
            MiscUtil.addToBot(new DamageAllEnemiesAction(p, card.multiDamage, card.damageTypeForTurn, AttackEffect.NONE));
        }
    }

    public static HitHandler simpleDamageAction(final AttackEffect effect) {
        return (card, extra, p, m) -> {
            for (int i = 0; i < extra; i++) {
                MiscUtil.addToBot(new DamageAction(m, new DamageInfo(p, card.damage, card.damageTypeForTurn), effect));
            }
        };
    }
}
