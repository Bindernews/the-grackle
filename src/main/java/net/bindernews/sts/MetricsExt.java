package net.bindernews.sts;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.metrics.Metrics;

public class MetricsExt {
    private MetricsExt() {}

    private static final ReflectionHacks.RMethod fnAddData = ReflectionHacks.privateMethod(
            Metrics.class, "addData", Object.class, Object.class);
    private static final ReflectionHacks.RMethod fnSendPost = ReflectionHacks.privateMethod(
            Metrics.class, "sendPost", String.class, String.class);

    public static void addData(Metrics m, String key, Object value) {
        fnAddData.invoke(m, key, value);
    }

    public static void sendPost(Metrics m, String url, String fileToDelete) {
        fnSendPost.invoke(m, url, fileToDelete);
    }
}
