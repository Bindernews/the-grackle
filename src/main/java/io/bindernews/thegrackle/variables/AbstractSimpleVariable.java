package io.bindernews.thegrackle.variables;

import basemod.abstracts.DynamicVariable;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.bindernews.bnsts.IVariable;
import lombok.*;

/**
 * This class splits the raw manipulation of a dynamic variable from its common
 * usage (e.g. {@link ExtraHitsVariable}), and reduces overall memory usage
 * by adding only one field to {@link AbstractCard} instead of three, and
 * only allocating instances as necessary.
 * <br/>
 * The field value should default to {@code null}. When one of the {@code set*}
 * methods of {@link AbstractSimpleVariable} is called, the internal field will be initialized.
 */
@AllArgsConstructor
public abstract class AbstractSimpleVariable extends DynamicVariable implements IVariable {
    /**
     * The field instance that we're storing values in.
     */
    protected final SpireField<VariableInst> field;
    public final VariableInst defaultValue;

    @Override
    public boolean isModified(AbstractCard card) {
        return value(card) != baseValue(card);
    }

    @Override
    public int value(AbstractCard card) {
        val f = field.get(card);
        return f == null ? defaultValue.value : f.value;
    }

    @Override
    public int baseValue(AbstractCard card) {
        val f = field.get(card);
        return f == null ? defaultValue.baseValue : f.baseValue;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        val f = field.get(card);
        return f == null ? defaultValue.upgraded : f.upgraded;
    }

    @Override
    public void setBaseValue(AbstractCard card, int amount) {
        val f = initField(card);
        f.value = amount;
        f.baseValue = amount;
        card.initializeDescription();
    }

    @Override
    public void setValue(AbstractCard card, int amount) {
        initField(card).value = amount;
    }

    @Override
    public void upgrade(AbstractCard card, int amount) {
        val f = initField(card);
        f.upgraded = true;
        f.baseValue += amount;
        f.value += amount;
    }

    /**
     * Returns the extraHits field of the card, initializing it if it's null.
     */
    private VariableInst initField(AbstractCard card) {
        var f = field.get(card);
        if (f == null) {
            f = new VariableInst(defaultValue.value, defaultValue.baseValue, defaultValue.upgraded);
            field.set(card, f);
        }
        return f;
    }

    /**
     * The backing field type that stores field data.
     */
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class VariableInst {
        public int value = -1;
        public int baseValue = -1;
        public boolean upgraded = false;
    }
}
