package io.bindernews.thegrackle.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import io.bindernews.thegrackle.Grackle;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Log4j2
public class MetricsPatches {
    static String URL = "https://stats.grackle.bindernews.net";

    @SpirePatch(clz = Metrics.class, method = "run")
    public static class RunPatch {
        public static void Postfix(Metrics metrics) {
            tryUploadMetrics(metrics);
        }
    }

    @SpirePatch(clz = GameOverScreen.class, method = "shouldUploadMetricData")
    public static class ShouldUploadMetricData {
        public static boolean Postfix(boolean returnValue) {
            if (Grackle.isPlaying()) {
                returnValue = Settings.UPLOAD_DATA;
            }
            return returnValue;
        }
    }


    public static void tryUploadMetrics(Metrics metrics) {
        if (metrics.type == Metrics.MetricRequestType.UPLOAD_METRICS && Grackle.isPlaying()) {
            try {
                Method m = Metrics.class.getDeclaredMethod("sendPost", String.class, String.class);
                m.setAccessible(true);
                m.invoke(metrics, URL, null);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException var2) {
                log.error("Exception while sending metrics", var2);
            }
        }
    }
}
