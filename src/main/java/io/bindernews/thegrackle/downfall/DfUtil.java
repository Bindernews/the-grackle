package io.bindernews.thegrackle.downfall;

import charbosses.relics.AbstractCharbossRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import io.bindernews.bnsts.IField;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class DfUtil {

    public static final IField<AbstractCharbossRelic, AbstractRelic> fBaseRelic =
            IField.unreflect(AbstractCharbossRelic.class, "baseRelic");

    public static void setImagesFromBase(AbstractCharbossRelic relic) {
        val base = fBaseRelic.get(relic);
        relic.img = base.img;
        relic.largeImg = base.largeImg;
        relic.outlineImg = base.outlineImg;
    }

}
