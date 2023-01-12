package io.bindernews.bnsts;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.*;
import com.evacipated.cardcrawl.mod.stslib.variables.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.*;
import java.util.ArrayList;
import java.util.function.*;

/**
 * A utility for easily configuring an {@link AbstractCard}'s values.
 * <br/>
 * Calling {@link CardVariables#config} allows users to conveniently create and
 * set values. StSLib's {@link DynamicVariable}s are supported, as well as anything
 * implementing {@link IVariable}, allowing user expansion.
 *
 * @author bindernews
 */
public class CardVariables implements ICardInitializer {

    private final ArrayList<VariableSetting> settings = new ArrayList<>();
    private final ArrayList<ICardInitializer> children = new ArrayList<>();

    public CardVariables() {}

    @NotNull
    public static CardVariables config(Consumer<CardVariables> c) {
        val cv = new CardVariables();
        c.accept(cv);
        return cv;
    }

    public void add(IVariable variable, int value, int valueUpg) {
        settings.add(new VariableSetting(variable, value, valueUpg));
    }

    public void child(ICardInitializer initializer) {
        children.add(initializer);
    }

    public void addModifier(Supplier<AbstractCardModifier> ctor) {
        child(new CardModifierSetup(ctor));
    }

    public void block(int base, int upg) {
        add(vBlock, base, upg);
    }

    public void cost(int base) { cost(base, -1); }
    public void cost(int base, int upg) {
        add(vCost, base, upg);
    }

    public void damage(int base, int upg) {
        add(vDamage, base, upg);
    }

    public void magic(int base, int upg) {
        add(vMagic, base, upg);
    }

    public void multiDamage(boolean base, boolean upg) {
        child(new ICardInitializer() {
            @Override @SneakyThrows public void init(AbstractCard card) {
                hMultiDamage[1].invoke(card, base);
            }
            @Override @SneakyThrows public void upgrade(AbstractCard card) {
                hMultiDamage[1].invoke(card, upg);
            }
        });
    }

    public void exhaust(boolean base) {
        exhaust(base, base);
    }

    public void exhaust(boolean base, boolean upg) {
        child(new LambdaCardInit(
                card -> card.exhaust = base,
                card -> card.exhaust = upg
        ));
    }

    public void onUpgrade(Consumer<AbstractCard> action) {
        child(new LambdaCardInit(x -> {}, action));
    }

    public void onInit(Consumer<AbstractCard> action) {
        child(new LambdaCardInit(action, x -> {}));
    }

    @Override
    public void init(AbstractCard card) {
        for (val s : settings) {
            s.variable.setBaseValue(card, s.value);
        }
        children.forEach(a -> a.init(card));
        card.initializeDescription();
    }

    @Override
    public void upgrade(AbstractCard card) {
        if (!card.upgraded) {
            upgradeName(card);
            forceUpgrade(card);
        }
    }

    public void forceUpgrade(AbstractCard card) {
        for (val s : settings) {
            if (s.valueUpg != -1) {
                s.variable.upgrade(card, s.valueUpg - s.value);
            }
        }
        children.forEach(a -> a.upgrade(card));
    }

    @Data
    static class VariableSetting {
        final IVariable variable;
        final int value;
        final int valueUpg;
    }

    @Data
    public static class CardModifierSetup implements ICardInitializer {
        final Supplier<AbstractCardModifier> ctor;

        @Override
        public void init(AbstractCard card) {
            CardModifierManager.addModifier(card, ctor.get());
        }

        @Override
        public void upgrade(AbstractCard card) {}
    }


    private static final MethodHandle hUpgradeDamage;
    private static final MethodHandle hUpgradeBlock;
    private static final MethodHandle hUpgradeMagic;
    private static final MethodHandle hUpgradeName;
    private static final MethodHandle hUpgradeCost;
    private static final MethodHandle[] hMultiDamage;

    static {
        val mtypeInt = MethodType.methodType(void.class, int.class);
        val mtypeNone = MethodType.methodType(void.class);
        hUpgradeDamage = findCardMethod("upgradeDamage", mtypeInt);
        hUpgradeBlock = findCardMethod("upgradeBlock", mtypeInt);
        hUpgradeMagic = findCardMethod("upgradeMagicNumber", mtypeInt);
        hUpgradeCost = findCardMethod("upgradeBaseCost", mtypeInt);
        hUpgradeName = findCardMethod("upgradeName", mtypeNone);
        hMultiDamage = unreflectField(AbstractCard.class, "multiDamage");
    }

