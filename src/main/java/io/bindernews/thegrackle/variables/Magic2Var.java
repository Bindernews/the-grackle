package io.bindernews.thegrackle.variables;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import io.bindernews.bnsts.IVariable;
import io.bindernews.thegrackle.GrackleMod;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class Magic2Var extends AbstractSimpleVariable implements IVariable {
    public static final String KEY = GrackleMod.makeId("magic2");

    public static final Magic2Var inst = new Magic2Var();

    private Magic2Var() {
        super(Fields.magic2, new VariableInst(-1, -1, false));
    }

    @Override
    public String key() {
        return KEY;
    }


    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<VariableInst> magic2 = new SpireField<>(() -> null);
    }
}
