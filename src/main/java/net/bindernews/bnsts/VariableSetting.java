package net.bindernews.bnsts;

import lombok.Data;
import net.bindernews.grackle.helper.IVariable;

@Data
public class VariableSetting {
    private IVariable variable;
    private int valueBase;
    private int valueUpgrade;
}
