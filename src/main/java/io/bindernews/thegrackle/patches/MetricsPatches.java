package io.bindernews.thegrackle.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import io.bindernews.thegrackle.Events;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Log4j2
@SuppressWarnings("unused")
public class MetricsPatches {

    @SpirePatch(clz = Metrics.class, method = "run")
    public static class RunPatch {
        public static void Postfix(Metrics metrics) {
            Events.metricsRun().emit(metrics);
        }
    }

    /**
     * Upload the metrics to the passed url in JSON format.
     * @param metrics Metrics data to upload
     * @param url URL to upload data to
     */
    public static void sendPost(Metrics metrics, String url) {
        try {
            Method m = Metrics.class.getDeclaredMethod("sendPost", String.class, String.class);
            m.setAccessible(true);
            m.invoke(metrics, url, null);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException var2) {
            log.error("Exception while sending metrics to " + url, var2);
        }
    }

    @SpirePatch(clz = GameOverScreen.class, method = "shouldUploadMetricData")
    public static class ShouldUploadMetricData {
        public static boolean Postfix(boolean returnValue) {
            return Settings.UPLOAD_DATA;
        }
    }
}