    private static MethodHandle findCardMethod(String name, MethodType args) {
        try {
            val m = AbstractCard.class.getDeclaredMethod(name, args.parameterArray());
            m.setAccessible(true);
            return MethodHandles.lookup().unreflect(m);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static MethodHandle[] unreflectField(Class<?> clz, String name) {
        try {
            val lookup = MethodHandles.lookup();
            val m = clz.getDeclaredField(name);
            m.setAccessible(true);
            return new MethodHandle[] {
                    lookup.unreflectGetter(m),
                    lookup.unreflectSetter(m),
            };
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SneakyThrows
    public static void upgradeName(AbstractCard card) {
        hUpgradeName.invoke(card);
    }

    public static final IVariable vBlock = new IVariable() {
        @Override
        public int value(AbstractCard card) { return card.block; }
        @Override
        public int baseValue(AbstractCard card) { return card.baseBlock; }
        @Override
        public boolean upgraded(AbstractCard card) { return card.upgradedBlock; }
        @Override
        public boolean isModified(AbstractCard card) { return card.isBlockModified; }
        @Override
        public void setValue(AbstractCard card, int amount) { card.block = amount; }
        @Override
        public void setBaseValue(AbstractCard card, int amount) { card.baseBlock = amount; }
        @Override @SneakyThrows
        public void upgrade(AbstractCard card, int amount) { hUpgradeBlock.invoke(card, amount); }
    };

    public static final IVariable vDamage = new IVariable() {
        @Override
        public int value(AbstractCard card) { return card.damage; }
        @Override
        public int baseValue(AbstractCard card) { return card.baseDamage; }
        @Override
        public boolean upgraded(AbstractCard card) { return card.upgradedDamage; }
        @Override
        public boolean isModified(AbstractCard card) { return card.isDamageModified; }
        @Override
        public void setValue(AbstractCard card, int amount) { card.damage = amount; }
        @Override
        public void setBaseValue(AbstractCard card, int amount) { card.baseDamage = amount; }
        @Override @SneakyThrows
        public void upgrade(AbstractCard card, int amount) { hUpgradeDamage.invoke(card, amount); }
    };

    public static final IVariable vMagic = new IVariable() {
        @Override
        public int value(AbstractCard card) { return card.magicNumber; }
        @Override
        public int baseValue(AbstractCard card) { return card.baseMagicNumber; }
        @Override
        public boolean upgraded(AbstractCard card) { return card.upgradedMagicNumber; }
        @Override
        public boolean isModified(AbstractCard card) { return card.isMagicNumberModified; }
        @Override
        public void setValue(AbstractCard card, int amount) { card.magicNumber = amount; }
        @Override
        public void setBaseValue(AbstractCard card, int amount) { card.baseMagicNumber = amount; }
        @Override @SneakyThrows
        public void upgrade(AbstractCard card, int amount) { hUpgradeMagic.invoke(card, amount); }
    };

    public static final IVariable vCost = new IVariable() {
        @Override
        public int value(AbstractCard card) { return card.costForTurn; }
        @Override
        public int baseValue(AbstractCard card) { return card.cost; }
        @Override
        public boolean upgraded(AbstractCard card) { return card.upgradedCost; }
        @Override
        public boolean isModified(AbstractCard card) { return card.isCostModified; }
        @Override
        public void setValue(AbstractCard card, int amount) { card.costForTurn = amount; }
        @Override
        public void setBaseValue(AbstractCard card, int amount) { card.cost = amount; }
        @Override @SneakyThrows
        public void upgrade(AbstractCard card, int amount) {
            hUpgradeCost.invoke(card, baseValue(card) + amount );
        }
    };

    public static final IVariable vExhaustive = buildForDynamicVariable(
            new ExhaustiveVariable(),
            ExhaustiveField.ExhaustiveFields.exhaustive::set,
            ExhaustiveVariable::setBaseValue,
            ExhaustiveVariable::upgrade
    );

    public static final IVariable vRefund = buildForDynamicVariable(
            new RefundVariable(),
            RefundFields.refund::set,
            RefundVariable::setBaseValue,
            RefundVariable::upgrade
    );

    public static final IVariable vPersist = buildForDynamicVariable(
            new PersistVariable(),
            PersistFields.persist::set,
            PersistFields::setBaseValue,
            PersistFields::upgrade
    );


    public static IVariable buildForDynamicVariable(
            final DynamicVariable dynVar,
            final BiConsumer<AbstractCard, Integer> fSetValue,
            final BiConsumer<AbstractCard, Integer> fSetBaseValue,
            final BiConsumer<AbstractCard, Integer> fUpgrade
    ) {
        return new IVariable() {
            public int value(AbstractCard card) { return dynVar.value(card); }
            public int baseValue(AbstractCard card) { return dynVar.baseValue(card); }
            public boolean upgraded(AbstractCard card) { return dynVar.upgraded(card); }
            public boolean isModified(AbstractCard card) { return dynVar.isModified(card); }
            @SneakyThrows
            public void setValue(AbstractCard card, int amount) { fSetValue.accept(card, amount); }
            public void setBaseValue(AbstractCard card, int amount) { fSetBaseValue.accept(card, amount); }
            public void upgrade(AbstractCard card, int amount) { fUpgrade.accept(card, amount); }
        };
    }
}
